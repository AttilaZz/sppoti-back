package com.fr.service;

import com.fr.commons.dto.UserDTO;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
public interface LoginService extends AbstractControllerService
{
	/**
	 * Find user by username.
	 *
	 * @param username
	 * 		of the user.
	 *
	 * @return user data.
	 */
	UserDTO getUserByUsernameForLogin(String username);
	
	/**
	 * Find user by facebook id.
	 *
	 * @param facebookId
	 * 		facebook id.
	 *
	 * @return user data.
	 */
	UserDTO getUserByFacebookId(String facebookId);
	
	/**
	 * find user by google id.
	 *
	 * @param googleId
	 * 		user id in google api.
	 *
	 * @return user data.
	 */
	UserDTO getUserByGoogleId(String googleId);
	
	/**
	 * find user by twitter id.
	 *
	 * @param twitterId
	 * 		user id in twitter api.
	 *
	 * @return user data.
	 */
	UserDTO getUserByTwitterId(String twitterId);
}
