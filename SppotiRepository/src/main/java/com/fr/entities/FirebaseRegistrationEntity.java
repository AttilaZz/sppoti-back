package com.fr.entities;

import javax.persistence.*;

/**
 * Created by djenanewail on 11/19/17.
 */
@Table(name = "FIREBASE_REGISTRATION")
@Entity
public class FirebaseRegistrationEntity extends AbstractCommonEntity
{
	@Column(name = "REGISTRATION_KEY", nullable = false, unique = true)
	private String registrationKey;
	
	@Column(name = "CONNECTED", nullable = false)
	private Boolean deviceConnected = Boolean.FALSE;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = false)
	private UserEntity user;
	
	public String getRegistrationKey() {
		return this.registrationKey;
	}
	
	public void setRegistrationKey(final String registrationKey) {
		this.registrationKey = registrationKey;
	}
	
	public UserEntity getUser() {
		return this.user;
	}
	
	public void setUser(final UserEntity user) {
		this.user = user;
	}
	
	public Boolean getDeviceConnected() {
		return this.deviceConnected;
	}
	
	public void setDeviceConnected(final Boolean deviceConnected) {
		this.deviceConnected = deviceConnected;
	}
}