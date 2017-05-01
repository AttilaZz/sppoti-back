package com.fr.api.account;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.service.AccountControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping(value = "/account")
class AccountValidateController
{
	/** Account service. */
	private AccountControllerService accountControllerService;
	
	/** Init service. */
	@Autowired
	void setAccountControllerService(AccountControllerService accountControllerService)
	{
		this.accountControllerService = accountControllerService;
	}
	
	/**
	 * Confirm email to get access to sppoti.
	 *
	 * @param code
	 * 		confirmation code.
	 *
	 * @return 202 status if account enabled.
	 */
	@PutMapping(value = "/validate/{code}")
	ResponseEntity<Void> confirmUserEmail(@PathVariable("code") String code)
	{
		
		if (StringUtils.isEmpty(code)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		// if given code exist in database confirm registration
		accountControllerService.tryActivateAccount(code);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
		
	}
	
	
	/**
	 * Send a new confirmation link.
	 *
	 * @param userDTO
	 * 		user data to whom send a link.
	 *
	 * @return
	 */
	@PutMapping("/validate/regenerate/code")
	ResponseEntity<Void> generateNewConfirmationLink(@RequestBody UserDTO userDTO)
	{
		
		if (StringUtils.isEmpty(userDTO.getEmail())) {
			throw new BusinessGlobalException("Email is required !");
		}
		
		accountControllerService.generateNewConfirmationEmail(userDTO);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
