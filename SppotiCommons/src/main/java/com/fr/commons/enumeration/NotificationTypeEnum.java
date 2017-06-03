package com.fr.commons.enumeration;

/**
 * Created by djenanewail on 2/11/17.
 */
public enum NotificationTypeEnum
{
	
	//FRIEND
	FRIEND_REQUEST_SENT(51), FRIEND_REQUEST_ACCEPTED(52), FRIEND_REQUEST_REFUSED(53),
	
	//COMMENT
	X_COMMENTED_ON_YOUR_POST(21), X_LIKED_YOUR_COMMENT(22), X_TAGGED_YOU_IN_A_COMMENT(22),
	
	//POST
	X_POSTED_ON_YOUR_PROFILE(11), X_LIKED_YOUR_POST(12), X_TAGGED_YOU_IN_A_POST(13),
	
	//TEAM
	X_INVITED_YOU_TO_JOIN_HIS_TEAM(31), X_ACCEPTED_YOUR_TEAM_INVITATION(32), X_REFUSED_YOUR_TEAM_INVITATION(33),
	
	//SPPOTI
	X_INVITED_YOU_TO_JOIN_HIS_SPPOTI(41), X_ACCEPTED_YOUR_SPPOTI_INVITATION(42), X_REFUSED_YOUR_SPPOTI_INVITATION(43),
	
	//Challenge
	SPPOTI_ADMIN_CHELLENGED_YOU(61), SPPOTI_ADMIN_ACCEPTED_YOUR_CHALLENGE(62), SPPOTI_ADMIN_REFUSED_YOUR_CHALLENGE(63),
	ADVERSE_TEAM_CHALLENGED_YOU(64), CHALLENGED_TEAM_REFUSED_YOUR_CHALLENGE(65),
	CHALLENGED_TEAM_ACCEPTED_YOUR_CHALLENGE(66);
	
	private final int notifType;
	
	NotificationTypeEnum(final int notifType)
	{
		this.notifType = notifType;
	}
	
	public int getNotifType()
	{
		return this.notifType;
	}
}
