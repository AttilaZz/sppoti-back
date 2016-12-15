package com.fr.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Moi on 19-Nov-16.
 */
@Component
public class CustomLogoutHandler implements LogoutHandler {


    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(auth.isAuthenticated());
        if (auth.isAuthenticated()) {
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, auth);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
