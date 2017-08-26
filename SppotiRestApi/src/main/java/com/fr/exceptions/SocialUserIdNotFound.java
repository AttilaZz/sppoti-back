package com.fr.exceptions;

import javax.persistence.EntityNotFoundException;

/**
 * Created by djenanewail on 8/26/17.
 */
public class SocialUserIdNotFound extends EntityNotFoundException
{
	public SocialUserIdNotFound(final String message) {
		super(message);
	}
}
