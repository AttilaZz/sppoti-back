package com.fr.impl;

import com.fr.commons.dto.SignUpDTO;
import com.fr.commons.dto.SportDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.LanguageEnum;
import com.fr.commons.enumeration.UserRoleTypeEnum;
import com.fr.commons.exception.AccountConfirmationLinkExpiredException;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.exception.ConflictUsernameException;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.*;
import com.fr.enums.CoverType;
import com.fr.mail.AccountMailer;
import com.fr.service.AccountControllerService;
import com.fr.transformers.UserTransformer;
import com.fr.transformers.impl.SportTransformer;
import com.fr.transformers.impl.UserTransformerImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
class AccountControllerServiceImpl extends AbstractControllerServiceImpl implements AccountControllerService {

    /**
     * Class logger.
     */
    private Logger LOGGER = Logger.getLogger(AccountControllerServiceImpl.class);

    /**
     * Spring security Password encoder.
     */
    private PasswordEncoder passwordEncoder;

    /**
     * {@link AccountMailer} to send emails.
     */
    private final AccountMailer accountMailer;

    /**
     * {@link UserEntity} transformer;
     */
    private final UserTransformer userTransformer;

    /**
     * {@link com.fr.entities.SportEntity} transformer.
     */
    private final SportTransformer sportTransformer;

    @Value("${spring.app.account.recover.expiry.date}")
    private int daysBeforeExpiration;

