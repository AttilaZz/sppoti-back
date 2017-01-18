package com.fr.security;

import com.fr.aop.TraceControllers;
import com.fr.controllers.service.implem.AbstractControllerServiceImpl;
import com.fr.entities.Users;
import org.apache.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Component
public class UserDetailServiceImpl extends AbstractControllerServiceImpl implements UserDetailsService {

    private static Logger LOGGER = Logger.getLogger(TraceControllers.class);

    @Override
    public UserDetails loadUserByUsername(String loginUser) throws UsernameNotFoundException {

        // database request
        Users account = getUserByLogin(loginUser);

        if (account == null) {
            LOGGER.info("The given login (" + loginUser + " was not found: " + ")");
            throw new UsernameNotFoundException("no user found with: " + loginUser);
        }

        Date aujourdhui = new Date();

        DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM,
                DateFormat.MEDIUM);

        LOGGER.info("Trying to log user : " + loginUser + " with id=" + account.getId() + " - at: " + mediumDateFormat.format(aujourdhui));
        return new AccountUserDetails(account);
    }

    Users getUserByLogin(String username) {
        String numberRegex = "[0-9]+";

        if (username.contains("@")) {
            return userRepository.getByEmail(username);
        } else if (username.matches(numberRegex)) {
            return userRepository.getByTelephone(username);
        } else {
            return userRepository.getByUsername(username);
        }

    }

}
