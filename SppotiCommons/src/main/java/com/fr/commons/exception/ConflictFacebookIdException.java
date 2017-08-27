package com.fr.commons.exception;

/**
 * Created by djenanewail on 8/27/17.
 */
public class ConflictFacebookIdException extends RuntimeException
{
	public ConflictFacebookIdException() {
		super();
	}
	
	public ConflictFacebookIdException(final String message) {
		super(message);
	}
}
