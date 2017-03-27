package com.fr.repositoriesImpl;

import java.util.Date;

import javax.transaction.Transactional;

import com.fr.repositories.HibernateTokenRepository;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.fr.entities.PersistentLogin;

/**
 * Created by: Wail DJENANE on Aug 16, 2016
 */

@Component
public class HibernateTokenRepositoryImpl implements PersistentTokenRepository {

    @Autowired
    private HibernateTokenRepository hibernateTokenRepository;

    private static Logger LOGGER = Logger.getLogger(HibernateTokenRepositoryImpl.class);

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        LOGGER.debug("Creating Token for user : {}" + token.getUsername());
        PersistentLogin persistentLogin = new PersistentLogin();
        persistentLogin.setUsername(token.getUsername());
        persistentLogin.setSeries(token.getSeries());
        persistentLogin.setToken(token.getTokenValue());
        persistentLogin.setLast_used(token.getDate());
        hibernateTokenRepository.save(persistentLogin);

    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        LOGGER.debug("Fetch Token if any for seriesId : {}" + seriesId);
        try {

            PersistentLogin persistentLogin = hibernateTokenRepository.getBySeries(seriesId);

            return new PersistentRememberMeToken(persistentLogin.getUsername(), persistentLogin.getSeries(),
                    persistentLogin.getToken(), persistentLogin.getLast_used());
        } catch (Exception e) {
            LOGGER.info("Token not found...");
            return null;
        }
    }

    @Override
    public void removeUserTokens(String username) {
        LOGGER.debug("Removing Token if any for user : {}" + username);

        PersistentLogin persistentLogin = hibernateTokenRepository.getByUsername(username);

        if (persistentLogin != null) {
            LOGGER.info("rememberMe was selected");
            hibernateTokenRepository.delete(persistentLogin);
        }

    }

    @Override
    public void updateToken(String seriesId, String tokenValue, Date lastUsed) {
        LOGGER.debug("Updating Token for seriesId : {}" + seriesId);

        PersistentLogin persistentLogin = hibernateTokenRepository.getBySeries(seriesId);

        persistentLogin.setToken(tokenValue);
        persistentLogin.setLast_used(lastUsed);
        hibernateTokenRepository.delete(persistentLogin);
    }

}
