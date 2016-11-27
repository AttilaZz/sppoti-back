package com.fr.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.fr.controllers.serviceImpl.SignUpServiceImpl;
import com.fr.models.JsonPostRequest;
import com.fr.models.SignUpRequest;
import com.fr.models.UserRoleType;
import com.fr.pojos.Sport;
import com.fr.pojos.UserRoles;
import com.fr.pojos.Users;

/**
 * Created by: Wail DJENANE On June 01, 2016
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/inscription")
public class SignUpController {

	private Logger LOGGER = Logger.getLogger(SignUpController.class);

	@Autowired
	private SignUpServiceImpl signUpService;

	@RequestMapping(value = "/create/user", method = RequestMethod.POST, headers = "content-type=application/x-www-form-urlencoded")
	@ResponseBody
	public ResponseEntity<Void> createUser(@ModelAttribute JsonPostRequest json, UriComponentsBuilder ucBuilder) {

		Gson gson = new Gson();
		SignUpRequest user = null;
		if (json != null) {
			try {
				user = gson.fromJson(json.getJson(), SignUpRequest.class);

				LOGGER.info("SignUp data sent by user: " + new ObjectMapper().writeValueAsString(user));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		} else {
			LOGGER.info("SIGN-UP: Data sent by user are invalid");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Users newUser = new Users();

		/*
		 * processing user Sports
		 */
		Set<Sport> userSports = new HashSet<Sport>();

		if (!signUpService.isReceivedDataNotEmpty(user)) {
			LOGGER.info("INSCRIPTION: Un attribut obligatoire est vide !!");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}

		newUser.setRelatedSports(userSports);

		/*
		 * processing user Profile
		 */
		String profileNameToSet = UserRoleType.USER.getUserProfileType();
		UserRoles profile = signUpService.getProfileEntity(profileNameToSet);

		if (profile == null) {
			LOGGER.info("Profile name <" + profileNameToSet + "> doesn't exist !!");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Set<UserRoles> lrole = new HashSet<UserRoles>();
		lrole.add(profile);
		newUser.setUserProfile(lrole);

		/*
		 * saving the new user
		 */
		if (signUpService.saveNewUser(newUser)) {
			/*
			 * Send confirmation email
			 */

			LOGGER.info("User : " + user.getFirstName() + " " + user.getLastName() + " Has been saved !");
			LOGGER.info("Confirmation code is => " + confirmationCode);

			/*
			 * Send email to confirm account
			 */
			if (signUpService.sendConfirmationEmail(newUser.getEmail(), confirmationCode)) {
				LOGGER.info("Confirmation email has been sent successfuly !");
			} else {
				LOGGER.info("Error sending confirmation :-(");
			}

			/*
			 * newUser is returned in the JSON, so the confirmation code must be
			 * hided for security reasons
			 */
			newUser.setConfirmationCode("");

			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}

		LOGGER.info("Error creating user : " + user.getFirstName() + " " + user.getLastName());
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}

	// verify weather username or email exist or not in the database
	@RequestMapping(value = "/verify", method = RequestMethod.POST, headers = "content-type=application/x-www-form-urlencoded")
	public ResponseEntity<Void> verifyEmailAndUsernameExistance(@ModelAttribute Users user,
			UriComponentsBuilder ucBuilder) {

		boolean isDuplicated = false;
		String email = user.getEmail();
		String username = user.getUsername();

		// must have only one field
		if ((email == null && username == null) || (email != null && username != null)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (email != null) {
			if (email.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else if (!signUpService.isEmailContentValid(email)) {
				isDuplicated = true;
			}
		}
		if (username != null) {
			if (username.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} else if (!signUpService.isUsernameValid(username)) {
				isDuplicated = true;
			}
		}

		if (!isDuplicated) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}

	@RequestMapping(value = "/allSports", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Sport>> getAllSports() {

		List<Sport> allSports = signUpService.getAllSports();

		return new ResponseEntity<List<Sport>>(allSports, HttpStatus.OK);

	}

	@RequestMapping(value = "/create/user/{code}", method = RequestMethod.GET)
	public ResponseEntity<Void> confirmUserEmail(@PathVariable("code") String code) {

		if (code == null || code.isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}

		// if given code exist in database confirm registration
		if (signUpService.tryActivateAccount(code)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}

		// code is not valid
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);

	}
}
