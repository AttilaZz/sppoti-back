package com.fr.api.account;

import com.fr.commons.dto.SignUpDTO;
import com.fr.commons.exception.AccountConfirmationLinkExpiredException;
import com.fr.commons.exception.ConflictEmailException;
import com.fr.commons.exception.ConflictUsernameException;
import com.fr.service.AccountControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * Created by: Wail DJENANE On June 01, 2016
 */
@RestController
@RequestMapping(value = "/account")
class AccountAddController
{
	
	/** Account service. */
	private AccountControllerService accountControllerService;
	
	/** Init account service. */
	@Autowired
	void setAccountControllerService(AccountControllerService accountControllerService)
	{
		this.accountControllerService = accountControllerService;
	}
	
	/**
	 * @param user
	 * 		user to add.
	 *
	 * @return http status 201 if created, ...
	 */
	@PostMapping(value = "/create")
	@ResponseBody
	ResponseEntity createUser(@RequestBody @Valid SignUpDTO user)
	{
		
		try {
			accountControllerService.saveNewUser(user);
			
			return ResponseEntity.status(HttpStatus.CREATED).build();
			
		} catch (ConflictEmailException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (ConflictUsernameException e) {
			return ResponseEntity.status(411).body(e.getMessage());
		} catch (AccountConfirmationLinkExpiredException e) {
			return ResponseEntity.status(413).body(e.getMessage());
		}
		
	}
	
}