    /**
     * Init dependencies.
     */
    @Autowired
    public AccountControllerServiceImpl(AccountMailer accountMailer, PasswordEncoder passwordEncoder, UserTransformerImpl userTransformer, SportTransformer sportTransformer) {
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
    public void saveNewUser(SignUpDTO user) {

        /*
            if username or email exist, account valid:
             - reject sign_up
             - delete old account
         */
        Optional<UserEntity> checkUsername = Optional.ofNullable(userRepository.getByUsernameAndDeletedFalse(user.getUsername()));
        Optional<UserEntity> checkEmail = Optional.ofNullable(userRepository.getByEmailAndDeletedFalse(user.getEmail()));

        checkEmail.ifPresent(this::checkifAccountExist);
        checkUsername.ifPresent(this::checkifAccountExist);

        UserEntity newUser = userTransformer.signUpDtoToEntity(user);

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
        RoleEntity profile = roleRepository.getByName(UserRoleTypeEnum.USER);

        if (profile == null) {
            throw new EntityNotFoundException("Profile name <" + UserRoleTypeEnum.USER.name() + "> not found !!");
        }

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(profile);
        newUser.setRoles(roles);

        //save new user.
        userRepository.save(newUser);
        LOGGER.info("Account has been created for user : " + user.getEmail());

        /*
         * Send email to confirm account
         */
        SendEmailToActivateAccount(newUser);

    }

    /**
     * Send Email to activate new account.
     */
    private void SendEmailToActivateAccount(UserEntity user) {
        UserDTO userDTO = userTransformer.modelToDto(user);
        Thread thread = new Thread(() -> {
            this.sendConfirmationEmail(userDTO, user.getConfirmationCode());
            LOGGER.info("Confirmation email has been sent successfully !");
        });
        thread.start();
    }

    private void checkifAccountExist(UserEntity u) {
        if (!SppotiUtils.isDateExpired(u.getAccountMaxActivationDate()) || u.isConfirmed()) {
            throw new ConflictUsernameException("Username already exist");
        } else if (!u.isConfirmed()) {
            throw new AccountConfirmationLinkExpiredException("Account exist, but not confirmed yet ! Ask for another confirmation code.");
        }
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
        else if (!StringUtils.isEmpty(userDTO.getPassword()) && StringUtils.isEmpty(userDTO.getEmail()) && !StringUtils.isEmpty(userDTO.getOldPassword())) {

            //Check that old password is correct
            if (!passwordEncoder.matches(userDTO.getOldPassword(), connectedUser.getPassword())) {
                throw new BusinessGlobalException("Old password not correct");
            }

            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            connectedUser.setPassword(encodedPassword);
        }
        /*
            email --
            User must confirm new email address to login next time.
         */
        else if (!StringUtils.isEmpty(userDTO.getEmail()) && !StringUtils.isEmpty(userDTO.getPassword())) {

            //Check that password is correct
            if (!passwordEncoder.matches(userDTO.getPassword(), connectedUser.getPassword())) {
                throw new BusinessGlobalException("Password not correct");
            }

            connectedUser.setEmail(userDTO.getEmail());
            connectedUser.setConfirmed(false);
            String confirmationCode = SppotiUtils.generateConfirmationKey();
            connectedUser.setConfirmationCode(confirmationCode);

            sendConfirmationEmail(userDTO, confirmationCode);
        }
        else if(!StringUtils.isEmpty(userDTO.getLanguage())){
            connectedUser.setLanguageEnum(LanguageEnum.valueOf(userDTO.getLanguage()));
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
     *
     * @param userDTO
     */
    @Override
    @Transactional
    public void sendRecoverAccountEmail(SignUpDTO userDTO) {

        Optional<UserEntity> optional = Optional.ofNullable(userRepository.getByEmailAndDeletedFalse(userDTO.getEmail()));

        optional.ifPresent(u -> {
            String code = SppotiUtils.generateConfirmationKey();

            Date tokenExpiryDate = SppotiUtils.generateExpiryDate(daysBeforeExpiration);

            u.setRecoverCodeCreationDate(tokenExpiryDate);
            u.setRecoverCode(code);
            userRepository.save(u);
            LOGGER.info("Recover password email sent tocommit: " + u.getEmail());


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

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void generateNewConfirmationEmail(UserDTO userDTO) {

        Optional<UserEntity> optional = Optional.ofNullable(userRepository.getByEmailAndDeletedFalse(userDTO.getEmail()));

        optional.ifPresent(u -> {
            if (u.isConfirmed()) {
                throw new BusinessGlobalException("Account Already activated");
            }
            //generate new code
            String confirmationCode = SppotiUtils.generateConfirmationKey();
            u.setConfirmationCode(confirmationCode);
            //generate new expiry date
            u.setAccountMaxActivationDate(SppotiUtils.generateExpiryDate(daysBeforeExpiration));
            //update
            userRepository.saveAndFlush(u);
            //send email
            SendEmailToActivateAccount(u);
            LOGGER.info("New activation link has been sent to user: " + userDTO.getEmail());
        });

        optional.orElseThrow(() -> new EntityNotFoundException("Email not associated to an account"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO fillAccountResponse(UserEntity entity) {
        entity.setPassword(null);
        return userTransformer.modelToDto(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO handleFriendShip(String username, Long connectedUserId) {

        UserEntity targetUser = this.getUserByUsername(username);
        if (targetUser == null) {
            throw new EntityNotFoundException("Target user id not found");
        }
        UserEntity connectedUser = this.getUserById(connectedUserId);

        targetUser.setPassword(null);//do not return password
        UserDTO user = userTransformer.modelToDto(targetUser);
        user.setFriendStatus(GlobalAppStatusEnum.PUBLIC_RELATION.getValue());

        if (connectedUser != null) {
            if (!connectedUser.getId().equals(targetUser.getId())) {
                /* manage requests sent to me. */
                FriendShipEntity friendShip;

                friendShip = friendShipRepository.findByFriendUuidAndUserUuidAndDeletedFalse(connectedUser.getUuid(), targetUser.getUuid());

                if (friendShip == null) {
                    friendShip = friendShipRepository.findByFriendUuidAndUserUuidAndDeletedFalse(targetUser.getUuid(), connectedUser.getUuid());
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
                if (!friendShipRepository.findByUserUuidAndFriendUuidAndStatusAndDeletedFalse(targetUser.getUuid(), connectedUser.getUuid(), GlobalAppStatusEnum.PENDING).isEmpty()) {
                    user.setFriendStatus(GlobalAppStatusEnum.PENDING_SENT.getValue());
                }
            }
            user.setMyProfile(connectedUser.getId().equals(targetUser.getId()));
        } else {
            user.setMyProfile(true);
        }

        //Map all user sports
        List<SportDTO> sportDTOs = targetUser.getRelatedSports().stream()
                .map(sportTransformer::modelToDto)
                .collect(Collectors.toList());
        user.setSportDTOs(sportDTOs);

        if (!targetUser.getAddresses().isEmpty()) {
            user.setAddress(targetUser.getAddresses().first().getAddress());
        }

        return user;
    }
}