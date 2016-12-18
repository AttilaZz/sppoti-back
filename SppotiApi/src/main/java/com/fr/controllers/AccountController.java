package com.fr.controllers;

import com.fr.controllers.serviceImpl.SignUpServiceImpl;
import com.fr.entities.*;
import com.fr.exceptions.ConflictEmailException;
import com.fr.exceptions.ConflictPhoneException;
import com.fr.exceptions.ConflictUsernameException;
import com.fr.models.SignUpRequest;
import com.fr.models.User;
import com.fr.models.UserRoleType;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
public class AccountController {

    private Logger LOGGER = Logger.getLogger(AccountController.class);

    @Autowired
    private SignUpServiceImpl signUpService;

    @PostMapping(value = "/create")
    @ResponseBody
    public void createUser(@RequestBody SignUpRequest user, HttpServletResponse response) {

        Users newUser = new Users();

		/*
         * processing user Sports
		 */
        Set<Sport> userSports = new HashSet<>();

        if (!signUpService.isReceivedDataNotEmpty(user)) {
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

        newUser.setPassword(user.getPassword());
        String uName = user.getUsername();
        uName = uName.trim().toLowerCase();
        newUser.setUsername(uName);

        for (Long sportId : user.getSportId()) {
            // if the parsed sport exist in database == correct request
            Sport mSport = signUpService.getSportById(sportId);
            if (mSport != null) {
                userSports.add(mSport);
            } else {
                LOGGER.info("INSCRIPTION: le nom de sport envoyé n'est pas reconnu");
                response.setStatus(400);
                return;
            }
        }

        newUser.setRelatedSports(userSports);

		/*
         * processing user Profile
		 */
        String profileNameToSet = UserRoleType.USER.getUserProfileType();
        Roles profile = signUpService.getProfileEntity(profileNameToSet);

        if (profile == null) {
            LOGGER.info("Profile name <" + profileNameToSet + "> doesn't exist !!");
            response.setStatus(400);
            return;
        }

        Set<Roles> roles = new HashSet<>();
        roles.add(profile);
        newUser.setUserRoles(roles);

		/*
         * saving the new user
		 */
        try {
            signUpService.saveNewUser(newUser);
            /*
             * Send confirmation email
			 */

            LOGGER.info("User : " + user.getFirstName() + " " + user.getLastName() + " Has been saved !");
            LOGGER.info("Confirmation code is => " + confirmationCode);

			/*
             * Send email to confirm account
			 */
//            Thread thread = new Thread(() -> {
//                signUpService.sendConfirmationEmail(newUser.getEmail(), confirmationCode);
//                LOGGER.info("Confirmation email has been sent successfully !");
//            });
//            thread.start();

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
        if (signUpService.tryActivateAccount(code)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        // code is not valid
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<User> connectedUserInfo(Authentication authentication) {

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        Users connectedUser = accountUserDetails.getConnectedUserDetails();

        User user = new User();
        user.setLastName(connectedUser.getLastName());
        user.setFirstname(connectedUser.getFirstName());
        user.setUsername(connectedUser.getUsername());

        try{
            user.setAddress(connectedUser.getAddresses().first().getAddress());
        }catch (Exception e){
            LOGGER.warn("User has no address yet !");
        }

        return new ResponseEntity<>(user, HttpStatus.OK);

    }

}