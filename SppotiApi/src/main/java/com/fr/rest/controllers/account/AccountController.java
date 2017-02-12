package com.fr.rest.controllers.account;

import com.fr.rest.service.AccountControllerService;
import com.fr.entities.*;
import com.fr.enums.CoverType;
import com.fr.exceptions.ConflictEmailException;
import com.fr.exceptions.ConflictPhoneException;
import com.fr.exceptions.ConflictUsernameException;
import com.fr.commons.dto.SignUpRequestDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.models.UserRoleType;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * Created by: Wail DJENANE On June 01, 2016
 */
@RestController
@RequestMapping(value = "/account")
public class AccountController {

    private Logger LOGGER = Logger.getLogger(AccountController.class);
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
        Set<Sport> userSports = new HashSet<>();

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
            Sport mSport = accountControllerService.getSportById(sportId);
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
        Roles profile = accountControllerService.getProfileEntity(profileNameToSet);

        if (profile == null) {
            LOGGER.info("Profile name <" + profileNameToSet + "> doesn't exist !!");
            response.setStatus(400);
            return;
        }

        Set<Roles> roles = new HashSet<>();
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

    @PutMapping(value = "/validate/{code}")
    public ResponseEntity<Void> confirmUserEmail(@PathVariable("code") String code) {

        if (code == null || code.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // if given code exist in database confirm registration
        if (accountControllerService.tryActivateAccount(code)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        // code is not valid
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<UserDTO> editUserInfo(@RequestBody UserDTO user, HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        UserEntity connected_user = accountControllerService.getUserById(userId);

        //detect which element uwer want to update
        Resources resource = new Resources();

        boolean update = false;

        if ((user.getAvatar() != null && !user.getAvatar().isEmpty()) || (user.getCover() != null && !user.getCover().isEmpty() && user.getCoverType() != 0)) {

            resource.setSelected(true);

            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                resource.setUrl(user.getAvatar());
                resource.setType(1);
                resource.setTypeExtension(1);
                accountControllerService.unSelectOldResource(userId, 1);
                update = true;
            } else if (user.getCover() != null && !user.getCover().isEmpty() && (CoverType.IMAGE.type() == user.getCoverType() || CoverType.VIDEO.type() == user.getCoverType())) {
                resource.setUrl(user.getCover());
                resource.setType(2);
                resource.setTypeExtension(user.getCoverType());
                accountControllerService.unSelectOldResource(userId, 2);
                update = true;
            }

            resource.setUser(connected_user);
            connected_user.getRessources().add(resource);

        } else {
            if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
                connected_user.setFirstName(user.getFirstName());
                update = true;
            }
            if (user.getLastName() != null && !user.getLastName().isEmpty()) {
                connected_user.setLastName(user.getLastName());
                update = true;
            }
            if (user.getAddress() != null && !user.getAddress().isEmpty()) {
                connected_user.getAddresses().add(new Address(user.getAddress()));
                update = true;
            }
            if (user.getUsername() != null && !user.getUsername().isEmpty()) {
                connected_user.setUsername(user.getUsername());
                update = true;
            }
            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                connected_user.setTelephone(user.getPhone());
                update = true;
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                connected_user.setPassword(user.getPassword());
                update = true;
            }
            //TODO: Update sports
        }

        if (update) {

            try {
                accountControllerService.updateUser(connected_user);
                LOGGER.info("USER-UPDATE: UserDTO has been updated!");
                return new ResponseEntity<>(user, HttpStatus.OK);

            } catch (RuntimeException e) {
                LOGGER.error("USER-UPDATE: ERROR updating user: " + e);
                return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);

            }

        } else {
            LOGGER.error("USER-UPDATE: Nothing to update OR missigin parameter");
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }

    }

}