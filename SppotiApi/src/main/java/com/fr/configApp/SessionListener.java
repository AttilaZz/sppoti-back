/**
 *
 */
package com.fr.configApp;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

/**
 * Created by: Wail DJENANE on Aug 16, 2016
 */
public class SessionListener implements HttpSessionListener {

    private Logger LOGGER = Logger.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        LOGGER.info("==== Session (" + se.getSession().getId() + ") is created ====");
        se.getSession().setMaxInactiveInterval(0);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        LOGGER.info("==== Session is destroyed ====");
    }

}
