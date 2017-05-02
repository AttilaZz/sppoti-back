package com.fr.api.friend;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.entities.FriendShipEntity;
import com.fr.entities.UserEntity;
import com.fr.security.AccountUserDetails;
import com.fr.service.FriendControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/11/17.
 */

@RestController
@RequestMapping("/friend")
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
	 * @param authentication
	 * 		spring auth.
	 *
	 * @return created friend.
	 */
	@PostMapping
	ResponseEntity<Object> addFriend(@RequestBody final UserDTO user, final Authentication authentication)
	{

        /*
		Check received data
         */
		if (user == null || user.getFriendUuid() == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

        /*
		Get connected user id
         */
		final Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		
        /*
		Prepare friendShip
         */
		final UserEntity connectedUser = this.friendControllerService.getUserById(userId);
		final UserEntity friend = this.friendControllerService.getUserByUuId(user.getFriendUuid());

        /*
		Check if the friend id refers to an existing user account
         */
		if (friend == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

        /*
		Friend with my self
         */
		if (connectedUser.equals(friend)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

        /*
		Check if friendship exist
         */
		final FriendShipEntity tempFriendShip = this.friendControllerService
				.getByFriendUuidAndUser(friend.getUuid(), connectedUser.getUuid());
		if (tempFriendShip != null && !tempFriendShip.getStatus().equals(GlobalAppStatusEnum.PUBLIC_RELATION)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		FriendShipEntity friendShip = new FriendShipEntity();
		
		//friendship aleady exist in a different status
		if (tempFriendShip != null && tempFriendShip.getStatus().equals(GlobalAppStatusEnum.PUBLIC_RELATION)) {
			friendShip = tempFriendShip;
			friendShip.setStatus(GlobalAppStatusEnum.PENDING);
		} else {
			final UserEntity u = this.friendControllerService.getUserByUuId(user.getFriendUuid());
			friendShip.setFriend(u);
			friendShip.setUser(connectedUser);
		}

        /*
		Prepare friendship for saving
         */
		this.friendControllerService.saveFriendShip(friendShip);
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
}
