package com.fr.api.friend;

import com.fr.service.FriendBusinessService;
import com.fr.versionning.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/11/17.
 */

@RestController
@RequestMapping("/friend")
@ApiVersion("1")
class FriendDeleteController
{
	
	private final Logger LOGGER = LoggerFactory.getLogger(FriendDeleteController.class);
	
	private FriendBusinessService friendControllerService;
	
	@Autowired
	void setFriendControllerService(final FriendBusinessService friendControllerService)
	{
		this.friendControllerService = friendControllerService;
	}
	
	/**
	 * @param friendId
	 * 		friend id.
	 *
	 * @return 200 http status if friendship deleted, 400 status otherwise
	 */
	@DeleteMapping("/{friend_id}")
	ResponseEntity deleteFriend(@PathVariable("friend_id") final String friendId)
	{
		this.LOGGER.info("Request sent to delete a friendship");
		
		this.friendControllerService.deleteFriendShip(friendId);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
