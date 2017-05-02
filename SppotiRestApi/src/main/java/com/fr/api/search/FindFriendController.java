package com.fr.api.search;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.entities.FriendShipEntity;
import com.fr.repositories.FriendShipRepository;
import com.fr.service.AccountControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wdjenane on 10/02/2017.
 */
@RestController
@RequestMapping("/find/friends")
class FindFriendController
{
	/** Friend list size. */
	@Value("${key.friendShipPerPage}")
	private int friend_size;
	
	/** Friend repository. */
	private final FriendShipRepository friendShipRepository;
	
	/** Account controller service. */
	private final AccountControllerService accountControllerService;
	
	/** Init services. */
	@Autowired
	public FindFriendController(final FriendShipRepository friendShipRepository,
								final AccountControllerService accountControllerService)
	{
		this.friendShipRepository = friendShipRepository;
		this.accountControllerService = accountControllerService;
	}
	
	/**
	 * @param userPrefix
	 * 		user prefix to search.
	 * @param page
	 * 		page number.
	 *
	 * @return All confirmed friends of connected user
	 */
	@GetMapping(value = "/{motif}/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<List<UserDTO>> searchConfimedFriend(@PathVariable("motif") final String userPrefix,
													   @PathVariable("page") final int page)
	{
		
		
		if (userPrefix.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		final List<FriendShipEntity> foundFriends;
		final Pageable pageable = new PageRequest(page, this.friend_size);
		
		final String[] parts = userPrefix.split(" ");
		
		if (parts.length > 2) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else //get users by first name and last name
			if (parts.length == 2)
				foundFriends = this.friendShipRepository.findFriendsByFirstNameAndLastNameAndStatus(parts[0], parts[1],
						GlobalAppStatusEnum.CONFIRMED.name(), pageable);
			else {
				//get users by username, first name and last name
				foundFriends = this.friendShipRepository
						.findFriendByUsernameAndStatus(parts[0], GlobalAppStatusEnum.CONFIRMED.name(), pageable);
				
			}
		
		final List<UserDTO> users = new ArrayList<>();
		
		for (final FriendShipEntity friendShip : foundFriends) {
			
			users.add(this.accountControllerService.fillAccountResponse(friendShip.getFriend()));
		}
		
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
}
