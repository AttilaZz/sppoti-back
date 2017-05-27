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
	LoginServiceImpl() {
		super(this.messagingTemplate);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserEntity getByEmail(final String username)
	{
		return this.userRepository.getByEmailAndDeletedFalse(username);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserEntity getByTelephone(final String username)
	{
		return this.userRepository.getByTelephoneAndDeletedFalse(username);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserEntity getByUsername(final String username)
	{
		return this.userRepository.getByUsernameAndDeletedFalse(username);
	}
}
