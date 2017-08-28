package com.fr.security;

import com.fr.commons.utils.SppotiUtils;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

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
		final String username = buildParameter(super.obtainUsername(request));
		final String facebookId = buildParameter(request.getParameter(SPRING_SECURITY_FORM_FACEBOOK_KEY));
		final String googleId = buildParameter(request.getParameter(SPRING_SECURITY_FORM_GOOGLE_KEY));
		final String twitterId = buildParameter(request.getParameter(SPRING_SECURITY_FORM_TWITTER_KEY));
		
		return String.join(",", username, facebookId, googleId, twitterId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String obtainPassword(final HttpServletRequest request) {
		return !StringUtils.isEmpty(super.obtainPassword(request)) ? SppotiUtils.decode64ToString(super.obtainPassword(request)): null;
	}
	
	private String buildParameter(final String param) {
		if (!StringUtils.isEmpty(param) && !"undefined".equals(param)) {
			return param.trim();
		}
		return null;
	}
}