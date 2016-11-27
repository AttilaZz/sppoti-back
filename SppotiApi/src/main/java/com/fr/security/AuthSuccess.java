package com.fr.security;

import com.fr.RepositoriesService.UserDaoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;

import static com.fr.filter.HeadersAttributes.*;
import static com.fr.filter.HeadersValues.*;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Component
public class AuthSuccess extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    UserDaoService userDao;

    private static final String ATT_USER_ID = "USER_ID";

    private static Logger LOGGER = Logger.getLogger(AuthSuccess.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        LOGGER.info("User has been logged :-)");

        request.getSession().setAttribute(ATT_USER_ID, getAuthenticationId());

        response.setHeader(ATTR_ORIGIN.getValue(), Origins.getValue());
        response.setHeader(ATTR_CREDENTIALS.getValue(), AllowCredentials.getValue());
        response.setHeader(ATTR_METHODS.getValue(), AllMethods.getValue());
        response.setHeader(ATTR_AGE.getValue(), Max_Age.getValue());
        response.setHeader(ATTR_HEADER.getValue(), Allowed_Headers.getValue());

        response.setStatus(HttpServletResponse.SC_OK);

    }

    /**
     * @return logged user id
     */
    private Long getAuthenticationId() {
        Long id = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            id = ((MyUserDetails) principal).getId();
        }

        return id;
    }

}
