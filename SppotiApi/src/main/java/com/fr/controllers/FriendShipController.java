/**
 * 
 */
package com.fr.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.fr.controllers.service.FriendControllerService;
import com.fr.models.FriendRequest;
import com.fr.models.JsonPostRequest;
import com.fr.entities.FriendShip;
import com.fr.entities.Users;

/**
 * Created by: Wail DJENANE on Jul 3, 2016
 */

@RestController
@RequestMapping("/friend")
public class FriendShipController {

	@Autowired
	private FriendControllerService friendDataService;

	private Logger LOGGER = Logger.getLogger(FriendShipController.class);

	private static final String ATT_USER_ID = "USER_ID";

	@RequestMapping(value = "/add", method = RequestMethod.POST, headers = "content-type=application/x-www-form-urlencoded")
	// @RequestMapping(value = "/add", method = RequestMethod.POST, consumes =
	// MediaType.APPLICATION_JSON_VALUE, produces =
	// MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> addPost(@ModelAttribute JsonPostRequest json, HttpServletRequest request,
			UriComponentsBuilder ucBuilder) {

		// get current logged user
		Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
		Users user = friendDataService.getUserById(userId);
		LOGGER.info("LOGGED User: => " + userId);

		Gson gson = new Gson();
		FriendRequest newfriend = null;
		if (json != null) {
			try {
				newfriend = gson.fromJson(json.getJson(), FriendRequest.class);
				LOGGER.info("FRIEND-ADD: data sent by user: " + new ObjectMapper().writeValueAsString(newfriend));
			} catch (JsonProcessingException e) {

				e.printStackTrace();
			}
		} else {
			LOGGER.info("FRIEND-ADD: Data sent by user are invalid");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		/*
		 * process request data params
		 */
		if (newfriend.getFriendId() == null && newfriend.getFriendUsername() == null) {
			LOGGER.info("FRIEND-ADD: No friend found in the request");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		/*
		 * Get DATA from request json
		 */
		Long friendId;
		String friendUsername;
		Users friendUser;
		if (newfriend.getFriendId() != null) {
			friendId = newfriend.getFriendId();
			friendUser = friendDataService.getUserById(friendId);
		} else {
			friendUsername = newfriend.getFriendUsername();
			friendUser = friendDataService.getFrindfromUsername(friendUsername);
		}

		/*
		 * Friend with ur self, realy !!
		 */
		if (userId == friendUser.getId()) {
			LOGGER.info("FRIEND-ADD: You can't be friend of your self body :-p");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		/*
		 * check if the friend account is activated to avoid any problem
		 */

		if (friendUser != null && !friendUser.isConfirmed()) {
			LOGGER.info("FRIEND-ADD: This user (Friend) is not confirmed yet !!");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		/*
		 * check if friendship already exist !!
		 */
		if (friendDataService.getConfirmedFriendShip(userId, friendUser.getId()) != null) {
			LOGGER.info("FRIEND-ADD: Friend ship already exist ! should be secured from front.");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (friendDataService.getPendingFriendShip(userId, friendUser.getId()) != null) {
			LOGGER.info("FRIEND-ADD: Friend ship Pending! User must wait for response or cancel it!.");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		/*
		 * Prepare friend Object to save
		 */
		FriendShip f = new FriendShip();
		f.setUser(user);
		f.setFrienId(friendUser);
		f.setPending(true);

		if (friendDataService.saveFriend(f)) {
			LOGGER.info("FRIEND-ADD: Friend has been added");
			return new ResponseEntity<>(HttpStatus.OK);
		}

		/*
		 * for any other problem !
		 */
		LOGGER.info("FRIEND-ADD: should be checked again for an anknown problem !");
		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);

	}

	@RequestMapping(value = "/confirm/{friendShipId}", method = RequestMethod.GET)
	public ResponseEntity<Void> updateUser(@PathVariable("friendShipId") Long friendShipId) {

		FriendShip f = friendDataService.getfriendShipById(friendShipId);

		if (f == null) {
			LOGGER.info("FRIEND-CONFIRM: Parameter id is not valid");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		f.setConfirmed(true);
		f.setPending(false);

		if (friendDataService.updateFriend(f)) {
			LOGGER.info("FRIEND-CONFIRM: FridnShip has been confirmed");
			return new ResponseEntity<>(HttpStatus.OK);
		}

		LOGGER.info("FRIEND-CONFIRM: Problem when updating into database");
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);

	}

	@RequestMapping(value = "/delete/{friendId}", method = RequestMethod.GET)
	public ResponseEntity<Void> deleteUser(@PathVariable("friendId") Long friendId, HttpServletRequest request) {

		// check if the id refers to a user
		// check if this user is a friend with the connected user
		// delete friendship
		Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);

		Users friend = friendDataService.getUserById(friendId);

		if (friend == null) {
			LOGGER.info("FRIEND-DELETE: The intered id doesn't refer to an existing user !");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		FriendShip fr = friendDataService.getFriendShip(userId, friendId);

		if (fr == null) {
			LOGGER.info("FRIEND-DELETE: Failed to retreive the friendShip with the given ID");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (friendDataService.deleteFriendShip(fr)) {
			LOGGER.info("FRIEND-DELETE: FriendShip between user:" + userId + " and friendId " + friendId
					+ " has been deleted !");
			return new ResponseEntity<>(HttpStatus.OK);
		}

		LOGGER.info("FRIEND-DELETE: Unknown problem when deleting from database !!");
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
}
