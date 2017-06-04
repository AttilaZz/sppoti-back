package com.fr.api.friend;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.service.FriendControllerService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 12/26/16.
 * <p>
 * This controller is used to accept or refuse friend request
 */

@RestController
@RequestMapping("/friend")
@ApiVersion("1")
class FriendUpdateStatusController
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
	 * 		friend id.
	 * @param authentication
	 * 		spring auth.
	 *
	 * @return 200 http status if updated, 400 otherwise
	 */
	@PutMapping
	ResponseEntity updateFriend(@RequestBody final UserDTO user, final Authentication authentication)
	{
		
		final Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		
		if (user == null || user.getFriendUuid() == 0) {
			throw new BusinessGlobalException("Friend id must not be null or O");
		}
		
		this.friendControllerService.updateFriendShip(userId, user.getFriendUuid(), user.getFriendStatus());
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
}