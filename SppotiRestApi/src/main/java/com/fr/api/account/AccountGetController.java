package com.fr.api.account;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.entities.UserEntity;
import com.fr.service.AccountControllerService;
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
class AccountGetController
{
	
	/** Account controller service. */
	private AccountControllerService accountControllerService;
	
	/** Init account service. */
	@Autowired
	void setAccountControllerService(final AccountControllerService accountControllerService)
	{
		this.accountControllerService = accountControllerService;
	}
	
	/**
	 * Get connected user account informations.
	 */
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@GetMapping
	ResponseEntity<UserDTO> connectedUserInfo(final Authentication authentication)
	{
		
		final AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		final UserEntity targetUser = this.accountControllerService.getUserById(accountUserDetails.getId());
		
		return new ResponseEntity<>(this.accountControllerService.fillAccountResponse(targetUser), HttpStatus.OK);
		
	}
	
	/**
	 * HGet any user account information by its username.
	 *
	 * @param username
	 * 		username of a user.
	 *
	 * @return user data.
	 */
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	@GetMapping("/other/{username}/**")
	ResponseEntity<UserDTO> otherUserInfo(@PathVariable("username") final String username,
										  final Authentication authentication)
	{
		
		//        String path = (String) httpServletRequest.getAttribute(
		//                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		//        AntPathMatcher apm = new AntPathMatcher();
		//        String bestMatchPattern = (String ) httpServletRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		//        String finalPath = apm.extractPathWithinPattern(bestMatchPattern, path);
		
		final AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		
		return new ResponseEntity<>(
				this.accountControllerService.handleFriendShip(username, accountUserDetails.getId()), HttpStatus.OK);
		
	}
}
