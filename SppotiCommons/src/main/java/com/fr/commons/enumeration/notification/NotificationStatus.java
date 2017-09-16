package com.fr.commons.enumeration.notification;

/**
 * Created by djenanewail on 6/27/17.
 */
public enum NotificationStatus
{
	READ(7), UNREAD(8);
	
	private final int status;
	
	NotificationStatus(final int status) {
		this.status = status;
	}
	
	public int getValue()
	{
		return this.status;
	}
	
}
