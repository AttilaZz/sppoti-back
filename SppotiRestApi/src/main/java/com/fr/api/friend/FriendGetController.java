package com.fr.api.friend;

import com.fr.commons.dto.FriendResponseDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.service.FriendBusinessService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by djenanewail on 2/11/17.
 */

@RestController
@RequestMapping("/friend")
@ApiVersion("1")
class FriendGetController
{
	
	/** Friend service. */
	private final FriendBusinessService friendControllerService;
	
	/** Init services. */
	@Autowired
	FriendGetController(final FriendBusinessService friendControllerService)
	{
		this.friendControllerService = friendControllerService;
	}
	
	/**
	 * @param userId
	 * 		connected user id.
	 * @param page
	 * 		page number.
	 *
	 * @return confirmed friend list.
	 */
	@GetMapping("/confirmed/{userId}/{page}")
	ResponseEntity<List<UserDTO>> getConfirmedFriendList(@PathVariable final String userId, @PathVariable final int page)
	{
		
		final List<UserDTO> friendList = this.friendControllerService.getConfirmedFriendList(userId, page);
		
		return new ResponseEntity<>(friendList, HttpStatus.OK);
		
	}
	
	/**
	 * @param page
	 * 		page number.
	 *
	 * @return all refused friend requests.
	 */
	@GetMapping("/refused/{page}")
	ResponseEntity<FriendResponseDTO> getRefusedFriendList(@PathVariable final int page)
	{
		final FriendResponseDTO friendResponse = this.friendControllerService.getRefusedFriendList(page);
		
		return new ResponseEntity<>(friendResponse, HttpStatus.OK);
		
	}
	
	
	/**
	 * @param page
	 * 		page number.
	 *
	 * @return all pending requests.
	 */
	@GetMapping("/pending/sent/{page}")
	ResponseEntity<FriendResponseDTO> getSentPendingFriendList(@PathVariable final int page)
	{
		
		final FriendResponseDTO friendResponse = this.friendControllerService.getAllSentPendingFriendList(page);
		
		return new ResponseEntity<>(friendResponse, HttpStatus.OK);
		
	}
	
	/**
	 * @param page
	 * 		page number.
	 *
	 * @return all friend pending requests.
	 */
	@GetMapping("/pending/received/{page}")
	ResponseEntity<FriendResponseDTO> getReceivedPendingFriendList(@PathVariable final int page)
	{
		final FriendResponseDTO friendResponse = this.friendControllerService.getAllReceivedPendingFriendList(page);
		
		return new ResponseEntity<>(friendResponse, HttpStatus.OK);
		
	}
}
