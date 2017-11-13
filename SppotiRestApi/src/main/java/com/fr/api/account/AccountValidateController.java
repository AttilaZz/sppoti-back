package com.fr.api.account;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.TypeAccountValidation;
import com.fr.commons.exception.BusinessGlobalException;
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
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping(value = "/account")
@ApiVersion("1")
class AccountValidateController
{
	private final Logger LOGGER = LoggerFactory.getLogger(AccountValidateController.class);
	
	private AccountBusinessService accountService;
	
	@Autowired
	void setAccountService(final AccountBusinessService accountService)
	{
		this.accountService = accountService;
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
	ResponseEntity<Void> confirmUserEmail(@PathVariable("code") final String code,
										  @RequestParam("type") final TypeAccountValidation type)
	{
		this.LOGGER.info("Request sent to validate the email in order to activate the account");
		
		if (StringUtils.isEmpty(code) || type == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		// if given code exist in database confirm registration
		this.accountService.tryActivateAccount(code, type);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
		
	}
	
	/**
	 * Send a new confirmation link.
	 *
	 * @param userDTO
	 * 		user data to whom send a link.
	 *
	 * @return 201 http status.
	 */
	@PutMapping("/validate/regenerate/code")
	ResponseEntity<Void> generateNewConfirmationLink(@RequestBody final UserDTO userDTO)
	{
		this.LOGGER.info("Request sent to generate a new confirmation code to activate the account");
		
		if (StringUtils.isEmpty(userDTO.getEmail())) {
			throw new BusinessGlobalException("Email is required !");
		}
		
		this.accountService.generateNewConfirmationEmail(userDTO);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
