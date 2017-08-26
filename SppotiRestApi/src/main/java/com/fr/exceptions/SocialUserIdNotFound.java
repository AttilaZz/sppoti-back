package com.fr.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by djenanewail on 8/26/17.
 */
public class SocialUserIdNotFound extends AuthenticationException
{
	public SocialUserIdNotFound(final String message) {
		super(message);
	}
}
