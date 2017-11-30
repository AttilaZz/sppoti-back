package com.fr.api.friend;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.service.FriendBusinessService;
import com.fr.versionning.ApiVersion;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private final Logger LOGGER = LoggerFactory.getLogger(FriendAddController.class);
	
	private FriendBusinessService friendControllerService;
	
	@Autowired
	void setFriendControllerService(final FriendBusinessService friendControllerService)
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
		
		this.LOGGER.info("Request sent to add a friend");
		
		if (StringUtils.isBlank(user.getFriendUuid())) {
			throw new BusinessGlobalException("Friend id is empty");
		}
		
		this.friendControllerService.saveFriendShip(user);
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
}
