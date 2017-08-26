package com.fr.security;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.exceptions.SocialUserIdNotFound;
import com.fr.service.LoginBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Component
public class UserDetailServiceImpl implements UserDetailsService
{
	/** Class logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailServiceImpl.class);
	
	/** Login service. */
	private LoginBusinessService loginService;
	
	/** Init login service. */
	@Autowired
	public void setLoginService(final LoginBusinessService loginService)
	{
		this.loginService = loginService;
	}
	
	/**
	 * Login logic, based on parameters sent by the user.
	 *
	 * @param loginUser
	 * 		login parameters imploded in a string separated by comma, ex [a,b,c,d] a: username b: facebook id c: google id
	 * 		d: twitter id
	 *
	 * @return AccountUserDetails
	 *
	 * @throws UsernameNotFoundException if username not found.
	 * @throws SocialUserIdNotFound if social id not found.
	 */
	@Override
	public UserDetails loadUserByUsername(final String loginUser) throws UsernameNotFoundException
	{
		
		final String[] loginAttributes = loginUser.split(",");
		LOGGER.info("received data from login form: ", Arrays.toString(loginAttributes));
		
		final String username = loginAttributes[0];
		final String facebookId = loginAttributes[1];
		final String googleId = loginAttributes[2];
		final String twitterId = loginAttributes[3];
		
		UserDTO account = null;
		boolean isSocial = false;
		if (StringUtils.hasText(username)) {
			account = this.loginService.getUserByUsernameForLogin(username);
		} else if (StringUtils.hasText(facebookId)) {
			account = this.loginService.getUserByFacebookId(facebookId);
			isSocial = true;
		} else if (StringUtils.hasText(googleId)) {
			account = this.loginService.getUserByGoogleId(googleId);
			isSocial = true;
		} else if (StringUtils.hasText(twitterId)) {
			account = this.loginService.getUserByTwitterId(twitterId);
			isSocial = true;
		}
		
		if (account == null) {
			LOGGER.info("The given login (" + loginUser + " was not found: " + ")");
			if (isSocial) {
				throw new SocialUserIdNotFound("Social id is not valid");
			} else {
				throw new UsernameNotFoundException("no user found with: " + loginUser);
			}
		}
		
		LOGGER.info("Trying to log user : ", username);
		return new AccountUserDetails(account);
	}
	
}