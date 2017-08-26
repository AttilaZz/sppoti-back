package com.fr.security;

import com.fr.aop.TraceControllers;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Component
public class UserDetailServiceImpl implements UserDetailsService
{
	/** Class logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TraceControllers.class);
	
	/** Login service. */
	private LoginService loginService;
	
	/** Init login service. */
	@Autowired
	public void setLoginService(final LoginService loginService)
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
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(final String loginUser) throws UsernameNotFoundException
	{
		
		final String[] loginAttributes = loginUser.split(",");
		
		final String username = loginAttributes[0];
		final String facebookId = loginAttributes[1];
		final String googleId = loginAttributes[2];
		final String twitterId = loginAttributes[3];
		
		// database request
		final UserDTO account = this.loginService.getUserByUsernameForLogin(username);
		
		if (account == null) {
			LOGGER.info("The given login (" + loginUser + " was not found: " + ")");
			throw new UsernameNotFoundException("no user found with: " + loginUser);
		}
		
		LOGGER.info("Trying to log user : ", username);
		return new AccountUserDetails(account);
	}
	
	
}
