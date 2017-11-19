package com.fr.commons.dto;

/**
 * Created by djenanewail on 11/19/17.
 */
public class FirebaseDTO extends AbstractCommonDTO
{
	private String userId;
	private String registrationId;
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setUserId(final String userId) {
		this.userId = userId;
	}
	
	public String getRegistrationId() {
		return this.registrationId;
	}
	
	public void setRegistrationId(final String registrationId) {
		this.registrationId = registrationId;
	}
}
