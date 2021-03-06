package com.fr.impl;

import com.fr.commons.dto.*;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.commons.enumeration.ErrorMessageEnum;
import com.fr.commons.enumeration.LanguageEnum;
import com.fr.commons.enumeration.TypeAccountValidation;
import com.fr.commons.enumeration.UserRoleTypeEnum;
import com.fr.commons.exception.*;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.*;
import com.fr.enums.CoverType;
import com.fr.repositories.ConnexionHistoryRepository;
import com.fr.repositories.FirebaseRegistrationRepository;
import com.fr.service.AccountBusinessService;
import com.fr.service.email.AccountMailerService;
import com.fr.transformers.ConnexionHistoryTransformer;
import com.fr.transformers.UserTransformer;
import com.fr.transformers.impl.SportTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: Wail DJENANE On June 01, 2016
 */

@Component
class AccountBusinessServiceImpl extends CommonControllerServiceImpl implements AccountBusinessService
{
	
	private final Logger LOGGER = LoggerFactory.getLogger(AccountBusinessServiceImpl.class);
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AccountMailerService accountMailerService;
	@Autowired
	private UserTransformer userTransformer;
	@Autowired
	private SportTransformer sportTransformer;
	@Autowired
	private ConnexionHistoryRepository connexionHistoryRepository;
	@Autowired
	private ConnexionHistoryTransformer connexionHistoryTransformer;
	@Autowired
	private FirebaseRegistrationRepository firebaseRegistrationRepository;
	
	@Value("${spring.app.account.recover.expiry.date}")
	private int daysBeforeExpiration;
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void saveNewUser(final SignUpDTO user)
	{
		
		checkRequiredAttributeUniqueness(user);
		
		final UserEntity newUser = this.userTransformer.signUpDtoToEntity(user);
		
		newUser.setAccountMaxActivationDate(SppotiUtils.generateExpiryDate(this.daysBeforeExpiration));
		
		// processing user Profile
		final RoleEntity profile = this.roleRepository.getByName(UserRoleTypeEnum.USER);
		
		if (profile == null) {
			this.LOGGER.info("Profile <{}> not found !!", UserRoleTypeEnum.USER.name());
			throw new EntityNotFoundException();
		}
		
		final Set<RoleEntity> roles = new HashSet<>();
		roles.add(profile);
		newUser.setRoles(roles);
		
		if (user.getPassword() != null) {
			final PasswordHistory p = new PasswordHistory(newUser.getPassword(), newUser);
			newUser.getPasswordHistories().add(p);
		}
		
		newUser.setParamEntity(new AccountParamEntity(true, true, newUser));
		
		this.userRepository.saveAndFlush(newUser);
		this.LOGGER.info("Account has been created for user : " + user);
		
		//		SendEmailToActivateAccount(newUser, TypeAccountValidation.signup);
	}
	
	private void checkRequiredAttributeUniqueness(final SignUpDTO user) {
		
		final Optional<UserEntity> userFoundByUsername = Optional
				.ofNullable(this.userRepository.findByUsernameAndDeletedFalse(user.getUsername()));
		final Optional<UserEntity> userFoundByEmail = Optional
				.ofNullable(this.userRepository.findByEmailAndDeletedFalse(user.getEmail()));
		
		final Optional<UserEntity> userFoundByFacebookId = user.getFacebookId() != null ?
				Optional.ofNullable(this.userRepository.findByFacebookIdAndDeletedFalse(user.getFacebookId())) :
				Optional.empty();
		
		final Optional<UserEntity> userFoundByGoogleId = user.getGoogleId() != null ?
				Optional.ofNullable(this.userRepository.findByGoogleIdAndDeletedFalse(user.getGoogleId())) :
				Optional.empty();
		
		final Optional<UserEntity> userFoundByTwitterId = user.getTwitterId() != null ?
				Optional.ofNullable(this.userRepository.findByTwitterIdAndDeletedFalse(user.getTwitterId())) :
				Optional.empty();
		
		userFoundByEmail.ifPresent(e -> {
			if (isAccountExist(e)) {
				throw new ConflictEmailException(ErrorMessageEnum.EMAIL_ALREADY_EXISTS.getMessage());
			}
		});
		userFoundByUsername.ifPresent(e -> {
			if (isAccountExist(e)) {
				throw new ConflictUsernameException(ErrorMessageEnum.USERNAME_ALREADY_EXISTS.getMessage());
			}
		});
		
		userFoundByFacebookId.ifPresent(e -> {
			if (isAccountExist(e)) {
				throw new ConflictFacebookIdException(ErrorMessageEnum.FACEBOOK_ID_ALREADY_EXISTS.getMessage());
			}
		});
		userFoundByGoogleId.ifPresent(e -> {
			if (isAccountExist(e)) {
				throw new ConflictGoogleIdException(ErrorMessageEnum.GOOGLE_ID_ALREADY_EXISTS.getMessage());
			}
		});
		userFoundByTwitterId.ifPresent(e -> {
			if (isAccountExist(e)) {
				throw new ConflictTwitterException(ErrorMessageEnum.TWITTER_ID_ALREADY_EXISTS.getMessage());
			}
		});
	}
	
