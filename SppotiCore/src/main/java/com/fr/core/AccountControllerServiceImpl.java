package com.fr.core;

import com.fr.commons.dto.SignUpRequestDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.ResourcesEntity;
import com.fr.entities.RoleEntity;
import com.fr.entities.UserEntity;
import com.fr.exceptions.ConflictEmailException;
import com.fr.exceptions.ConflictUsernameException;
import com.fr.models.UserRoleType;
import com.fr.rest.service.AccountControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by: Wail DJENANE On June 01, 2016
 */

@Component
public class AccountControllerServiceImpl extends AbstractControllerServiceImpl implements AccountControllerService {

    private Logger LOGGER = Logger.getLogger(AccountControllerServiceImpl.class);

    @Value("${key.originBack}")
    private String rootAddress;


    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     *
     * @param user
     */
    @Transactional
    @Override
    public void saveNewUser(SignUpRequestDTO user) {

        UserEntity newUser = new UserEntity();

        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setDateBorn(user.getDateBorn());
        newUser.setSexe(user.getSexe());
        newUser.setEmail(user.getEmail());

        String confirmationCode = UUID.randomUUID().toString();
        newUser.setConfirmationCode(confirmationCode);

        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setPassword(user.getPassword());

        String uName = user.getUsername().trim();
        newUser.setUsername(uName);

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
        RoleEntity profile = roleRepository.getByName(UserRoleType.USER.name());

        if (profile == null) {
            throw new EntityNotFoundException("Profile name <" + UserRoleType.USER.name() + "> not found !!");
        }

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(profile);
        newUser.setRoles(roles);

        if (userRepository.getByEmail(user.getEmail()) != null) {
            throw new ConflictEmailException("Email already exists");
        }
//        else if (userRepository.getByTelephone(user.getTelephone()) != null) {
//            throw new ConflictPhoneException("Phone already exists");
//        }
        else if (userRepository.getByUsername(user.getUsername()) != null) {
            throw new ConflictUsernameException("Username already exists");
        } else {
            UserEntity u = userRepository.save(newUser);
            LOGGER.info("Account has been created: " + u);

            /*
             * Send email to confirm account
			 */
            Thread thread = new Thread(() -> {
                this.sendConfirmationEmail(newUser.getEmail(), confirmationCode);
                LOGGER.info("Confirmation email has been sent successfully !");
            });
            thread.start();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleEntity getProfileEntity(String profileType) {
        return roleRepository.getByName(profileType);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public boolean tryActivateAccount(String code) {

        UserEntity users = userRepository.getByConfirmationCode(code);

        if (users.isConfirmed()) {
            return false;
        }

        try {

            users.setConfirmed(true);
            userRepository.save(users);
            return true;

        } catch (Exception e) {
            LOGGER.error("Account activation error", e);
            return false;
        }

    }

    /**
     * Send account email activation code.
     *
     * @param email user email.
     * @param code account confirmation code.
     */
    private void sendConfirmationEmail(String email, String code) {

        // String rootAddress =
        // globalAddressConfigProperties().getProperty("originBack");
        String link = rootAddress + "SppotiWebAppGui/inscription/create/user/" + code;
        String body = "<H2>Account confirmation</H2><br/><br/>"
                + "<p>Please Click the link or copy/paste in the browser to corfirm your account</p><br/>" + "<p>" + link
                + "</p>";

        try {
            mailer.sendMail2(email, "Account Confirmation", body);
        } catch (MessagingException e) {
            LOGGER.error("Could not send account confirmation email to user: " + email, e);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public boolean updateUser(UserEntity connected_user) {

        userRepository.save(connected_user);
        return true;
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
        return userRepository.getByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO fillUserResponse(UserEntity targetUser, UserEntity connectedUser) {
        return super.fillUserResponse(targetUser, connectedUser);
    }
}