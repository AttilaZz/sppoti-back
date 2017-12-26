package com.fr.api.account;

import com.fr.commons.dto.SignUpDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.service.AccountBusinessService;
import com.fr.versionning.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by djenanewail on 4/9/17.
 */

@RestController
@RequestMapping(value = "/account")
@ApiVersion("1")
public class AccountRecoverController
{
	private final Logger LOGGER = LoggerFactory.getLogger(AccountRecoverController.class);
	
	private AccountBusinessService accountControllerService;
	
	@Autowired
	void setAccountControllerService(final AccountBusinessService accountControllerService)
	{
		this.accountControllerService = accountControllerService;
	}
	
	/**
	 * Confirm recover email.
	 *
	 * @param code
	 * 		confirmation code.
	 * @param userDTO
	 * 		user data.
	 *
	 * @return 202 status if account recovered correctly.
	 */
	@PutMapping("/validate/password/{code}")
	ResponseEntity<Void> confirmUserEmail(@RequestBody final UserDTO userDTO, @PathVariable("code") final String code)
	{
		this.LOGGER.info("Request sent to validate the password after account recover");
		
		if (StringUtils.isEmpty(code) || StringUtils.isEmpty(userDTO.getPassword())) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		// if given code exist in database confirm registration
		this.accountControllerService.recoverAccount(userDTO, code);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
		
	}
	
	/**
	 * Send email to recover account.
	 *
	 * @param userDTO
	 * 		user data.
	 *
	 * @return 200 if email found and messages sent.
	 */
	@PutMapping("/recover")
	ResponseEntity<Void> recoverAccount(@RequestBody final SignUpDTO userDTO)
	{
		this.LOGGER.info("Request sent to recover the password");
		
		if (StringUtils.isEmpty(userDTO.getEmail())) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		// if given code exist in database confirm registration
		this.accountControllerService.sendRecoverAccountEmail(userDTO);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
		
	}
	
}
