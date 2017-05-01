package com.fr.api.search;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.UserEntity;
import com.fr.repositories.UserRepository;
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

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 12/17/16.
 */

@RestController
@RequestMapping("/find/users")
class FindUsersController
{
	
	/** Firiend list size. */
	@Value("${key.friendShipPerPage}")
	private int friend_size;
	
	/** USer repository. */
	private final UserRepository userRepository;
	/** Account service. */
	private final AccountControllerService accountControllerService;
	
	/** Init service. */
	@Autowired
	public FindUsersController(final UserRepository userRepository,
							   final AccountControllerService accountControllerService)
	{
		this.userRepository = userRepository;
		this.accountControllerService = accountControllerService;
	}
	
	/**
	 * @param userPrefix
	 * 		user prefix to find.
	 * @param page
	 * 		page number.
	 *
	 * @return List of all users containing the STRING in request
	 */
	@GetMapping(value = "/{user}/{page}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<List<UserDTO>> searchUser(@PathVariable("user") final String userPrefix,
											 @PathVariable("page")    final int page)
	{
		
		//TODO: move implementation to CORE MODULE
		
		if (userPrefix.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		final List<UserEntity> foundUsers;
		final Pageable pageable = new PageRequest(page, this.friend_size);
		
		final String[] parts = userPrefix.split(" ");
		
		if (parts.length > 2) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else if (parts.length == 2) {
			//get users by first name and last name
			foundUsers = this.userRepository.getSearchedUsersByFirstNameAndLastName(parts[0], parts[1], pageable);
		} else {
			//get users by username, first name and last name
			foundUsers = this.userRepository.getSearchedUsers(parts[0], pageable);
			
		}
		
		final List<UserDTO> users = foundUsers.stream().map(this.accountControllerService::fillAccountResponse)
				.collect(Collectors.toList());
		
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	
}
