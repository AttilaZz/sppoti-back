package com.fr.api.account;

import com.fr.commons.dto.SignUpDTO;
import com.fr.service.AccountControllerService;
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
	void setAccountControllerService(final AccountControllerService accountControllerService)
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
	ResponseEntity createUser(@RequestBody @Valid final SignUpDTO user)
	{
		
		this.accountControllerService.saveNewUser(user);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
		
	}
	
}