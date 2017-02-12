package com.fr.core;

import com.fr.rest.service.AccountControllerService;
import com.fr.entities.*;
import com.fr.exceptions.ConflictEmailException;
import com.fr.exceptions.ConflictUsernameException;
import com.fr.commons.dto.SignUpRequestDTO;
import com.fr.commons.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

/**
 * Created by: Wail DJENANE On June 01, 2016
 */

@Component
public class AccountControllerServiceImpl extends AbstractControllerServiceImpl implements AccountControllerService {

    @Value("${key.originBack}")
    private String rootAddress;

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

//                Friend friend = new Friend(u);
//                friendRepository.save(friend);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Roles getProfileEntity(String profileType) {
        return roleRepository.getByName(profileType);
    }

    @Override
    public Sport getSportById(Long id) {
        return sportRepository.getById(id);
    }

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
            e.printStackTrace();
            return false;
        }

    }

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

    @Override
    public boolean updateUser(UserEntity connected_user) {

        userRepository.save(connected_user);
        return true;
    }

    @Override
    public void unSelectOldResource(Long userId, int i) {
        Resources resources = resourceRepository.getByUserIdAndTypeAndIsSelectedTrue(userId, i);
        if (resources != null) {
            resources.setSelected(false);
            resourceRepository.save(resources);
        }
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public UserDTO fillUserResponse(UserEntity targetUser, UserEntity connected_user) {
        return super.fillUserResponse(targetUser, connected_user);
    }
}