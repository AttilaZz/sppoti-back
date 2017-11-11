package com.fr.entities;

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
	private UserEntity user;
	
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
