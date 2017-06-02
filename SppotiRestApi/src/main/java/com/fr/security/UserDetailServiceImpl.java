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

import java.text.DateFormat;
import java.util.Date;

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
	 * {@inheritDoc}
	 */
	@Override
	public UserDetails loadUserByUsername(final String loginUser) throws UsernameNotFoundException
	{
		
		// database request
		final UserDTO account = this.loginService.getUserByUsername(loginUser);
		
		if (account == null) {
			LOGGER.info("The given login (" + loginUser + " was not found: " + ")");
			throw new UsernameNotFoundException("no user found with: " + loginUser);
		}
		
		final Date date = new Date();
		
		final DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
		
		LOGGER.info("Trying to log user : " + loginUser + " with id=" + account.getId() + " - at: " +
				mediumDateFormat.format(date));
		return new AccountUserDetails(account);
	}
	
	
}
