package com.fr.commons.enumeration;

/**
 * Created by djenanewail on 6/27/17.
 */
public enum TeamStatus
{
	PUBLIC(0), PRIVATE(1);
	
	private final int status;
	
	TeamStatus(final int status) {
		this.status = status;
	}
	
	public int getValue()
	{
		return this.status;
	}
	
}
