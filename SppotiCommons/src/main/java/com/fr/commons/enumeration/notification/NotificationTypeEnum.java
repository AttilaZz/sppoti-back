package com.fr.commons.enumeration.notification;

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
	X_INVITED_YOU_TO_JOIN_HIS_SPPOTI(41), X_ACCEPTED_THE_SPPOTI_INVITATION(42), X_REFUSED_YOUR_SPPOTI_INVITATION(43),
	SPPOTI_HAS_BEEN_EDITED(44),
	
	//Challenge
	SPPOTI_ADMIN_CHALLENGED_YOU(61), SPPOTI_ADMIN_ACCEPTED_THE_CHALLENGE(62), SPPOTI_ADMIN_REFUSED_YOUR_CHALLENGE(63),
	SPPOTI_ADMIN_CANCELED_HIS_CHALLENGE(64), TEAM_ADMIN_SENT_YOU_A_CHALLENGE(65), TEAM_ADMIN_REFUSED_YOUR_CHALLENGE(66),
	TEAM_ADMIN_ACCEPTED_YOUR_CHALLENGE(67), TEAM_ADMIN_CANCELED_HIS_CHALLENGE(68),
	
	//Score
	SCORE_SET_AND_WAITING_FOR_APPROVAL(71), SCORE_HAS_BEEN_APPROVED(72), SCORE_HAS_BEEN_REFUSED(73),
	
	//RATING
	YOU_HAVE_BEEN_RATED(81);
	
	private final Integer notifType;
	
	NotificationTypeEnum(final Integer notifType)
	{
		this.notifType = notifType;
	}
	
	public Integer getNotifType()
	{
		return this.notifType;
	}
	
	public boolean isTeamNotification() {
		return this.notifType.equals(X_INVITED_YOU_TO_JOIN_HIS_TEAM.getNotifType()) ||
				this.notifType.equals(X_ACCEPTED_YOUR_TEAM_INVITATION.getNotifType()) ||
				this.notifType.equals(X_REFUSED_YOUR_TEAM_INVITATION.getNotifType());
	}
	
	public boolean isSppotiNotification() {
		return this.notifType.equals(X_INVITED_YOU_TO_JOIN_HIS_SPPOTI.getNotifType()) ||
				this.notifType.equals(X_ACCEPTED_THE_SPPOTI_INVITATION.getNotifType()) ||
				this.notifType.equals(X_REFUSED_YOUR_SPPOTI_INVITATION.getNotifType()) ||
				this.notifType.equals(SPPOTI_HAS_BEEN_EDITED.getNotifType());
	}
	
	public boolean isRatingNotification() {
		return this.notifType.equals(YOU_HAVE_BEEN_RATED.getNotifType());
	}
	
	public boolean isScoreNotification() {
		return this.notifType.equals(SCORE_SET_AND_WAITING_FOR_APPROVAL.getNotifType()) ||
				this.notifType.equals(SCORE_HAS_BEEN_APPROVED.getNotifType()) ||
				this.notifType.equals(SCORE_HAS_BEEN_REFUSED.getNotifType());
	}
	
	public boolean isChallengeNotification() {
		return this.notifType.equals(SPPOTI_ADMIN_CHALLENGED_YOU.getNotifType()) ||
				this.notifType.equals(SPPOTI_ADMIN_ACCEPTED_THE_CHALLENGE.getNotifType()) ||
				this.notifType.equals(SPPOTI_ADMIN_REFUSED_YOUR_CHALLENGE.getNotifType()) ||
				this.notifType.equals(SPPOTI_ADMIN_CANCELED_HIS_CHALLENGE.getNotifType()) ||
				this.notifType.equals(TEAM_ADMIN_SENT_YOU_A_CHALLENGE.getNotifType()) ||
				this.notifType.equals(TEAM_ADMIN_REFUSED_YOUR_CHALLENGE.getNotifType()) ||
				this.notifType.equals(TEAM_ADMIN_ACCEPTED_YOUR_CHALLENGE.getNotifType()) ||
				this.notifType.equals(TEAM_ADMIN_CANCELED_HIS_CHALLENGE.getNotifType());
	}
	
	
	public boolean isPostNotification() {
		return this.notifType.equals(X_POSTED_ON_YOUR_PROFILE.getNotifType()) ||
				this.notifType.equals(X_LIKED_YOUR_POST.getNotifType()) ||
				this.notifType.equals(X_TAGGED_YOU_IN_A_POST.getNotifType());
	}
	
	
	public boolean isCommentNotification() {
		return this.notifType.equals(X_COMMENTED_ON_YOUR_POST.getNotifType()) ||
				this.notifType.equals(X_LIKED_YOUR_COMMENT.getNotifType()) ||
				this.notifType.equals(X_TAGGED_YOU_IN_A_COMMENT.getNotifType());
	}
	
	
	public boolean isFriendNotification() {
		return this.notifType.equals(FRIEND_REQUEST_SENT.getNotifType()) ||
				this.notifType.equals(FRIEND_REQUEST_ACCEPTED.getNotifType()) ||
				this.notifType.equals(FRIEND_REQUEST_REFUSED.getNotifType());
	}
}
