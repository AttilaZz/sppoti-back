package com.fr.api.account;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.entities.UserEntity;
import com.fr.service.AccountBusinessService;
import com.fr.versionning.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping(value = "/account")
@ApiVersion("1")
class AccountGetController
{
	private final Logger LOGGER = LoggerFactory.getLogger(AccountGetController.class);
	
	private AccountBusinessService accountService;
	
	@Autowired
	void setAccountService(final AccountBusinessService accountService)
	{
		this.accountService = accountService;
	}
	
	/**
	 * Get connected user account informations.
	 */
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@GetMapping
	ResponseEntity<UserDTO> connectedUserInfo(final Authentication authentication)
	{
		this.LOGGER.info("Request sent to get connected user data");
		
		final AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		final UserEntity targetUser = this.accountService.getUserById(accountUserDetails.getId());
		
		return new ResponseEntity<>(this.accountService.fillAccountResponse(targetUser), HttpStatus.OK);
		
	}
	
	/**
	 * HGet any user account information by its username.
	 *
	 * @param username
	 * 		username of a user.
	 *
	 * @return user data.
	 */
	@GetMapping("/other/{username}/**")
	ResponseEntity<UserDTO> otherUserInfo(@PathVariable("username") final String username)
	{
		this.LOGGER.info("Request sent to get any user data");
		
		return new ResponseEntity<>(this.accountService.getAnyUserProfileData(username), HttpStatus.OK);
	}
}
