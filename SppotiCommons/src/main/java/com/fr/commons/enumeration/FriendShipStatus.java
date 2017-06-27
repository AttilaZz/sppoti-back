package com.fr.commons.enumeration;

/**
 * Created by djenanewail on 6/27/17.
 */
public enum FriendShipStatus
{
	PUBLIC_RELATION(1), PENDING_SENT(2), PENDING(3), CONFIRMED(4), REFUSED(5);
	
	private final int status;
	
	FriendShipStatus(final int status) {
		this.status = status;
	}
	
	public int getValue()
	{
		return this.status;
	}
	
}
