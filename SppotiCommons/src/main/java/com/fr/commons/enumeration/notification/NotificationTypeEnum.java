package com.fr.commons.enumeration.notification;

import com.fasterxml.jackson.annotation.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by djenanewail on 2/11/17.
 */
public enum NotificationTypeEnum
{
	//FRIEND
	FRIEND_REQUEST_SENT, FRIEND_REQUEST_ACCEPTED, FRIEND_REQUEST_REFUSED,
	
	//COMMENT
	X_COMMENTED_ON_YOUR_POST, X_LIKED_YOUR_COMMENT, X_TAGGED_YOU_IN_A_COMMENT,
	
	//POST
	X_POSTED_ON_YOUR_PROFILE, X_LIKED_YOUR_POST, X_TAGGED_YOU_IN_A_POST,
	
	//TEAM
	X_INVITED_YOU_TO_JOIN_HIS_TEAM, X_ACCEPTED_YOUR_TEAM_INVITATION, X_REFUSED_YOUR_TEAM_INVITATION,
	
	//SPPOTI
	X_INVITED_YOU_TO_JOIN_HIS_SPPOTI, X_ACCEPTED_THE_SPPOTI_INVITATION, X_REFUSED_YOUR_SPPOTI_INVITATION,
	SPPOTI_HAS_BEEN_EDITED, X_ASKED_TO_JOIN_SPPOTI,
	
	//Challenge
	SPPOTI_ADMIN_CHALLENGED_YOU, SPPOTI_ADMIN_ACCEPTED_THE_CHALLENGE, SPPOTI_ADMIN_REFUSED_YOUR_CHALLENGE,
	SPPOTI_ADMIN_CANCELED_HIS_CHALLENGE, TEAM_ADMIN_SENT_YOU_A_CHALLENGE, TEAM_ADMIN_REFUSED_YOUR_CHALLENGE,
	TEAM_ADMIN_ACCEPTED_YOUR_CHALLENGE, TEAM_ADMIN_CANCELED_HIS_CHALLENGE,
	
	//Score
	SCORE_SET_AND_WAITING_FOR_APPROVAL, SCORE_HAS_BEEN_APPROVED, SCORE_HAS_BEEN_REFUSED,
	
	//RATING
	YOU_HAVE_BEEN_RATED;
	
	private static final String PATH = "/properties/firebase-notification.properties";
	private static Properties properties;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationTypeEnum.class);
	
	private Integer type;
	private String property;
	
	private void init()
	{
		if (properties == null) {
			properties = new Properties();
			try {
				LOGGER.info("Reading property from {}", PATH);
				properties.load(NotificationTypeEnum.class.getResourceAsStream(PATH));
			} catch (final Exception e) {
				LOGGER.info("Unable to load " + PATH + " file from classpath.", e);
				System.exit(1);
			}
		}
		final String value = (String) properties.get(this.toString());
		final String[] fields = value.split(",");
		this.type = Integer.valueOf(fields[0]);
		this.property = fields[1];
		LOGGER.info("Notif type properties has been populated with type:{} and property: {}", this.type, this.property);
	}
	
	public String getProperty()
	{
		if (this.property == null) {
			init();
		}
		return this.property;
	}
	
	@JsonValue
	public Integer getType()
	{
		if (this.type == null) {
			init();
		}
		return this.type;
	}
	
	public boolean isTeamNotification() {
		return this.equals(X_INVITED_YOU_TO_JOIN_HIS_TEAM) || this.equals(X_ACCEPTED_YOUR_TEAM_INVITATION) ||
				this.equals(X_REFUSED_YOUR_TEAM_INVITATION);
	}
	
	public boolean isSppotiNotification() {
		return this.equals(X_INVITED_YOU_TO_JOIN_HIS_SPPOTI) || this.equals(X_ACCEPTED_THE_SPPOTI_INVITATION) ||
				this.equals(X_REFUSED_YOUR_SPPOTI_INVITATION) || this.equals(SPPOTI_HAS_BEEN_EDITED);
	}
	
	public boolean isRatingNotification() {
		return this.equals(YOU_HAVE_BEEN_RATED);
	}
	
	public boolean isChallengeNotification() {
		return this.equals(SPPOTI_ADMIN_CHALLENGED_YOU) || this.equals(SPPOTI_ADMIN_ACCEPTED_THE_CHALLENGE) ||
				this.equals(SPPOTI_ADMIN_REFUSED_YOUR_CHALLENGE) || this.equals(SPPOTI_ADMIN_CANCELED_HIS_CHALLENGE) ||
				this.equals(TEAM_ADMIN_SENT_YOU_A_CHALLENGE) || this.equals(TEAM_ADMIN_REFUSED_YOUR_CHALLENGE) ||
				this.equals(TEAM_ADMIN_ACCEPTED_YOUR_CHALLENGE) || this.equals(TEAM_ADMIN_CANCELED_HIS_CHALLENGE);
	}
	
	/**
	 * FRIENDSHIP
	 */
	public boolean isFriendNotification() {
		return this.equals(FRIEND_REQUEST_SENT) || this.equals(FRIEND_REQUEST_ACCEPTED) ||
				this.equals(FRIEND_REQUEST_REFUSED);
	}
	
	public boolean isFriendRequestSent() {
		return this.equals(FRIEND_REQUEST_SENT);
	}
	
	public boolean isFriendRequestAccepted() {
		return this.equals(FRIEND_REQUEST_SENT);
	}
	
	public boolean isFriendRequestRefused() {
		return this.equals(FRIEND_REQUEST_SENT);
	}
	
	/**
	 * POST
	 */
	public boolean isPostNotification() {
		return this.equals(X_POSTED_ON_YOUR_PROFILE) || this.equals(X_LIKED_YOUR_POST) ||
				this.equals(X_TAGGED_YOU_IN_A_POST);
	}
	
	public boolean isSomeOnePostedOnProfile() {
		return this.equals(X_POSTED_ON_YOUR_PROFILE);
	}
	
	public boolean isSomeOnePostedOnYourProfile() {
		return this.equals(X_POSTED_ON_YOUR_PROFILE);
	}
	
	public boolean isSomeOneTaggedYouOnHisPost() {
		return this.equals(X_POSTED_ON_YOUR_PROFILE);
	}
	
	/**
	 * COMMENT
	 */
	public boolean isCommentNotification() {
		return this.equals(X_COMMENTED_ON_YOUR_POST) || this.equals(X_LIKED_YOUR_COMMENT) ||
				this.equals(X_TAGGED_YOU_IN_A_COMMENT);
	}
	
	public boolean isSomeOneCommentedOnProfile() {
		return this.equals(X_POSTED_ON_YOUR_PROFILE);
	}
	
	public boolean isSomeOneCommentedOnYourProfile() {
		return this.equals(X_POSTED_ON_YOUR_PROFILE);
	}
	
	public boolean isSomeOneTaggedYouOnHisComment() {
		return this.equals(X_POSTED_ON_YOUR_PROFILE);
	}
	
	/**
	 * SCORE
	 */
	public boolean isScoreNotification() {
		return this.equals(SCORE_SET_AND_WAITING_FOR_APPROVAL) || this.equals(SCORE_HAS_BEEN_APPROVED) ||
				this.equals(SCORE_HAS_BEEN_REFUSED);
	}
	
	public boolean isScoreHasBeenApproved() {
		return this.equals(SCORE_HAS_BEEN_APPROVED);
	}
	
	public boolean isScoreHasBeenRefused() {
		return this.equals(SCORE_HAS_BEEN_REFUSED);
	}
}
