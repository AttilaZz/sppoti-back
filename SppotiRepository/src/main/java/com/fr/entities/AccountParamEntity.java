package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by djenanewail on 11/10/17.
 */
@Table(name = "ACCOUNT_PARAM")
@Entity
public class AccountParamEntity extends AbstractCommonEntity
{
	@Column
	private boolean canReceiveEmail;
	
	@Column
	private boolean canReceiveNotification;
	
	@OneToOne(mappedBy = "paramEntity")
	@JsonIgnore
	private UserEntity user;
	
	public AccountParamEntity() {
	}
	
	public AccountParamEntity(final boolean canReceiveEmail) {
		this.canReceiveEmail = canReceiveEmail;
	}
	
	public AccountParamEntity(final boolean canReceiveEmail, final boolean canReceiveNotification,
							  final UserEntity user)
	{
		this.canReceiveEmail = canReceiveEmail;
		this.canReceiveNotification = canReceiveNotification;
		this.user = user;
	}
	
	public boolean isCanReceiveEmail() {
		return this.canReceiveEmail;
	}
	
	public void setCanReceiveEmail(final boolean canReceiveEmail) {
		this.canReceiveEmail = canReceiveEmail;
	}
	
	public boolean isCanReceiveNotification() {
		return this.canReceiveNotification;
	}
	
	public void setCanReceiveNotification(final boolean canReceiveNotification) {
		this.canReceiveNotification = canReceiveNotification;
	}
	
	public UserEntity getUser() {
		return this.user;
	}
	
	public void setUser(final UserEntity user) {
		this.user = user;
	}
}
