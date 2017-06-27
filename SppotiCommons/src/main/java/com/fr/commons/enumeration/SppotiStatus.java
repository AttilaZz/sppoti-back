package com.fr.commons.enumeration;

/**
 * Created by djenanewail on 6/27/17.
 */
public enum SppotiStatus
{
	PRIVATE(1), PUBLIC(0), IN_PROGRESS(10), DONE(11), DELETED(9);
	
	private final int status;
	
	SppotiStatus(final int status) {
		this.status = status;
	}
	
	public int getValue()
	{
		return this.status;
	}
	
}
