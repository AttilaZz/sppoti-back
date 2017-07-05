package com.fr.security;

import com.fr.aop.TraceAuthentification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class AuthFailure extends SimpleUrlAuthenticationFailureHandler
{
	
	/**
	 * Class logger.
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(TraceAuthentification.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
										final AuthenticationException exception) throws IOException, ServletException
	{
		
		this.LOGGER.info("Failed to log user :-(");
		
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
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
	
}