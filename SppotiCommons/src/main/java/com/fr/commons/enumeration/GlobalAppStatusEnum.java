package com.fr.commons.enumeration;

/**
 * Created by djenanewail on 12/26/16.
 */
public enum GlobalAppStatusEnum
{
	
	PUBLIC_RELATION(1), PENDING_SENT(2), PENDING(3), CONFIRMED(4), REFUSED(5), NO_CHALLENGE_YET(6), DELETED(9);
	private final int status;
	
	GlobalAppStatusEnum(final int status)
	{
		this.status = status;
	}
	
	public int getValue()
	{
		return this.status;
	}
	
}
