package com.fr.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fr.controllers.service.IndexService;
import com.fr.filter.HeadersValues;

/**
 * Created by: Wail DJENANE On June 01, 2016
 */
@Controller
@CrossOrigin
public class IndexController {

    @Autowired
    private IndexService indService;

    @Autowired
    private PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;

    @Autowired
    private AuthenticationTrustResolver authenticationTrustResolver;

    @GetMapping(value = {"/", "/login"})
    public String homePage() {
        return "index";
    }

    private static Logger LOGGER = Logger.getLogger(IndexController.class);

    /**
     * This method handles logout requests. Toggle the handlers if you are
     * RememberMe functionality is useless in your app.
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void logoutService(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // new SecurityContextLogoutHandler().logout(request, response,
            // auth);
            persistentTokenBasedRememberMeServices.logout(request, response, auth);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        String headerOrigin = request.getHeader("Origin");
        LOGGER.info("Origin requested LOGOUT: " + headerOrigin);
        if (headerOrigin == null || !headerOrigin.equals(HeadersValues.Origins.getValue())) {
            try {
                response.sendRedirect("login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method returns true if users is already authenticated [logged-in],
     * else false.
     */

    private boolean isCurrentAuthenticationAnonymous() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authenticationTrustResolver.isAnonymous(authentication);
    }
}
