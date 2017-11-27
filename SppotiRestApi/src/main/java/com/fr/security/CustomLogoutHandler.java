package com.fr.security;

import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.entities.FirebaseRegistrationEntity;
import com.fr.repositories.FirebaseRegistrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Moi on 19-Nov-16.
 */
@Component
public class CustomLogoutHandler implements LogoutHandler
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomLogoutHandler.class);
	
	@Autowired
	private FirebaseRegistrationRepository firebaseRegistrationRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void logout(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse,
					   final Authentication authentication)
	{
		
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth != null && auth.isAuthenticated()) {
			
			final String loggedUser = ((AccountUserDetails) auth.getPrincipal()).getUsername();
			
			LOGGER.info("Logging out user < {} >", loggedUser);
			
			final String fbToken = getUserFirebaseToken();
			if (StringUtils.hasText(fbToken) && !"null".equals(fbToken)) {
				LOGGER.info("Logging out mobile device, using firebase token {}", fbToken);
				final FirebaseRegistrationEntity entity = this.firebaseRegistrationRepository
						.findByRegistrationKey(fbToken);
				entity.setDeviceConnected(false);
				this.firebaseRegistrationRepository.save(entity);
			}
			
			new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, auth);
			SecurityContextHolder.clearContext();
			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		}
		
		httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}
	
	private String getUserFirebaseToken() {
		final AccountUserDetails accountUserDetails = (AccountUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		return accountUserDetails.getFirebaseToken();
	}
}
