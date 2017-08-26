package com.fr.service;

import com.fr.commons.dto.UserDTO;

/**
 * Created by djenanewail on 1/22/17.
 */
public interface LoginBusinessService extends AbstractBusinessService
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
