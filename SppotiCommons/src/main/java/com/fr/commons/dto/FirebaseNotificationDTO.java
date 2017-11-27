package com.fr.commons.dto;

import com.fr.commons.dto.notification.NotificationDTO;
import com.fr.commons.enumeration.FcmPluginEnum;

import static com.fr.commons.enumeration.FcmPluginEnum.FCM_PLUGIN_ACTIVITY;

/**
 * Created by djenanewail on 11/25/17.
 */
public class FirebaseNotificationDTO
{
	private String title;
	private String body;
	private final String sound = "default";
	private FcmPluginEnum pluginActivity = FCM_PLUGIN_ACTIVITY;
	
	public FirebaseNotificationDTO() {
	}
	
	public FirebaseNotificationDTO(final NotificationDTO dto) {
		this.title = "SPPOTI";
		this.body = "You received a new notification";
	}
	
	public FirebaseNotificationDTO(final String title, final String body) {
		this.title = title;
		this.body = body;
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
