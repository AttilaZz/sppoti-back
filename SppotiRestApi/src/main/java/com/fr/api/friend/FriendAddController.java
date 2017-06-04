package com.fr.api.friend;

import com.fr.commons.dto.UserDTO;
import com.fr.service.FriendControllerService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/11/17.
 */

@RestController
@RequestMapping("/friend")
@ApiVersion("1")
class FriendAddController
{
	
	/** Friend service. */
	private FriendControllerService friendControllerService;
	
	/** Init service. */
	@Autowired
	void setFriendControllerService(final FriendControllerService friendControllerService)
	{
		this.friendControllerService = friendControllerService;
	}
	
	/**
	 * @param user
	 * 		friend to add.
	 *
	 * @return created friend.
	 */
	@PostMapping
	ResponseEntity<Object> addFriend(@RequestBody final UserDTO user)
	{
		
		//Check received data
		if (user == null || user.getFriendUuid() == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		this.friendControllerService.saveFriendShip(user);
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
}
