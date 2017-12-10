package com.fr.impl;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.UserEntity;
import com.fr.repositories.FirebaseRegistrationRepository;
import com.fr.service.LoginBusinessService;
import com.fr.transformers.UserTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
class LoginBusinessServiceImpl extends CommonControllerServiceImpl implements LoginBusinessService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginBusinessServiceImpl.class);
	
	@Autowired
	private UserTransformer userTransformer;
	
	@Autowired
	private FirebaseRegistrationRepository firebaseRegistrationRepository;
	
	/**
	 * {@inheritDoc}
	 */
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
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void updateUserDeviceToConnectedStatus(final String firebaseRegistrationKey, final String userEmail) {
		this.firebaseRegistrationRepository.findByRegistrationKey(firebaseRegistrationKey).ifPresent(e -> {
			LOGGER.info("Device status for user {} using token {}, has been activated", userEmail,
					firebaseRegistrationKey);
			e.setDeviceConnected(true);
			this.firebaseRegistrationRepository.save(e);
		});
	}
	
	/**
	 * if account is deactivated and suppress date is less than 90 days reactivate account.
	 *
	 * @param entity
	 * 		user entity.
	 *
	 * @return dto of the logged user.
	 */
	@Transactional
	private UserDTO checkLoginRulesAndGetUser(final UserEntity entity) {
		final UserEntity temp;
		
		if (entity != null) {
			if (entity.getDeactivationDate() != null &&
					!SppotiUtils.isAccountReadyToBeCompletlyDeleted(entity.getDeactivationDate(), 90)) {
				LOGGER.info("Account {} has been reactivated", entity.getEmail());
				entity.setDeleted(false);
				temp = this.userRepository.saveAndFlush(entity);
				return this.userTransformer.modelToDto(temp);
			}
			return this.userTransformer.modelToDto(entity);
			
		}
		return null;
	}
}