package com.fr.entities;

import javax.persistence.*;

/**
 * Created by djenanewail on 11/19/17.
 */
@Entity
@Table(name = "FIREBASE_KEY")
public class FirebaseRegistrationEntity extends AbstractCommonEntity
{
	@Column(name = "REGISTRATION_KEY", nullable = false, unique = true)
	private String key;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = false)
	private UserEntity user;
	
	public String getKey() {
		return this.key;
	}
	
	public void setKey(final String key) {
		this.key = key;
	}
	
	public UserEntity getUser() {
		return this.user;
	}
	
	public void setUser(final UserEntity user) {
		this.user = user;
	}
}
