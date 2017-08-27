package com.fr.commons.exception;

/**
 * Created by djenanewail on 8/27/17.
 */
public class ConflictTwitterException extends RuntimeException
{
	public ConflictTwitterException() {
		super();
	}
	
	public ConflictTwitterException(final String message) {
		super(message);
	}
}
