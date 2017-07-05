/**
 *
 */
package com.fr.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.fr.filter.HeadersAttributes.*;
import static com.fr.filter.HeadersValues.*;

/**
 * Created by: Wail DJENANE on Jul 1, 2016
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response,
								final Authentication authentication) throws IOException, ServletException
	{
		
		final String[] allowedHeaders = Origins.getValue().split(",");
		
		for (final String allowedHeader : allowedHeaders) {
			if (allowedHeader.equals(request.getHeader("origin"))) {
				response.setHeader(ATTR_ORIGIN.getValue(), request.getHeader("origin"));
			}
		}
		
		response.setHeader(ATTR_CREDENTIALS.getValue(), AllowCredentials.getValue());
		response.setHeader(ATTR_METHODS.getValue(), AllMethods.getValue());
		response.setHeader(ATTR_AGE.getValue(), Max_Age.getValue());
		response.setHeader(ATTR_HEADER.getValue(), Allowed_Headers.getValue());
		
		if (authentication != null) {
			System.out.println(authentication.getName());
			// new SecurityContextLogoutHandler().logout( request, response,
			// authentication);
		}
		/*
		 * perform other required operation
		 */
		// String URL = request.getContextPath() + "/app";
		response.setStatus(HttpStatus.OK.value());
		// response.sendRedirect(URL);
		
	}
	
}
