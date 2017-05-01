package com.fr.commons.exception;

/**
 * Created by djenanewail on 2/16/17.
 */
public class NoRightToAcceptOrRefuseChallenge extends RuntimeException
{
	
	public NoRightToAcceptOrRefuseChallenge()
	{
		super();
	}
	
	public NoRightToAcceptOrRefuseChallenge(final String message)
	{
		super(message);
	}
}
