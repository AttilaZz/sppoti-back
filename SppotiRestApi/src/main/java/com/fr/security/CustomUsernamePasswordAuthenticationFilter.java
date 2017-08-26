package com.fr.security;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by djenanewail on 8/25/17.
 */
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
	// ~ Static fields/initializers
	// =====================================================================================
	
	private static final String SPRING_SECURITY_FORM_FACEBOOK_KEY = "facebook";
	private static final String SPRING_SECURITY_FORM_GOOGLE_KEY = "google";
	private static final String SPRING_SECURITY_FORM_TWITTER_KEY = "twitter";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String obtainUsername(final HttpServletRequest request) {
		final String username = super.obtainUsername(request);
		final String facebookId = request.getParameter(SPRING_SECURITY_FORM_FACEBOOK_KEY);
		final String googleId = request.getParameter(SPRING_SECURITY_FORM_GOOGLE_KEY);
		final String twitterId = request.getParameter(SPRING_SECURITY_FORM_TWITTER_KEY);
		
		return String.join(",", username, facebookId, googleId, twitterId);
	}
}