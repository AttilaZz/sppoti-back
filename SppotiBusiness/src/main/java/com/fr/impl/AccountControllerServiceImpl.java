package com.fr.impl;

import com.fr.commons.dto.SignUpDTO;
import com.fr.commons.dto.SportDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.LanguageEnum;
import com.fr.commons.enumeration.TypeAccountValidation;
import com.fr.commons.enumeration.UserRoleTypeEnum;
import com.fr.commons.exception.AccountConfirmationLinkExpiredException;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.exception.ConflictEmailException;
import com.fr.commons.exception.ConflictUsernameException;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.*;
import com.fr.enums.CoverType;
import com.fr.mail.AccountMailer;
import com.fr.service.AccountControllerService;
import com.fr.transformers.UserTransformer;
import com.fr.transformers.impl.SportTransformer;
import com.fr.transformers.impl.UserTransformerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
class AccountControllerServiceImpl extends AbstractControllerServiceImpl implements AccountControllerService
{
	
	/** Class logger. */
	private final Logger LOGGER = LoggerFactory.getLogger(AccountControllerServiceImpl.class);
	
	/** Spring security Password encoder. */
	private final PasswordEncoder passwordEncoder;
	
	/** {@link AccountMailer} to send emails. */
	private final AccountMailer accountMailer;
	
	/** {@link UserEntity} transformer. */
	private final UserTransformer userTransformer;
	
	/** {@link com.fr.entities.SportEntity} transformer. */
	private final SportTransformer sportTransformer;
	
	/** Days before expiration. */
	@Value("${spring.app.account.recover.expiry.date}")
	private int daysBeforeExpiration;
	
