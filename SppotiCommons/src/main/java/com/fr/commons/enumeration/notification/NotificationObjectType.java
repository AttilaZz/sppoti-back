package com.fr.commons.enumeration.notification;

/**
 * Created by djenanewail on 9/16/17.
 */
public enum NotificationObjectType
{
	TEAM(0), SPPOTI(1), POST(2), COMMENT(3), SCORE(4), RATING(5), FRIENDSHIP(6), LIKE(7), CHALLENGE(8);
	
	private final int positionInNotificationEntity;
	
	NotificationObjectType(final int positionInNotificationEntity) {
		this.positionInNotificationEntity = positionInNotificationEntity;
	}
}
