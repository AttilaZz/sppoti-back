package com.fr.commons.dto;

import com.fr.commons.enumeration.FcmPluginEnum;
import com.fr.commons.enumeration.notification.NotificationTypeEnum;

import static com.fr.commons.enumeration.FcmPluginEnum.FCM_PLUGIN_ACTIVITY;
import static com.fr.commons.enumeration.notification.NotificationTypeEnum.*;

/**
 * Created by djenanewail on 11/25/17.
 */
public class FirebaseNotificationDTO extends AbstractCommonDTO
{
	private String title;
	private String body;
	private final String sound = "default";
	private FcmPluginEnum pluginActivity = FCM_PLUGIN_ACTIVITY;
	
	public FirebaseNotificationDTO() {
	}
	
	public FirebaseNotificationDTO(final NotificationTypeEnum typeEnum, final UserDTO sender) {
		this.title = sender.getUsername().toUpperCase();
		
		switch (typeEnum) {
			//SCORE
			case SCORE_HAS_BEEN_APPROVED:
				this.body = SCORE_HAS_BEEN_APPROVED.getProperty();
				break;
			case SCORE_HAS_BEEN_REFUSED:
				this.body = SCORE_HAS_BEEN_REFUSED.getProperty();
				break;
			case SCORE_SET_AND_WAITING_FOR_APPROVAL:
				this.body = SCORE_SET_AND_WAITING_FOR_APPROVAL.getProperty();
				break;
			//FRIENDSHIP
			case FRIEND_REQUEST_SENT:
				this.body = FRIEND_REQUEST_SENT.getProperty();
				break;
			case FRIEND_REQUEST_REFUSED:
				this.body = FRIEND_REQUEST_REFUSED.getProperty();
				break;
			case FRIEND_REQUEST_ACCEPTED:
				this.body = FRIEND_REQUEST_ACCEPTED.getProperty();
				break;
			//RATING
			case YOU_HAVE_BEEN_RATED:
				this.body = YOU_HAVE_BEEN_RATED.getProperty();
				break;
			//LIKE
			case X_LIKED_YOUR_COMMENT:
				this.body = X_LIKED_YOUR_COMMENT.getProperty();
				break;
			case X_LIKED_YOUR_POST:
				this.body = X_LIKED_YOUR_POST.getProperty();
				break;
			//POSt & COMMENT
			case X_COMMENTED_ON_YOUR_POST:
				this.body = X_COMMENTED_ON_YOUR_POST.getProperty();
				break;
			case X_POSTED_ON_YOUR_PROFILE:
				this.body = X_POSTED_ON_YOUR_PROFILE.getProperty();
				break;
			//TAG
			case X_TAGGED_YOU_IN_A_POST:
				this.body = X_TAGGED_YOU_IN_A_POST.getProperty();
				break;
			case X_TAGGED_YOU_IN_A_COMMENT:
				this.body = X_TAGGED_YOU_IN_A_COMMENT.getProperty();
				break;
			//TEAM
			case X_REFUSED_YOUR_TEAM_INVITATION:
				this.body = X_REFUSED_YOUR_TEAM_INVITATION.getProperty();
				break;
			case X_ACCEPTED_YOUR_TEAM_INVITATION:
				this.body = X_ACCEPTED_YOUR_TEAM_INVITATION.getProperty();
				break;
			case X_INVITED_YOU_TO_JOIN_HIS_TEAM:
				this.body = X_INVITED_YOU_TO_JOIN_HIS_TEAM.getProperty();
				break;
			//SPPOTI
			case SPPOTI_HAS_BEEN_EDITED:
				this.body = SPPOTI_HAS_BEEN_EDITED.getProperty();
				break;
			case X_ACCEPTED_THE_SPPOTI_INVITATION:
				this.body = X_ACCEPTED_THE_SPPOTI_INVITATION.getProperty();
				break;
			case X_INVITED_YOU_TO_JOIN_HIS_SPPOTI:
				this.body = X_INVITED_YOU_TO_JOIN_HIS_SPPOTI.getProperty();
				break;
			case X_REFUSED_YOUR_SPPOTI_INVITATION:
				this.body = X_REFUSED_YOUR_SPPOTI_INVITATION.getProperty();
				break;
			//CHALLENGE
			case SPPOTI_ADMIN_CHALLENGED_YOU:
				this.body = SPPOTI_ADMIN_CHALLENGED_YOU.getProperty();
				break;
			case TEAM_ADMIN_SENT_YOU_A_CHALLENGE:
				this.body = TEAM_ADMIN_SENT_YOU_A_CHALLENGE.getProperty();
				break;
			case TEAM_ADMIN_CANCELED_HIS_CHALLENGE:
				this.body = TEAM_ADMIN_CANCELED_HIS_CHALLENGE.getProperty();
				break;
			case TEAM_ADMIN_REFUSED_YOUR_CHALLENGE:
				this.body = TEAM_ADMIN_REFUSED_YOUR_CHALLENGE.getProperty();
				break;
			case TEAM_ADMIN_ACCEPTED_YOUR_CHALLENGE:
				this.body = TEAM_ADMIN_ACCEPTED_YOUR_CHALLENGE.getProperty();
				break;
			case SPPOTI_ADMIN_ACCEPTED_THE_CHALLENGE:
				this.body = SPPOTI_ADMIN_ACCEPTED_THE_CHALLENGE.getProperty();
				break;
			case SPPOTI_ADMIN_CANCELED_HIS_CHALLENGE:
				this.body = SPPOTI_ADMIN_CANCELED_HIS_CHALLENGE.getProperty();
				break;
			case SPPOTI_ADMIN_REFUSED_YOUR_CHALLENGE:
				this.body = SPPOTI_ADMIN_REFUSED_YOUR_CHALLENGE.getProperty();
				break;
		}
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(final String title) {
		this.title = title;
	}
	
	public String getBody() {
		return this.body;
	}
	
	public void setBody(final String body) {
		this.body = body;
	}
	
	public String getSound() {
		return this.sound;
	}
	
	public FcmPluginEnum getPluginActivity() {
		return this.pluginActivity;
	}
	
	public void setPluginActivity(final FcmPluginEnum pluginActivity) {
		this.pluginActivity = pluginActivity;
	}
}
