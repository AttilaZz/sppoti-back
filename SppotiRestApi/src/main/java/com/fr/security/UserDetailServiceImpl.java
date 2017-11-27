package com.fr.security;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.commons.enumeration.ErrorMessageEnum;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailServiceImpl.class);
	
	private LoginBusinessService loginService;
	
	private static boolean isAttributeEmpty(final String param) {
		return StringUtils.isEmpty(param) || "null".equals(param);
	}
	
	@Autowired
	public void setLoginService(final LoginBusinessService loginService)
	{
		this.loginService = loginService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDetails loadUserByUsername(final String loginUser) throws UsernameNotFoundException, SocialUserIdNotFound
	{
		
		final String[] loginAttributes = loginUser.split(",");
		LOGGER.info("received data from login form: ", Arrays.toString(loginAttributes));
		
		final String username = loginAttributes[0];
		final String facebookId = loginAttributes[1];
		final String googleId = loginAttributes[2];
		final String twitterId = loginAttributes[3];
		final String firebaseToken = loginAttributes[4];
		
		UserDTO account = null;
		boolean isSocial = false;
		
		if (isAttributeEmpty(username) && isAttributeEmpty(facebookId) && isAttributeEmpty(googleId) &&
				isAttributeEmpty(twitterId)) {
			LOGGER.info("At least one parameter must be assigned, {}", Arrays.toString(loginAttributes));
			throw new UsernameNotFoundException("AT least one parameter must be assigned");
		}
		
		if (!isAttributeEmpty(username)) {
			LOGGER.info("Trying to log user : ", username);
			account = this.loginService.getUserByUsernameForLogin(username);
		} else if (!isAttributeEmpty(facebookId)) {
			LOGGER.info("Trying to log user with facebook-id: ", facebookId);
			account = this.loginService.getUserByFacebookId(facebookId);
			isSocial = true;
		} else if (!isAttributeEmpty(googleId)) {
			LOGGER.info("Trying to log user with google-id: ", googleId);
			account = this.loginService.getUserByGoogleId(googleId);
			isSocial = true;
		} else if (!isAttributeEmpty(twitterId)) {
			LOGGER.info("Trying to log user with twitter-id: ", twitterId);
			account = this.loginService.getUserByTwitterId(twitterId);
			isSocial = true;
		}
		
		if (account == null) {
			if (isSocial) {
				throw new SocialUserIdNotFound(ErrorMessageEnum.SOCIAL_USER_ID_NOT_FOUND.getMessage());
			} else {
				throw new UsernameNotFoundException("No user found with: " + loginUser);
			}
		}
		
		account.setFirebaseToken(!isAttributeEmpty(firebaseToken) ? firebaseToken : null);
		return new AccountUserDetails(account);
	}
}