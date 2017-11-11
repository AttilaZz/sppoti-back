package com.fr.impl;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.UserEntity;
import com.fr.service.LoginBusinessService;
import com.fr.transformers.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
class LoginBusinessServiceImpl extends CommonControllerServiceImpl implements LoginBusinessService
{
	@Autowired
	private UserTransformer userTransformer;
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public UserDTO getUserByUsernameForLogin(final String username) {
		return checkLoginRulesAndGetUser(getUserByLogin(username, false));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDTO getUserByFacebookId(final String facebookId) {
		return checkLoginRulesAndGetUser(this.userRepository.findByFacebookId(facebookId));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDTO getUserByGoogleId(final String googleId) {
		return checkLoginRulesAndGetUser(this.userRepository.findByGoogleId(googleId));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDTO getUserByTwitterId(final String twitterId) {
		return checkLoginRulesAndGetUser(this.userRepository.findByTwitterId(twitterId));
	}
	
	/**
	 * if account is deactivated and suppress date is less than 90 days reactivate account.
	 *
	 * @param entity
	 * 		user entity.
	 *
	 * @return dto of the logged user.
	 */
	private UserDTO checkLoginRulesAndGetUser(final UserEntity entity) {
		final UserEntity temp;
		
		if (entity != null) {
			if (entity.getDeactivationDate() != null &&
					!SppotiUtils.isAccountReadyToBeCompletlyDeleted(entity.getDeactivationDate(), 90)) {
				entity.setDeleted(false);
				temp = this.userRepository.saveAndFlush(entity);
				return this.userTransformer.modelToDto(temp);
			}
			return this.userTransformer.modelToDto(entity);
			
		}
		return null;
	}
}