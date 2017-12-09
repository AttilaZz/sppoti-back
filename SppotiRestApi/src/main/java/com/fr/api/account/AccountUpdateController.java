package com.fr.api.account;

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

import java.util.Objects;

/**
 * Created by djenanewail on 2/12/17.
 */

@RestController
@RequestMapping(value = "/account")
@ApiVersion("1")
class AccountUpdateController
{
	private final Logger LOGGER = LoggerFactory.getLogger(AccountUpdateController.class);
	
	private AccountBusinessService accountService;
	
	@Autowired
	void setAccountService(final AccountBusinessService accountService)
	{
		this.accountService = accountService;
	}
	
	/**
	 * Edit user account information.
	 *
	 * @param user
	 * 		data to update.
	 *
	 * @return updated data with http status 202 if update success, 400 otherwise.
	 */
	@PutMapping
	ResponseEntity<UserDTO> editUserInfo(@RequestBody final UserDTO user)
	{
		this.LOGGER.info("Request sent to edit user account data with this informations {}", user);
		
		boolean update = false;
		
		if (StringUtils.hasText(user.getAvatar()) ||
				(StringUtils.hasText(user.getCover()) && user.getCoverType() != null)) {
			
			this.accountService.updateAvatarAndCover(user);
			
			return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
		} else {
			if (Objects.nonNull(user.getBirthDate())) {
				update = true;
			}
			if (StringUtils.hasText(user.getFirstName())) {
				update = true;
			}
			if (StringUtils.hasText(user.getLastName())) {
				update = true;
			}
			if (StringUtils.hasText(user.getAddress())) {
				update = true;
			}
			if (StringUtils.hasText(user.getUsername())) {
				update = true;
			}
			if (StringUtils.hasText(user.getPhone())) {
				update = true;
			}
			if (StringUtils.hasText(user.getPassword())) {
				update = true;
			}
			if (StringUtils.hasText(user.getEmail())) {
				update = true;
			}
			if (StringUtils.hasText(user.getLanguage())) {
				update = true;
			}
			if (StringUtils.hasText(user.getTimeZone())) {
				update = true;
			}
			if (Objects.nonNull(user.isFirstConnexion()) && !user.isFirstConnexion()) {
				update = true;
			}
			if (Objects.nonNull(user.isProfileComplete())) {
				update = true;
			}
			if (Objects.nonNull(user.getGender())) {
				update = true;
			}
			
			//TODO: Update sports
			
			if (update) {
				this.accountService.updateUser(user);
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
	ResponseEntity<Void> deactivateAccount(@PathVariable final String userId)
	{
		this.LOGGER.info("Request sent to deactivate the account");
		
		this.accountService.deactivateAccount(userId);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
}