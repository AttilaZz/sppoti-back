package com.fr.impl;

import com.fr.commons.dto.SignUpRequestDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.GenderEnum;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.exception.ConflictEmailException;
import com.fr.commons.exception.ConflictUsernameException;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.AddressEntity;
import com.fr.entities.ResourcesEntity;
import com.fr.entities.RoleEntity;
import com.fr.entities.UserEntity;
import com.fr.enums.CoverType;
import com.fr.mail.AccountMailer;
import com.fr.commons.enumeration.UserRoleTypeEnum;
import com.fr.service.AccountControllerService;
import com.fr.transformers.impl.UserTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by: Wail DJENANE On June 01, 2016
 */

@Component
class AccountControllerServiceImpl extends AbstractControllerServiceImpl implements AccountControllerService {

    private Logger LOGGER = Logger.getLogger(AccountControllerServiceImpl.class);

    private PasswordEncoder passwordEncoder;

    private final AccountMailer accountMailer;

    private final UserTransformer userTransformer;

    @Value("${spring.app.account.recover.expiry.date}")
    private int daysBeforeExpiration;

    @Autowired
    public AccountControllerServiceImpl(AccountMailer accountMailer, PasswordEncoder passwordEncoder, UserTransformer userTransformer) {
        this.accountMailer = accountMailer;
        this.passwordEncoder = passwordEncoder;
        this.userTransformer = userTransformer;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void saveNewUser(SignUpRequestDTO user) {

        UserEntity newUser = new UserEntity();

        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setDateBorn(user.getDateBorn());
        newUser.setGender(GenderEnum.valueOf(user.getGender()));
        newUser.setEmail(user.getEmail());

        String confirmationCode = SppotiUtils.generateConfirmationKey();
        newUser.setConfirmationCode(confirmationCode);

        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        newUser.setUsername(user.getUsername().trim());
        newUser.setAccountMaxActivationDate(SppotiUtils.generateExpiryDate(daysBeforeExpiration));

//        for (Long sportId : user.getSportId()) {
//            // if the parsed SportDTO exist in database == correct request
//            SportEntity mSport = accountControllerService.getSportById(sportId);
//            if (mSport != null) {
//                userSports.add(mSport);
//            } else {
//                LOGGER.info("INSCRIPTION: le nom de SportDTO envoyé n'est pas reconnu");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//            }
//        }

//        newUser.setRelatedSports(userSports);

		/*
         * processing user Profile
		 */
        RoleEntity profile = roleRepository.getByName(UserRoleTypeEnum.USER.name());

        if (profile == null) {
            throw new EntityNotFoundException("Profile name <" + UserRoleTypeEnum.USER.name() + "> not found !!");
        }

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(profile);
        newUser.setRoles(roles);

        /*
            if username or email exist, account valid:
             - reject sign_up
             - delete old account
         */
        Optional<UserEntity> checkUsername = Optional.ofNullable(userRepository.getByUsernameAndDeletedFalse(user.getUsername()));
        Optional<UserEntity> checkEmail = Optional.ofNullable(userRepository.getByEmailAndDeletedFalse(user.getEmail()));

        checkEmail.ifPresent(u -> {
            if (!SppotiUtils.isDateExpired(u.getAccountMaxActivationDate())) {
                throw new ConflictEmailException("Email already exist");
            } else {
                //delete founded account to allow new user using this email
                userRepository.delete(u);
            }

        });
        checkUsername.ifPresent(u -> {
            if (!SppotiUtils.isDateExpired(u.getAccountMaxActivationDate())) {
                throw new ConflictUsernameException("Username already exist");
            } else {
                //delete founded account to allow new user using this username
                userRepository.delete(u);
            }
        });

        //save new user.
        userRepository.save(newUser);
        LOGGER.info("Account has been created for user : " + user.getEmail());

        /*
         * Send email to confirm account
         */
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        Thread thread = new Thread(() -> {
            this.sendConfirmationEmail(userDTO, confirmationCode);
            LOGGER.info("Confirmation email has been sent successfully !");
        });
        thread.start();

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void tryActivateAccount(String code) {

        Optional<UserEntity> optional = Optional.ofNullable(userRepository.getByConfirmationCodeAndDeletedFalse(code));

        optional.ifPresent(u -> {

            if (SppotiUtils.isDateExpired(u.getAccountMaxActivationDate())) {
                userRepository.delete(u);
                LOGGER.info("Token expired for user: " + u.getEmail());
                throw new BusinessGlobalException("Your token has been expired and deleted, try creating new account " +
                        "and confirm before 24h");
            }

            u.setConfirmed(true);
            userRepository.save(u);
        });

        optional.orElseThrow(() -> new EntityNotFoundException("Account not found, " +
                "This account may not being confirmed for more than 24h and same information was set in another " +
                "account"));

    }

    /**
     * Send account email activation code.
     *
     * @param userDTO user to reach.
     * @param code    account confirmation code.
     */
    private void sendConfirmationEmail(UserDTO userDTO, String code) {

        final Thread thread = new Thread(() -> {
            this.accountMailer.sendAccountConfirmationEmail(userDTO, code);
        });
        thread.start();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void updateUser(UserDTO userDTO) {

        UserEntity connectedUser = getConnectedUser();

        if (!StringUtils.isEmpty(userDTO.getFirstName())) {
            connectedUser.setFirstName(userDTO.getFirstName());
        }
        //last name
        if (!StringUtils.isEmpty(userDTO.getLastName())) {
            connectedUser.setLastName(userDTO.getLastName());
        }
        //address
        if (!StringUtils.isEmpty(userDTO.getAddress())) {
            connectedUser.getAddresses().add(new AddressEntity(userDTO.getAddress()));
        }
        //username
        if (!StringUtils.isEmpty(userDTO.getUsername())) {
            connectedUser.setUsername(userDTO.getUsername());
        }
        //phone
        if (!StringUtils.isEmpty(userDTO.getPhone())) {
            connectedUser.setTelephone(userDTO.getPhone());
        }
        //password
        if (!StringUtils.isEmpty(userDTO.getPassword())) {
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            connectedUser.setPassword(encodedPassword);
        }
        //email
        if (!StringUtils.isEmpty(userDTO.getEmail())) {
            connectedUser.setEmail(userDTO.getEmail());

            String confirmationCode = SppotiUtils.generateConfirmationKey();
            connectedUser.setConfirmationCode(confirmationCode);

            sendConfirmationEmail(userDTO, confirmationCode);
        }

        userRepository.save(connectedUser);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void unSelectOldResource(Long userId, int i) {
        ResourcesEntity resourcesEntity = resourceRepository.getByUserIdAndTypeAndIsSelectedTrue(userId, i);
        if (resourcesEntity != null) {
            resourcesEntity.setSelected(false);
            resourceRepository.save(resourcesEntity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.getByUsernameAndDeletedFalse(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO fillUserResponse(UserEntity targetUser, UserEntity connectedUser) {
        return super.fillUserResponse(targetUser, connectedUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateAvatarAndCover(UserDTO user) {

        UserEntity connectedUser = getConnectedUser();

        ResourcesEntity resource = new ResourcesEntity();
        resource.setSelected(true);

        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            resource.setUrl(user.getAvatar());
            resource.setType(1);
            resource.setTypeExtension(1);
            this.unSelectOldResource(connectedUser.getId(), 1);
        } else if (user.getCover() != null && !user.getCover().isEmpty() && (CoverType.IMAGE.type() == user.getCoverType()
                || CoverType.VIDEO.type() == user.getCoverType())) {
            resource.setUrl(user.getCover());
            resource.setType(2);
            resource.setTypeExtension(user.getCoverType());
            this.unSelectOldResource(connectedUser.getId(), 2);
        }

        resource.setUser(connectedUser);
        connectedUser.getResources().add(resource);

        userRepository.save(connectedUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void sendRecoverAccountEmail(UserDTO userDTO) {

        Optional<UserEntity> optional = Optional.ofNullable(userRepository.getByEmailAndDeletedFalse(userDTO.getEmail()));

        optional.ifPresent(u -> {
            String code = SppotiUtils.generateConfirmationKey();

            Date tokenExpiryDate = SppotiUtils.generateExpiryDate(daysBeforeExpiration);

            u.setRecoverCodeCreationDate(tokenExpiryDate);
            u.setRecoverCode(code);
            userRepository.save(u);

            final Thread thread = new Thread(() -> this.accountMailer.sendRecoverPasswordEmail(userTransformer.modelToDto(u), code, tokenExpiryDate));
            thread.start();
        });

        optional.orElseThrow(() -> new EntityNotFoundException("Email not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void recoverAccount(UserDTO userDTO, String code) {

        Optional<UserEntity> optional = Optional.ofNullable(userRepository.getByRecoverCodeAndDeletedFalse(code));

        optional.ifPresent(u -> {

            if (SppotiUtils.isDateExpired(u.getRecoverCodeCreationDate())) {
                LOGGER.info("Token expired for user: " + u.getEmail());
                throw new BusinessGlobalException("Your token has been expired");
            }

            u.setPassword(userDTO.getPassword());
            userRepository.save(u);
        });

        optional.orElseThrow(() -> new EntityNotFoundException("Account not found !"));

    }
}