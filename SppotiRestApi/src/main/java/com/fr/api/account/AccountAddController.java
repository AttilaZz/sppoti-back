package com.fr.api.account;

import com.fr.commons.dto.ConnexionHistoryDto;
import com.fr.commons.dto.SignUpDTO;
import com.fr.commons.enumeration.ErrorMessageEnum;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.service.AccountBusinessService;
import com.fr.versionning.ApiVersion;
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
class AccountAddController
{
	
	/** Account service. */
	private AccountBusinessService accountService;
	
	/** Init account service. */
	@Autowired
	void setAccountService(final AccountBusinessService accountService)
	{
		this.accountService = accountService;
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
		if (StringUtils.isEmpty(user.getFacebookId()) && StringUtils.isEmpty(user.getGoogleId()) &&
				StringUtils.isEmpty(user.getTwitterId()) && StringUtils.isEmpty(user.getPassword())) {
			throw new BusinessGlobalException(ErrorMessageEnum.PASSWORD_REQUIRED.getMessage());
		}
		
		this.accountService.saveNewUser(user);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
		
	}
	
	/**
	 * Save user connexion details.
	 *
	 * @param historyDto
	 * 		details to save.
	 *
	 * @return 201 if data has been saved correctly.
	 */
	@PostMapping(value = "/connexion/endpoint")
	@ResponseBody
	ResponseEntity addConnexionHistory(@RequestBody final ConnexionHistoryDto historyDto)
	{
		
		this.accountService.saveConnexionHistory(historyDto);
		
		return ResponseEntity.status(HttpStatus.OK).build();
		
	}
	
}