package com.fr.security;

import com.fr.aop.TraceAuthentification;
import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.fr.filter.HeadersAttributes.*;
import static com.fr.filter.HeadersValues.*;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Component
public class AuthFailure extends SimpleUrlAuthenticationFailureHandler {

    /**
     * Class logger.
     */
    private Logger LOGGER = Logger.getLogger(TraceAuthentification.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        LOGGER.info("Failed to log user :-(");

        response.setHeader(ATTR_ORIGIN.getValue(), Origins.getValue());
        response.setHeader(ATTR_CREDENTIALS.getValue(), AllowCredentials.getValue());
        response.setHeader(ATTR_METHODS.getValue(), AllMethods.getValue());
        response.setHeader(ATTR_AGE.getValue(), Max_Age.getValue());
        response.setHeader(ATTR_HEADER.getValue(), Allowed_Headers.getValue());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}