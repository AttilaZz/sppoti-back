package com.fr.commons.exception;

/**
 * Created by djenanewail on 2/23/17.
 */
public class NotAdminException extends RuntimeException
{
	
	public NotAdminException()
	{
		super();
	}
	
	public NotAdminException(final String message)
	{
		super(message);
	}
}
