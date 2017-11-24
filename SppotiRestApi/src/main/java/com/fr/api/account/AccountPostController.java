package com.fr.api.account;

import com.fr.commons.dto.ConnexionHistoryDto;
import com.fr.commons.dto.FirebaseDTO;
import com.fr.commons.dto.SignUpDTO;
import com.fr.commons.enumeration.ErrorMessageEnum;
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

import javax.validation.Valid;


/**
 * Created by: Wail DJENANE On June 01, 2016
 */
@RestController
@RequestMapping(value = "/account")
@ApiVersion("1")
class AccountPostController
{
	private final Logger LOGGER = LoggerFactory.getLogger(AccountPostController.class);
	
	private AccountBusinessService accountService;
	
	@Autowired
	void setAccountService(final AccountBusinessService accountService)
	{
		this.accountService = accountService;
	}
	
	@PostMapping(value = "/create")
	@ResponseBody
	ResponseEntity createUser(@RequestBody @Valid final SignUpDTO user)
	{
		this.LOGGER.info("Request sent to create a new user");
		if (StringUtils.isEmpty(user.getFacebookId()) && StringUtils.isEmpty(user.getGoogleId()) &&
				StringUtils.isEmpty(user.getTwitterId()) && StringUtils.isEmpty(user.getPassword())) {
			throw new BusinessGlobalException(ErrorMessageEnum.PASSWORD_REQUIRED.getMessage());
		}
		
		this.accountService.saveNewUser(user);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
		
	}
	
	@PostMapping(value = "/connexion/endpoint")
	@ResponseBody
	ResponseEntity addConnexionHistory(@RequestBody final ConnexionHistoryDto historyDto)
	{
		this.LOGGER.info("Request sent to save user connexion details");
		
		this.accountService.saveConnexionHistory(historyDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
		
	}
	
	@PostMapping(value = "/firebase/register")
	@ResponseBody
	ResponseEntity addConnexionHistory(@RequestBody final FirebaseDTO user)
	{
		this.LOGGER.info("Request sent to save firebase registration key");
		
		if (!StringUtils.hasText(user.getRegistrationId()) || !StringUtils.hasText(user.getUserId())) {
			throw new BusinessGlobalException("Request are incorrect or elements are missing, received data {}", user);
		}
		
		this.accountService.saveFirebaseRegistrationKey(user);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
		
	}
	
	
}