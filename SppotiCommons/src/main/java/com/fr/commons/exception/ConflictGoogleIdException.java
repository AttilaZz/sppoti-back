package com.fr.commons.exception;

/**
 * Created by djenanewail on 8/27/17.
 */
public class ConflictGoogleIdException extends RuntimeException
{
	
	public ConflictGoogleIdException() {
		super();
	}
	
	public ConflictGoogleIdException(final String message) {
		super(message);
	}
}
