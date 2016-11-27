package com.fr.repositoriesImpl;

import java.util.Date;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

import com.fr.pojos.PersistentLogin;

/**
 * Created by: Wail DJENANE on Aug 16, 2016
 */
@Repository("tokenRepositoryDao")
@Transactional
public class HibernateTokenRepositoryImpl extends GenericDaoImpl<PersistentLogin, String>
        implements PersistentTokenRepository {

    public HibernateTokenRepositoryImpl() {
        this.entityClass = PersistentLogin.class;
    }

    private static Logger LOGGER = Logger.getLogger(HibernateTokenRepositoryImpl.class);

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        LOGGER.debug("Creating Token for user : {}" + token.getUsername());
        PersistentLogin persistentLogin = new PersistentLogin();
        persistentLogin.setUsername(token.getUsername());
        persistentLogin.setSeries(token.getSeries());
        persistentLogin.setToken(token.getTokenValue());
        persistentLogin.setLast_used(token.getDate());
        persist(persistentLogin);

    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        LOGGER.debug("Fetch Token if any for seriesId : {}" + seriesId);
        try {
            Criteria crit = getSession().createCriteria(entityClass);
            crit.add(Restrictions.eq("series", seriesId));
            PersistentLogin persistentLogin = (PersistentLogin) crit.uniqueResult();

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
        Criteria crit = getSession().createCriteria(entityClass);
        crit.add(Restrictions.eq("username", username));
        PersistentLogin persistentLogin = (PersistentLogin) crit.uniqueResult();
        if (persistentLogin != null) {
            LOGGER.info("rememberMe was selected");
            delete(persistentLogin);
        }

    }

    @Override
    public void updateToken(String seriesId, String tokenValue, Date lastUsed) {
        LOGGER.debug("Updating Token for seriesId : {}" + seriesId);

        Criteria crit = getSession().createCriteria(entityClass);
        crit.add(Restrictions.eq("series", seriesId));
        PersistentLogin persistentLogin = (PersistentLogin) crit.uniqueResult();

        persistentLogin.setToken(tokenValue);
        persistentLogin.setLast_used(lastUsed);
        update(persistentLogin);
    }

}
