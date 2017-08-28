package com.fr.security;

import com.fr.commons.dto.security.AccountUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Moi on 19-Nov-16.
 */
@Component
public class CustomLogoutHandler implements LogoutHandler
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomLogoutHandler.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logout(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse,
					   final Authentication authentication)
	{
		
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth != null && auth.isAuthenticated()) {
			
			final String loggedUser = ((AccountUserDetails)auth.getPrincipal()).getUsername();
			
			LOGGER.info("Logging out user < {} >", loggedUser);
			
			new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, auth);
			SecurityContextHolder.clearContext();
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		}
		
		httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}
}
