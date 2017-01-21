package com.fr.controllers;

import com.fr.controllers.service.AccountControllerService;
import com.fr.entities.*;
import com.fr.enums.CoverType;
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

    private AccountControllerService accountService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setAccountService(AccountControllerService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/create")
    @ResponseBody
    public void createUser(@RequestBody SignUpRequest user, HttpServletResponse response) {

        Users newUser = new Users();

		/*
         * processing user Sports
		 */
        Set<Sport> userSports = new HashSet<>();

        if (!accountService.isReceivedDataNotEmpty(user)) {
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
            // if the parsed SportModel exist in database == correct request
            Sport mSport = accountService.getSportById(sportId);
            if (mSport != null) {
                userSports.add(mSport);
            } else {
                LOGGER.info("INSCRIPTION: le nom de SportModel envoyé n'est pas reconnu");
                response.setStatus(400);
                return;
            }
        }

        newUser.setRelatedSports(userSports);

		/*
         * processing user Profile
		 */
        String profileNameToSet = UserRoleType.USER.getUserProfileType();
        Roles profile = accountService.getProfileEntity(profileNameToSet);

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
            accountService.saveNewUser(newUser);
            /*
             * Send confirmation email
			 */

            LOGGER.info("User : " + user.getFirstName() + " " + user.getLastName() + " Has been saved !");
            LOGGER.info("Confirmation code is => " + confirmationCode);

			/*
             * Send email to confirm account
			 */
            Thread thread = new Thread(() -> {
                accountService.sendConfirmationEmail(newUser.getEmail(), confirmationCode);
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
        if (accountService.tryActivateAccount(code)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        // code is not valid
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<User> editUserInfo(@RequestBody User user, HttpServletRequest request) {

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users connected_user = accountService.getUserById(userId);

        //detect which element uwer want to update
        Resources resource = new Resources();

        boolean update = false;

        if ((user.getAvatar() != null && !user.getAvatar().isEmpty()) || (user.getCover() != null && !user.getCover().isEmpty() && user.getCoverType() != 0)) {

            resource.setSelected(true);

            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                resource.setUrl(user.getAvatar());
                resource.setType(1);
                resource.setTypeExtension(1);
                accountService.unSelectOldResource(userId, 1);
                update = true;
            } else if (user.getCover() != null && !user.getCover().isEmpty() && (CoverType.IMAGE.type() == user.getCoverType() || CoverType.VIDEO.type() == user.getCoverType())) {
                resource.setUrl(user.getCover());
                resource.setType(2);
                resource.setTypeExtension(user.getCoverType());
                accountService.unSelectOldResource(userId, 2);
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
            if (accountService.updateUser(connected_user)) {
                LOGGER.info("USER-UPDATE: User has been updated!");
                return new ResponseEntity<>(user, HttpStatus.OK);

            } else {
                LOGGER.error("USER-UPDATE: ERROR updating user");
                return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
            }
        } else {
            LOGGER.error("USER-UPDATE: Nothing to update OR missigin parameter");
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }

    }


    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<User> connectedUserInfo(Authentication authentication) {

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
        Users targetUser = accountService.getUserById(accountUserDetails.getId());


        return new ResponseEntity<>(accountService.fillUserResponse(targetUser, null), HttpStatus.OK);

    }


    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/other/{username}/**")
    public ResponseEntity<User> otherUserInfo(@PathVariable("username") String username, HttpServletRequest request) {

//        String path = (String) httpServletRequest.getAttribute(
//                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
//        AntPathMatcher apm = new AntPathMatcher();
//        String bestMatchPattern = (String ) httpServletRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
//        String finalPath = apm.extractPathWithinPattern(bestMatchPattern, path);

        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users connected_user = accountService.getUserById(userId);

        Users targetUser = accountService.getUserByUsername(username);

        if (targetUser == null) {
            LOGGER.error("ACCOUNT-OTHER: Username not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>(accountService.fillUserResponse(targetUser, connected_user), HttpStatus.OK);

    }
}