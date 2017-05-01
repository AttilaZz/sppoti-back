package com.fr.api.friend;

import com.fr.entities.FriendShipEntity;
import com.fr.entities.UserEntity;
import com.fr.security.AccountUserDetails;
import com.fr.service.FriendControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/11/17.
 */

@RestController
@RequestMapping("/friend")
class FriendDeleteController
{
	
	/** Friend service. */
	private FriendControllerService friendControllerService;
	
	/** Init friend service. */
	@Autowired
	void setFriendControllerService(final FriendControllerService friendControllerService)
	{
		this.friendControllerService = friendControllerService;
	}
	
	/**
	 * @param friendId
	 * 		friend id.
	 * @param authentication
	 * 		spring auth.
	 *
	 * @return 200 http status if friendship deleted, 400 status otherwise
	 */
	@DeleteMapping("/{friend_id}")
	ResponseEntity deleteFriend(@PathVariable("friend_id") final int friendId, final Authentication authentication)
	{
		
		final Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		final UserEntity connectedUser = this.friendControllerService.getUserById(userId);

        /*
		Check if friendship exist
         */
		final FriendShipEntity friendShip = this.friendControllerService
				.findFriendShip(friendId, connectedUser.getUuid());
		if (friendShip == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		this.friendControllerService.deleteFriendShip(friendShip);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
