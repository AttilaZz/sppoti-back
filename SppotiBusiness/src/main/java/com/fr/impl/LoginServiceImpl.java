package com.fr.impl;

import com.fr.entities.UserEntity;
import com.fr.service.LoginService;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
class LoginServiceImpl extends AbstractControllerServiceImpl implements LoginService
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserEntity getByEmail(final String username)
	{
		return this.userRepository.getByEmailAndDeletedFalseAndConfirmedTrue(username);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserEntity getByTelephone(final String username)
	{
		return this.userRepository.getByTelephoneAndDeletedFalseAndConfirmedTrue(username);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserEntity getByUsername(final String username)
	{
		return this.userRepository.getByUsernameAndDeletedFalseAndConfirmedTrue(username);
	}
}
