package com.fr.api.friend;

import com.fr.commons.dto.FriendResponseDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.entities.FriendShipEntity;
import com.fr.entities.UserEntity;
import com.fr.security.AccountUserDetails;
import com.fr.service.AccountControllerService;
import com.fr.service.FriendControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djenanewail on 2/11/17.
 */

@RestController
@RequestMapping("/friend")
class FriendGetController
{
	
	/** Account service. */
	private final AccountControllerService accountControllerService;
	
	/** Friend service. */
	private final FriendControllerService friendControllerService;
	
	/** Friend list size. */
	@Value("${key.friendShipPerPage}")
	private int friendListSize;
	
	/** Init services. */
	@Autowired
	FriendGetController(final AccountControllerService accountControllerService,
						final FriendControllerService friendControllerService)
	{
		this.accountControllerService = accountControllerService;
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
	ResponseEntity<List<UserDTO>> getConfirmedFriendList(@PathVariable final int userId, @PathVariable final int page)
	{
		
		final List<UserDTO> friendList = this.friendControllerService.getConfirmedFriendList(userId, page);
		
		return new ResponseEntity<>(friendList, HttpStatus.OK);
		
	}
	
	/**
	 * @param userId
	 * 		connected user id.
	 * @param page
	 * 		page number.
	 * @param authentication
	 * 		spring secu.
	 *
	 * @return all refused friend requests.
	 */
	@GetMapping("/refused/{userId}/{page}")
	ResponseEntity<FriendResponseDTO> getRefusedFriendList(@PathVariable final int userId, @PathVariable final int page,
														   final Authentication authentication)
	{
		
		final Long connected_user = ((AccountUserDetails) authentication.getPrincipal()).getId();
		final UserEntity connectedUser = this.friendControllerService.getUserById(connected_user);
		
		
		final Pageable pageable = new PageRequest(page, this.friendListSize);
		
		final List<FriendShipEntity> friendShips = this.friendControllerService
				.getByUserAndStatus(connectedUser.getUuid(), GlobalAppStatusEnum.REFUSED, pageable);
		
		if (friendShips.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		final FriendResponseDTO friendResponse = getFriendResponse(friendShips, connected_user);
		
		return new ResponseEntity<>(friendResponse, HttpStatus.OK);
		
	}
	
	
	/**
	 * @param page
	 * 		page number.
	 * @param authentication
	 * 		spring secu.
	 *
	 * @return all pending requests.
	 */
	@GetMapping("/pending/sent/{page}")
	ResponseEntity<FriendResponseDTO> getSentPendingFriendList(@PathVariable final int page,
															   final Authentication authentication)
	{
		
		final Long connected_user = ((AccountUserDetails) authentication.getPrincipal()).getId();
		final UserEntity connectedUser = this.friendControllerService.getUserById(connected_user);
		
		final Pageable pageable = new PageRequest(page, this.friendListSize);
		
		final List<FriendShipEntity> friendShips = this.friendControllerService
				.getByUserAndStatus(connectedUser.getUuid(), GlobalAppStatusEnum.PENDING, pageable);
		
		if (friendShips.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		final FriendResponseDTO friendResponse = getFriendResponse(friendShips, connected_user);
		
		return new ResponseEntity<>(friendResponse, HttpStatus.OK);
		
	}
	
	/**
	 * @param page
	 * 		page number.
	 * @param authentication
	 * 		spring security.
	 *
	 * @return all friend pending requests.
	 */
	@GetMapping("/pending/received/{page}")
	ResponseEntity<FriendResponseDTO> getReceivedPendingFriendList(@PathVariable final int page,
																   final Authentication authentication)
	{
		
		final Long connected_user = ((AccountUserDetails) authentication.getPrincipal()).getId();
		final UserEntity connectedUser = this.friendControllerService.getUserById(connected_user);
		
		final Pageable pageable = new PageRequest(page, this.friendListSize);
		
		final List<FriendShipEntity> friendShips = this.friendControllerService
				.getByFriendUuidAndStatus(connectedUser.getUuid(), GlobalAppStatusEnum.PENDING, pageable);
		
		if (friendShips.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		
		final FriendResponseDTO friendResponse = getFriendResponse(friendShips, connected_user);
		
		return new ResponseEntity<>(friendResponse, HttpStatus.OK);
		
	}
	
	/**
	 * @param friendShips
	 * 		list of all friendships.
	 *
	 * @return friend response DTO.
	 */
	private FriendResponseDTO getFriendResponse(final List<FriendShipEntity> friendShips, final Long connectedUser)
	{
		final List<UserDTO> friendList = new ArrayList<>();
		
		for (final FriendShipEntity friendShip : friendShips) {
			final UserEntity user;
			
			if (friendShip.getFriend().getId().equals(connectedUser)) {
				user = friendShip.getUser();
			} else {
				user = friendShip.getFriend();
			}
			
			friendList.add(this.accountControllerService.fillAccountResponse(user));
			
		}
		
		final FriendResponseDTO friendResponse = new FriendResponseDTO();
		friendResponse.setPendingList(friendList);
		
		return friendResponse;
	}
}