	private boolean isAccountExist(final UserEntity u)
	{
		if (!u.isConfirmed()) {
			throw new AccountConfirmationLinkExpiredException("Account exist! Ask for another confirmation code.");
		}
		return true;
	}
	
	private void SendEmailToActivateAccount(final UserEntity user, final TypeAccountValidation type)
	{
		final UserDTO userDTO = this.userTransformer.modelToDto(user);
		final Thread thread = new Thread(() -> {
			this.sendConfirmationEmail(userDTO, user.getConfirmationCode(), type);
			this.LOGGER.info("Confirmation email has been sent successfully !");
		});
		thread.start();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void tryActivateAccount(final String code, final TypeAccountValidation type)
	{
		
		final Optional<UserEntity> optional = Optional
				.ofNullable(this.userRepository.getByConfirmationCodeAndDeletedFalseAndConfirmedFalse(code));
		
		optional.ifPresent(u -> {
			
			//Attempt to activate account after a signup.
			if (type.name().equals(TypeAccountValidation.signup.name())) {
			}
			//Attempt to activate account after edit email.
			else if (type.name().equals(TypeAccountValidation.preference_edit_email.name())) {
			}
			
			//Check if validation link still active.
			if (SppotiUtils.isDateExpired(u.getAccountMaxActivationDate())) {
				throw new AccountConfirmationLinkExpiredException(
						"Token has been expired! Ask for another confirmation code.");
			}
			
			u.setConfirmed(true);
			u.setAccountMaxActivationDate(SppotiUtils.generateOldDate(2));
			this.userRepository.save(u);
		});
		
		optional.orElseThrow(() -> new EntityNotFoundException("Token not valid"));
		
	}
	
	/**
	 * Send account email activation code.
	 *
	 * @param userDTO
	 * 		user to reach.
	 * @param code
	 * 		activation code.
	 * @param type
	 * 		activation type.
	 */
	private void sendConfirmationEmail(final UserDTO userDTO, final String code, final TypeAccountValidation type)
	{
		this.accountMailerService.sendCreateAccountConfirmationEmail(userDTO, code, type);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void updateUser(final UserDTO userDTO)
	{
		
		final UserEntity connectedUser = getConnectedUser();
		
		if (StringUtils.hasText(userDTO.getFirstName())) {
			connectedUser.setFirstName(userDTO.getFirstName());
		}
		if (StringUtils.hasText(userDTO.getLastName())) {
			connectedUser.setLastName(userDTO.getLastName());
		}
		if (StringUtils.hasText(userDTO.getAddress())) {
			connectedUser.getAddresses().add(new AddressEntity(userDTO.getAddress()));
		}
		if (StringUtils.hasText(userDTO.getUsername())) {
			connectedUser.setUsername(userDTO.getUsername());
		}
		if (StringUtils.hasText(userDTO.getPhone())) {
			connectedUser.setTelephone(userDTO.getPhone());
		}
		if (StringUtils.hasText(userDTO.getTimeZone())) {
			connectedUser.setTimeZone(userDTO.getTimeZone());
		}
		if (Objects.nonNull(userDTO.isFirstConnexion())) {
			connectedUser.setFirstConnexion(false);
		}
		if (Objects.nonNull(userDTO.getBirthDate())) {
			connectedUser.setDateBorn(userDTO.getBirthDate());
		}
		if (Objects.nonNull(userDTO.isProfileComplete())) {
			connectedUser.setProfileComplete(userDTO.isProfileComplete());
		}
		if (Objects.nonNull(userDTO.getGender())) {
			connectedUser.setGender(userDTO.getGender());
		}
		if (StringUtils.hasText(userDTO.getPassword()) && StringUtils.isEmpty(userDTO.getEmail()) &&
				StringUtils.hasText(userDTO.getOldPassword())) {
			
			//Check that old password is correct
			if (!this.passwordEncoder.matches(userDTO.getOldPassword(), connectedUser.getPassword())) {
				throw new BusinessGlobalException("Old password not correct");
			}
			
			final String encodedPassword = this.passwordEncoder.encode(userDTO.getPassword());
			connectedUser.setPassword(encodedPassword);
			
			final PasswordHistory p = new PasswordHistory();
			p.setPassword(encodedPassword);
			p.setUser(connectedUser);
			connectedUser.getPasswordHistories().add(p);
		}
		/*
			email -- User must confirm new messages address to login next time.
         */
		else if (StringUtils.hasText(userDTO.getEmail()) && StringUtils.hasText(userDTO.getPassword())) {
			
			//Check that password is correct
			if (!this.passwordEncoder.matches(userDTO.getPassword(), connectedUser.getPassword())) {
				throw new BusinessGlobalException("Password not correct");
			}
			
			connectedUser.setEmail(userDTO.getEmail());
			//			connectedUser.setConfirmed(false);
			//			connectedUser.setAccountMaxActivationDate(SppotiUtils.generateExpiryDate(1));
			//			final String confirmationCode = SppotiUtils.generateConfirmationKey();
			//			connectedUser.setConfirmationCode(confirmationCode);
			
			//			sendConfirmationEmail(userDTO, confirmationCode, TypeAccountValidation.preference_edit_email);
		} else if (StringUtils.hasText(userDTO.getLanguage())) {
			connectedUser.setLanguageEnum(LanguageEnum.valueOf(userDTO.getLanguage()));
		}
		
		this.userRepository.save(connectedUser);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void unSelectOldResource(final Long userId, final int i)
	{
		final ResourcesEntity resourcesEntity = this.resourceRepository.getByUserIdAndTypeAndSelectedTrue(userId, i);
		if (resourcesEntity != null) {
			resourcesEntity.setSelected(false);
			this.resourceRepository.save(resourcesEntity);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void updateAvatarAndCover(final UserDTO user)
	{
		
		final UserEntity connectedUser = getConnectedUser();
		
		final ResourcesEntity resource = new ResourcesEntity();
		resource.setSelected(true);
		
		if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
			resource.setUrl(user.getAvatar());
			resource.setType(1);
			resource.setTypeExtension(1);
			this.unSelectOldResource(connectedUser.getId(), 1);
		} else if (user.getCover() != null && !user.getCover().isEmpty() &&
				(CoverType.IMAGE.type() == user.getCoverType() || CoverType.VIDEO.type() == user.getCoverType())) {
			resource.setUrl(user.getCover());
			resource.setType(2);
			resource.setTypeExtension(user.getCoverType());
			this.unSelectOldResource(connectedUser.getId(), 2);
		}
		
		resource.setUser(connectedUser);
		connectedUser.getResources().add(resource);
		
		this.userRepository.save(connectedUser);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @param userDTO
	 */
	@Override
	@Transactional
	public void sendRecoverAccountEmail(final SignUpDTO userDTO)
	{
		
		final Optional<UserEntity> optional = Optional
				.ofNullable(this.userRepository.findByEmailAndDeletedFalse(userDTO.getEmail()));
		
		optional.ifPresent(u -> {
			
			if (!u.isConfirmed()) {
				throw new AccountConfirmationLinkExpiredException("Account confirmation email has expired or not sent");
			}
			
			final String code = SppotiUtils.generateConfirmationKey();
			
			final Date tokenExpiryDate = SppotiUtils.generateExpiryDate(this.daysBeforeExpiration);
			
			u.setRecoverCodeCreationDate(tokenExpiryDate);
			u.setRecoverCode(code);
			this.userRepository.save(u);
			this.LOGGER.info("Recover password email sent to {} ", u.getEmail());
			
			this.accountMailerService
					.sendRecoverPasswordEmail(this.userTransformer.modelToDto(u), code, tokenExpiryDate);
		});
		
		this.LOGGER.info("Email {} not found in database", userDTO.getEmail());
		optional.orElseThrow(EntityNotFoundException::new);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void recoverAccount(final UserDTO userDTO, final String code)
	{
		
		final Optional<UserEntity> optional = Optional
				.ofNullable(this.userRepository.getByRecoverCodeAndDeletedFalseAndConfirmedTrue(code));
		
		optional.ifPresent(u -> {
			
			if (SppotiUtils.isDateExpired(u.getRecoverCodeCreationDate())) {
				this.LOGGER.info("Token expired for user: " + u.getEmail());
				throw new BusinessGlobalException("Your token has been expired");
			}
			
			u.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));
			this.userRepository.save(u);
		});
		
		optional.orElseThrow(() -> new EntityNotFoundException("Account not found !"));
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void generateNewConfirmationEmail(final UserDTO userDTO)
	{
		
		final Optional<UserEntity> optional = Optional
				.ofNullable(this.userRepository.getByEmailAndDeletedFalseAndConfirmedTrue(userDTO.getEmail()));
		
		optional.ifPresent(u -> {
			if (u.isConfirmed()) {
				throw new BusinessGlobalException("Account Already activated");
			}
			//generate new code
			final String confirmationCode = SppotiUtils.generateConfirmationKey();
			u.setConfirmationCode(confirmationCode);
			//generate new expiry date
			u.setAccountMaxActivationDate(SppotiUtils.generateExpiryDate(this.daysBeforeExpiration));
			//update
			this.userRepository.saveAndFlush(u);
			//send email
			SendEmailToActivateAccount(u, TypeAccountValidation.preference_edit_email);
			this.LOGGER.info("New activation link has been sent to user: " + userDTO.getEmail());
		});
		
		optional.orElseThrow(() -> new EntityNotFoundException("Email not associated to an account"));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDTO fillAccountResponse(final UserEntity entity)
	{
		entity.setPassword(null);
		return this.userTransformer.modelToDto(entity);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDTO getAnyUserProfileData(final String username)
	{
		
		final UserEntity targetUser = this.getUserByLogin(username, true);
		if (targetUser == null) {
			throw new EntityNotFoundException("Target user id not found");
		}
		
		targetUser.setPassword(null);//do not return password
		targetUser.setConnectedUserId(getConnectedUserId());
		final UserDTO user = this.userTransformer.modelToDto(targetUser);
		
		//Map all user sports
		final List<SportDTO> sportDTOs = targetUser.getRelatedSports().stream().map(this.sportTransformer::modelToDto)
				.collect(Collectors.toList());
		user.setSportDTOs(sportDTOs);
		
		if (!targetUser.getAddresses().isEmpty()) {
			user.setAddress(targetUser.getAddresses().first().getAddress());
		}
		
		return user;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void deactivateAccount(final String userId) {
		final Optional<UserEntity> optional = this.userRepository
				.getByUuidAndConfirmedTrueAndDeletedFalseAndConfirmedTrue(userId);
		
		optional.ifPresent(u -> {
			u.setDeleted(true);
			u.setDeactivationDate(new Date());
			this.userRepository.save(u);
			
			//TODO: send email
		});
		
		optional.orElseThrow(() -> new EntityNotFoundException("Account not found"));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ConnexionHistoryDto saveConnexionHistory(final ConnexionHistoryDto historyDto) {
		
		if (getConnectedUser() == null) {
			return new ConnexionHistoryDto();
		}
		
		historyDto.setUserId(getConnectedUser().getId());
		return this.connexionHistoryTransformer.modelToDto(
				this.connexionHistoryRepository.save(this.connexionHistoryTransformer.dtoToModel(historyDto)));
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void saveFirebaseRegistrationKey(final FirebaseDTO user) {
		final Optional<UserEntity> userOptional = this.userRepository
				.getByUuidAndDeletedFalseAndConfirmedTrue(user.getUserId());
		userOptional.ifPresent(u -> {
			
			final Optional<FirebaseRegistrationEntity> firebaseRegistrationEntity = this.firebaseRegistrationRepository
					.findByRegistrationKey(user.getRegistrationId());
			
			if (!firebaseRegistrationEntity.isPresent()) {
				final FirebaseRegistrationEntity entity = new FirebaseRegistrationEntity();
				entity.setRegistrationKey(user.getRegistrationId());
				entity.setUser(u);
				entity.setDeviceConnected(true);
				this.firebaseRegistrationRepository.save(entity);
				this.LOGGER.info("Firebase registration has been added: {}", entity);
			}
			
			firebaseRegistrationEntity.ifPresent(f -> {
				f.setDeviceConnected(true);
				this.firebaseRegistrationRepository.save(f);
				this.LOGGER.info("Firebase registration has been updated: {}", f.getRegistrationKey());
			});
			
			updateFirebaseTokenInSecurityContext(user.getRegistrationId());
		});
		userOptional.orElseThrow(
				() -> new BusinessGlobalException("User id in firebase request not found in DB, {}", user.getUserId()));
	}
	
	private void updateFirebaseTokenInSecurityContext(final String firebaseToken) {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		final AccountUserDetails accountUserDetails = (AccountUserDetails) principal;
		accountUserDetails.setFirebaseToken(firebaseToken);
		
		final Authentication newAuth = new UsernamePasswordAuthenticationToken(principal, auth.getCredentials(),
				auth.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(newAuth);
		
		this.LOGGER.info("Firebase token has been added to security context");
	}
}