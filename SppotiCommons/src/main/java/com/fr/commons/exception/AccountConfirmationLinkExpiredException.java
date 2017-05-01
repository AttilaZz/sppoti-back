package com.fr.commons.exception;

/**
 * Created by djenanewail on 4/9/17.
 */
public class AccountConfirmationLinkExpiredException extends RuntimeException
{
	
	public AccountConfirmationLinkExpiredException()
	{
		super();
	}
	
	public AccountConfirmationLinkExpiredException(final String message)
	{
		super(message);
	}
}
