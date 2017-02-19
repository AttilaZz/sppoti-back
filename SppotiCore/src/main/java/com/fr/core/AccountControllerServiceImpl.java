package com.fr.core;

import com.fr.commons.dto.SignUpRequestDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.ResourcesEntity;
import com.fr.entities.RoleEntity;
import com.fr.entities.SportEntity;
import com.fr.entities.UserEntity;
import com.fr.exceptions.ConflictEmailException;
import com.fr.exceptions.ConflictUsernameException;
import com.fr.rest.service.AccountControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

/**
 * Created by: Wail DJENANE On June 01, 2016
 */

@Component
public class AccountControllerServiceImpl extends AbstractControllerServiceImpl implements AccountControllerService {

    private Logger LOGGER = Logger.getLogger(AccountControllerServiceImpl.class);

    @Value("${key.originBack}")
    private String rootAddress;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void saveNewUser(UserEntity user) throws Exception {

        if (userRepository.getByEmail(user.getEmail()) != null) {
            throw new ConflictEmailException("Email already exists");
        }
//        else if (userRepository.getByTelephone(user.getTelephone()) != null) {
//            throw new ConflictPhoneException("Phone already exists");
//        }
        else if (userRepository.getByUsername(user.getUsername()) != null) {
            throw new ConflictUsernameException("Username already exists");
        } else {
            try {

                UserEntity u = userRepository.save(user);
                LOGGER.info("Account has been created: " + u);

            } catch (Exception e) {
                LOGGER.error("Create account error", e);
            }
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
    @Override
    public SportEntity getSportById(Long id) {
        return sportRepository.getById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isReceivedDataNotEmpty(SignUpRequestDTO user) {

        if (user.getFirstName() != null && !user.getFirstName().isEmpty() && user.getLastName() != null
                && !user.getLastName().isEmpty() && user.getEmail() != null && !user.getEmail().isEmpty()
                && user.getUsername() != null && !user.getUsername().isEmpty() && user.getPassword() != null
                && !user.getPassword().isEmpty() && user.getSexe() != null && !user.getSexe().isEmpty()
                && user.getSportId() != null && user.getSportId().length > 0 && user.getDateBorn() != null)
            return true;

        return false;
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
     * {@inheritDoc}
     */
    @Override
    public boolean sendConfirmationEmail(String email, String code) {

        // String rootAddress =
        // globalAddressConfigProperties().getProperty("originBack");
        String link = rootAddress + "SppotiWebAppGui/inscription/create/user/" + code;
        String body = "<H2>Account confirmation</H2><br/><br/>"
                + "<p>Please Click the link or copy/paste in the browser to corfirm your account</p><br/>" + "<p>" + link
                + "</p>";
        // Send a composed mail
        try {

            mailer.sendMail2(email, "Account Confirmation", body);

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

        return true;
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