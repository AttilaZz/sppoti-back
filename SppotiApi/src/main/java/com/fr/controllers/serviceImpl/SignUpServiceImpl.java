package com.fr.controllers.serviceImpl;

import com.fr.controllers.service.SignUpService;
import com.fr.entities.*;
import com.fr.exceptions.ConflictEmailException;
import com.fr.exceptions.ConflictUsernameException;
import com.fr.models.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

/**
 * Created by: Wail DJENANE On June 01, 2016
 */

@Component
public class SignUpServiceImpl extends AbstractControllerServiceImpl implements SignUpService {

    @Value("${key.originBack}")
    private String rootAddress;

    @Override
    public void saveNewUser(Users user) throws Exception {

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
                Users u = userRepository.save(user);
                Thread.sleep(2000);
                friendRepository.save(new Friend(u));

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
    public boolean isReceivedDataNotEmpty(SignUpRequest user) {

        if (user.getFirstName() != null && !user.getFirstName().isEmpty() && user.getLastName() != null
                && !user.getLastName().isEmpty() && user.getEmail() != null && !user.getEmail().isEmpty()
                && user.getUsername() != null && !user.getUsername().isEmpty() && user.getPassword() != null
                && !user.getPassword().isEmpty() && user.getSexe() != null && !user.getSexe().isEmpty()
                && user.getSportId() != null && user.getSportId().length > 0 && user.getDateBorn() != null
                && !user.getDateBorn().isEmpty())
            return true;

        return false;
    }

    @Override
    public boolean tryActivateAccount(String code) {

        Users users = userRepository.getByConfirmationCode(code);

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
    public Users getUserById(int id){
        return userRepository.getByUuid(id);
    }

    @Override
    public boolean updateUser(Users connected_user) {
        try {
            userRepository.save(connected_user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