	/** Init dependencies. */
	@Autowired
	public AccountControllerServiceImpl(final AccountMailer accountMailer, final PasswordEncoder passwordEncoder,
										final UserTransformerImpl userTransformer,
										final SportTransformer sportTransformer)
	{
		this.accountMailer = accountMailer;
		this.passwordEncoder = passwordEncoder;
		this.userTransformer = userTransformer;
		this.sportTransformer = sportTransformer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void saveNewUser(final SignUpDTO user)
	{

        /*
			if username or email exist, account valid:
             - reject sign_up
             - delete old account
         */
		final Optional<UserEntity> checkUsername = Optional
				.ofNullable(this.userRepository.getByUsernameAndDeletedFalse(user.getUsername()));
		final Optional<UserEntity> checkEmail = Optional
				.ofNullable(this.userRepository.getByEmailAndDeletedFalse(user.getEmail()));
		
		checkEmail.ifPresent(e -> {
			if (accountExist(e)) {
				throw new ConflictEmailException("L'adresse email existe déjà.");
			}
		});
		checkUsername.ifPresent(e -> {
			if (accountExist(e)) {
				throw new ConflictUsernameException("Le username existe déjà.");
			}
		});
		
		final UserEntity newUser = this.userTransformer.signUpDtoToEntity(user);
		newUser.setFirstName(SppotiUtils.normaliser(newUser.getFirstName()));
		newUser.setLastName(SppotiUtils.normaliser(newUser.getLastName()));
		
		newUser.setAccountMaxActivationDate(SppotiUtils.generateExpiryDate(this.daysBeforeExpiration));
		
		// processing user Profile
		final RoleEntity profile = this.roleRepository.getByName(UserRoleTypeEnum.USER);
		
		if (profile == null) {
			throw new EntityNotFoundException("Profile name <" + UserRoleTypeEnum.USER.name() + "> not found !!");
		}
		
		final Set<RoleEntity> roles = new HashSet<>();
		roles.add(profile);
		newUser.setRoles(roles);
		
		//save new user.
		this.userRepository.save(newUser);
		this.LOGGER.info("Account has been created for user : " + user.getEmail());

        /*
		 * Send email to confirm account
         */
		SendEmailToActivateAccount(newUser);
		
	}
	
	/** Send Email to activate new account. */
	private void SendEmailToActivateAccount(final UserEntity user)
	{
		final UserDTO userDTO = this.userTransformer.modelToDto(user);
		final Thread thread = new Thread(() -> {
			this.sendConfirmationEmail(userDTO, user.getConfirmationCode());
			this.LOGGER.info("Confirmation email has been sent successfully !");
		});
		thread.start();
	}
	
	/** Test if account exists. */
	private boolean accountExist(final UserEntity u)
	{
		if (!SppotiUtils.isDateExpired(u.getAccountMaxActivationDate()) || u.isConfirmed()) {
			return true;
		} else if (!u.isConfirmed()) {
			throw new AccountConfirmationLinkExpiredException("Account exist! Ask for another confirmation code.");
		}
		return false;
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
				throw new AccountConfirmationLinkExpiredException("Account exist! Ask for another confirmation code.");
			}
			
			u.setConfirmed(true);
			this.userRepository.save(u);
		});
		
		optional.orElseThrow(() -> new EntityNotFoundException("Token not found, " +
				"This account may not being confirmed for more than 24h or it has been already confirmed"));
		
	}
	
	/**
	 * Send account email activation code.
	 *
	 * @param userDTO
	 * 		user to reach.
	 * @param code
	 * 		account confirmation code.
	 */
	private void sendConfirmationEmail(final UserDTO userDTO, final String code)
	{
		
		final Thread thread = new Thread(() -> {
			this.accountMailer.sendCreateAccountConfirmationEmail(userDTO, code);
		});
		thread.start();
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
		//last name
		if (StringUtils.hasText(userDTO.getLastName())) {
			connectedUser.setLastName(userDTO.getLastName());
		}
		//address
		if (StringUtils.hasText(userDTO.getAddress())) {
			connectedUser.getAddresses().add(new AddressEntity(userDTO.getAddress()));
		}
		//username
		if (StringUtils.hasText(userDTO.getUsername())) {
			connectedUser.setUsername(userDTO.getUsername());
		}
		//phone
		if (StringUtils.hasText(userDTO.getPhone())) {
			connectedUser.setTelephone(userDTO.getPhone());
		}
		//timeZone
		if (StringUtils.hasText(userDTO.getTimeZone())) {
			connectedUser.setTimeZone(userDTO.getTimeZone());
		}
		//password
		else if (StringUtils.hasText(userDTO.getPassword()) && StringUtils.isEmpty(userDTO.getEmail()) &&
				StringUtils.hasText(userDTO.getOldPassword())) {
			
			//Check that old password is correct
			if (!this.passwordEncoder.matches(userDTO.getOldPassword(), connectedUser.getPassword())) {
				throw new BusinessGlobalException("Old password not correct");
			}
			
			final String encodedPassword = this.passwordEncoder.encode(userDTO.getPassword());
			connectedUser.setPassword(encodedPassword);
		}
		/*
			email --
            User must confirm new email address to login next time.
         */
		else if (StringUtils.hasText(userDTO.getEmail()) && StringUtils.hasText(userDTO.getPassword())) {
			
			//Check that password is correct
			if (!this.passwordEncoder.matches(userDTO.getPassword(), connectedUser.getPassword())) {
				throw new BusinessGlobalException("Password not correct");
			}
			
			connectedUser.setEmail(userDTO.getEmail());
			connectedUser.setConfirmed(false);
			final String confirmationCode = SppotiUtils.generateConfirmationKey();
			connectedUser.setConfirmationCode(confirmationCode);
			
			sendConfirmationEmail(userDTO, confirmationCode);
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
		final ResourcesEntity resourcesEntity = this.resourceRepository.getByUserIdAndTypeAndIsSelectedTrue(userId, i);
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
				.ofNullable(this.userRepository.getByEmailAndDeletedFalse(userDTO.getEmail()));
		
		optional.ifPresent(u -> {
			final String code = SppotiUtils.generateConfirmationKey();
			
			final Date tokenExpiryDate = SppotiUtils.generateExpiryDate(this.daysBeforeExpiration);
			
			u.setRecoverCodeCreationDate(tokenExpiryDate);
			u.setRecoverCode(code);
			this.userRepository.save(u);
			this.LOGGER.info("Recover password email sent tocommit: " + u.getEmail());
			
			
			final Thread thread = new Thread(() -> this.accountMailer
					.sendRecoverPasswordEmail(this.userTransformer.modelToDto(u), code, tokenExpiryDate));
			thread.start();
		});
		
		optional.orElseThrow(() -> new EntityNotFoundException("Email not found"));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void recoverAccount(final UserDTO userDTO, final String code)
	{
		
		final Optional<UserEntity> optional = Optional
				.ofNullable(this.userRepository.getByRecoverCodeAndDeletedFalse(code));
		
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
				.ofNullable(this.userRepository.getByEmailAndDeletedFalse(userDTO.getEmail()));
		
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
			SendEmailToActivateAccount(u);
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
	public UserDTO handleFriendShip(final String username, final Long connectedUserId)
	{
		
		final UserEntity targetUser = this.getUserByLogin(username, true);
		if (targetUser == null) {
			throw new EntityNotFoundException("Target user id not found");
		}
		final UserEntity connectedUser = this.getUserById(connectedUserId);
		
		targetUser.setPassword(null);//do not return password
		final UserDTO user = this.userTransformer.modelToDto(targetUser);
		user.setFriendStatus(GlobalAppStatusEnum.PUBLIC_RELATION.getValue());
		
		if (connectedUser != null) {
			if (!connectedUser.getId().equals(targetUser.getId())) {
				/* manage requests sent to me. */
				FriendShipEntity friendShip;
				
				friendShip = this.friendShipRepository
						.findLastByFriendUuidAndUserUuidAndDeletedFalseOrderByDatetimeCreatedDesc(
								connectedUser.getUuid(), targetUser.getUuid());
				
				if (friendShip == null) {
					friendShip = this.friendShipRepository
							.findLastByFriendUuidAndUserUuidAndDeletedFalseOrderByDatetimeCreatedDesc(
									targetUser.getUuid(), connectedUser.getUuid());
				}
				
				if (friendShip == null) {
					user.setFriendStatus(GlobalAppStatusEnum.PUBLIC_RELATION.getValue());
				} else {
					
					//We are friend.
					if (friendShip.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)) {
						user.setFriendStatus(GlobalAppStatusEnum.CONFIRMED.getValue());
						
						//Friend request waiting to be accepted by me.
					} else if (friendShip.getStatus().equals(GlobalAppStatusEnum.PENDING)) {
						user.setFriendStatus(GlobalAppStatusEnum.PENDING.getValue());
						
						//Friend request refused by me.
					} else if (friendShip.getStatus().equals(GlobalAppStatusEnum.REFUSED)) {
						user.setFriendStatus(GlobalAppStatusEnum.REFUSED.getValue());
						
					}
				}
				/*  Manage request sent by me. */
				if (!this.friendShipRepository.findByUserUuidAndFriendUuidAndStatusAndDeletedFalse(targetUser.getUuid(),
						connectedUser.getUuid(), GlobalAppStatusEnum.PENDING).isEmpty()) {
					user.setFriendStatus(GlobalAppStatusEnum.PENDING_SENT.getValue());
				}
			}
			user.setMyProfile(connectedUser.getId().equals(targetUser.getId()));
		} else {
			user.setMyProfile(true);
		}
		
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
	public void deactivateAccount(final int userId) {
		final Optional<UserEntity> optional = this.userRepository.getByUuidAndConfirmedTrueAndDeletedFalse(userId);
		
		optional.ifPresent(u -> {
			u.setDeleted(true);
			u.setDeactivationDate(new Date());
			this.userRepository.save(u);
		});
		
		optional.orElseThrow(() -> new EntityNotFoundException("Account not found"));
	}
}