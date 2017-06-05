package com.fr.api.account;

import com.fr.commons.dto.UserDTO;
import com.fr.service.AccountControllerService;
import com.fr.versionning.ApiVersion;
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
class AccountUpdateController
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
	 * Edit user account information.
	 *
	 * @param user
	 * 		data to update.
	 *
	 * @return updated data with http status 202 if update success, 400 otherwise.
	 */
	@PutMapping("/")
	ResponseEntity<UserDTO> editUserInfo(@RequestBody final UserDTO user)
	{
		
		boolean update = false;
		
		if (StringUtils.hasText(user.getAvatar()) ||
				(StringUtils.hasText(user.getCover()) && user.getCoverType() != null)) {
			
			this.accountControllerService.updateAvatarAndCover(user);
			
			return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
		} else {
			//first name
			if (StringUtils.hasText(user.getFirstName())) {
				update = true;
			}
			//last name
			if (StringUtils.hasText(user.getLastName())) {
				update = true;
			}
			//address
			if (StringUtils.hasText(user.getAddress())) {
				update = true;
			}
			//username
			if (StringUtils.hasText(user.getUsername())) {
				update = true;
			}
			//phone
			if (StringUtils.hasText(user.getPhone())) {
				update = true;
			}
			//password
			if (StringUtils.hasText(user.getPassword())) {
				update = true;
			}
			//email
			if (StringUtils.hasText(user.getEmail())) {
				update = true;
			}
			//language
			if (StringUtils.hasText(user.getLanguage())) {
				update = true;
			}
			//language
			if (StringUtils.hasText(user.getTimeZone())) {
				update = true;
			}
			
			//TODO: Update sports
			
			if (update) {
				this.accountControllerService.updateUser(user);
				return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
			}
			
		}
		
		return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Deactivate account.
	 *
	 * @param userId
	 * 		user id.
	 *
	 * @return 202 status if account has been deactivated.
	 */
	@PutMapping("/deactivate/{userId}")
	ResponseEntity<Void> deactivateAccount(@PathVariable final int userId)
	{
		this.accountControllerService.deactivateAccount(userId);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
		
	}
	
}