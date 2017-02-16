package com.fr.rest.controllers.account;

import com.fr.commons.dto.SignUpRequestDTO;
import com.fr.entities.RoleEntity;
import com.fr.entities.SportEntity;
import com.fr.entities.UserEntity;
import com.fr.exceptions.ConflictEmailException;
import com.fr.exceptions.ConflictPhoneException;
import com.fr.exceptions.ConflictUsernameException;
import com.fr.models.UserRoleType;
import com.fr.rest.service.AccountControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * Created by: Wail DJENANE On June 01, 2016
 */
@RestController
@RequestMapping(value = "/account")
public class AccountAddController {

    private Logger LOGGER = Logger.getLogger(AccountAddController.class);
    private static final String ATT_USER_ID = "USER_ID";

    private AccountControllerService accountControllerService;

    @Autowired
    public void setAccountControllerService(AccountControllerService accountControllerService) {
        this.accountControllerService = accountControllerService;
    }

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/create")
    @ResponseBody
    public void createUser(@RequestBody SignUpRequestDTO user, HttpServletResponse response) {

        UserEntity newUser = new UserEntity();

		/*
         * processing user Sports
		 */
        Set<SportEntity> userSports = new HashSet<>();

        if (!accountControllerService.isReceivedDataNotEmpty(user)) {
            LOGGER.info("INSCRIPTION: Un attribut obligatoire est vide !!");
            response.setStatus(400);
            return;
        }

        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setDateBorn(user.getDateBorn());
        newUser.setSexe(user.getSexe());
        newUser.setEmail(user.getEmail());

        String confirmationCode = UUID.randomUUID().toString();
        newUser.setConfirmationCode(confirmationCode);

        //newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setPassword(user.getPassword());

        String uName = user.getUsername().trim();
        //uName = uName.toLowerCase();
        newUser.setUsername(uName);

        for (Long sportId : user.getSportId()) {
            // if the parsed SportModelDTO exist in database == correct request
            SportEntity mSport = accountControllerService.getSportById(sportId);
            if (mSport != null) {
                userSports.add(mSport);
            } else {
                LOGGER.info("INSCRIPTION: le nom de SportModelDTO envoyé n'est pas reconnu");
                response.setStatus(400);
                return;
            }
        }

        newUser.setRelatedSports(userSports);

		/*
         * processing user Profile
		 */
        String profileNameToSet = UserRoleType.USER.getUserProfileType();
        RoleEntity profile = accountControllerService.getProfileEntity(profileNameToSet);

        if (profile == null) {
            LOGGER.info("Profile name <" + profileNameToSet + "> doesn't exist !!");
            response.setStatus(400);
            return;
        }

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(profile);
        newUser.setRoles(roles);

		/*
         * saving the new user
		 */
        try {
            accountControllerService.saveNewUser(newUser);
            /*
             * Send confirmation email
			 */

            LOGGER.info("UserDTO : " + user.getFirstName() + " " + user.getLastName() + " Has been saved !");
            LOGGER.info("Confirmation code is => " + confirmationCode);

			/*
             * Send email to confirm account
			 */
            Thread thread = new Thread(() -> {
                accountControllerService.sendConfirmationEmail(newUser.getEmail(), confirmationCode);
                LOGGER.info("Confirmation email has been sent successfully !");
            });
            thread.start();

			/*
             * newUser is returned in the JSON, so the confirmation code must be
			 * hided for security reasons
			 */
            newUser.setConfirmationCode("");

            response.setStatus(201);
            return;
        } catch (Exception e) {
            if (e instanceof ConflictEmailException) {
                LOGGER.info("Error creating user : " + user.getFirstName() + " " + user.getLastName() + " Email already exist!");
                response.setStatus(409);
                return;
            } else if (e instanceof ConflictPhoneException) {
                LOGGER.info("Error creating user : " + user.getFirstName() + " " + user.getLastName() + " Phone already exist!");
                response.setStatus(410);
                return;
            } else if (e instanceof ConflictUsernameException) {
                LOGGER.info("Error creating user : " + user.getFirstName() + " " + user.getLastName() + " Username already exist!");
                response.setStatus(411);
                return;
            }
        }

    }

}