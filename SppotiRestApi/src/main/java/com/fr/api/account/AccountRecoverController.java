package com.fr.api.account;

import com.fr.commons.dto.SignUpDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.service.AccountControllerService;
import com.fr.versionning.ApiVersion;
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
	/** Account service. */
	private AccountControllerService accountControllerService;
	
	/** Init account service. */
	@Autowired
	void setAccountControllerService(final AccountControllerService accountControllerService)
	{
		this.accountControllerService = accountControllerService;
	}
	
	/**
	 * Confirm email recover account by editing the password.
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
	 * @return 200 if email found and email sent.
	 */
	@PutMapping("/recover")
	ResponseEntity<Void> confirmUserEmail(@RequestBody final SignUpDTO userDTO)
	{
		
		if (StringUtils.isEmpty(userDTO.getEmail())) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		// if given code exist in database confirm registration
		this.accountControllerService.sendRecoverAccountEmail(userDTO);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
		
	}
	
}
