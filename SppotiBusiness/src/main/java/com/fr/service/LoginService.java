package com.fr.service;

import com.fr.entities.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
public interface LoginService extends AbstractControllerService
{
	
	/**
	 * Find user by email.
	 *
	 * @param username
	 * 		user email.
	 *
	 * @return User data.
	 */
	UserEntity getByEmail(String username);
	
	/**
	 * Find user by his phone number.
	 *
	 * @param username
	 * 		user phone number.
	 *
	 * @return user data.
	 */
	UserEntity getByTelephone(String username);
	
	/**
	 * Find user by his username.
	 *
	 * @param username
	 * 		user username.
	 *
	 * @return user data.
	 */
	UserEntity getByUsername(String username);
}
