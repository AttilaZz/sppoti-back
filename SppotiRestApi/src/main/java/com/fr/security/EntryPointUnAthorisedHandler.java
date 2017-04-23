package com.fr.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
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
public class EntryPointUnAthorisedHandler implements AuthenticationEntryPoint {

    /**
     * {@inheritDoc}
     */
    @Override
    public void commence(HttpServletRequest arg0, HttpServletResponse arg1, AuthenticationException arg2)
            throws IOException, ServletException {

        arg1.setHeader(ATTR_ORIGIN.getValue(), Origins.getValue());
        arg1.setHeader(ATTR_CREDENTIALS.getValue(), AllowCredentials.getValue());
        arg1.setHeader(ATTR_METHODS.getValue(), AllMethods.getValue());
        arg1.setHeader(ATTR_AGE.getValue(), Max_Age.getValue());
        arg1.setHeader(ATTR_HEADER.getValue(), Allowed_Headers.getValue());

        arg1.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized Access");
    }

}
