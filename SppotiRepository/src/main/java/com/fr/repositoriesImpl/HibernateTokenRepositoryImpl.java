package com.fr.repositoriesImpl;

import com.fr.entities.PersistentLogin;
import com.fr.repositories.HibernateTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by: Wail DJENANE on Aug 16, 2016
 */

@Component
public class HibernateTokenRepositoryImpl implements PersistentTokenRepository
{
	/** Hibernate token repository. */
	@Autowired
	private HibernateTokenRepository hibernateTokenRepository;
	
	/** Class logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateTokenRepositoryImpl.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createNewToken(final PersistentRememberMeToken token)
	{
		LOGGER.debug("Creating Token for user : {}" + token.getUsername());
		final PersistentLogin persistentLogin = new PersistentLogin();
		persistentLogin.setUsername(token.getUsername());
		persistentLogin.setSeries(token.getSeries());
		persistentLogin.setToken(token.getTokenValue());
		persistentLogin.setLast_used(token.getDate());
		this.hibernateTokenRepository.save(persistentLogin);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PersistentRememberMeToken getTokenForSeries(final String seriesId)
	{
		LOGGER.debug("Fetch Token if any for seriesId : {}" + seriesId);
		try {
			
			final PersistentLogin persistentLogin = this.hibernateTokenRepository.getBySeries(seriesId);
			
			return new PersistentRememberMeToken(persistentLogin.getUsername(), persistentLogin.getSeries(),
					persistentLogin.getToken(), persistentLogin.getLast_used());
		} catch (final Exception e) {
			LOGGER.info("Token not found...");
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeUserTokens(final String username)
	{
		LOGGER.debug("Removing Token if any for user : {}" + username);
		
		final PersistentLogin persistentLogin = this.hibernateTokenRepository.getByUsername(username);
		
		if (persistentLogin != null) {
			LOGGER.info("rememberMe was selected");
			this.hibernateTokenRepository.delete(persistentLogin);
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateToken(final String seriesId, final String tokenValue, final Date lastUsed)
	{
		LOGGER.debug("Updating Token for seriesId : {}" + seriesId);
		
		final PersistentLogin persistentLogin = this.hibernateTokenRepository.getBySeries(seriesId);
		
		persistentLogin.setToken(tokenValue);
		persistentLogin.setLast_used(lastUsed);
		this.hibernateTokenRepository.delete(persistentLogin);
	}
	
}
