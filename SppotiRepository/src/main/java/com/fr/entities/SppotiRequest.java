package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fr.commons.enumeration.GlobalAppStatusEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by djenanewail on 7/1/17.
 */
@Entity
@Table(name = "sppoti_request")
public class SppotiRequest extends AbstractCommonEntity
{
	
	@Column(nullable = false, name = "datetime_created")
	@Temporal(TemporalType.TIMESTAMP)
	private final Date datetimeCreated = new Date();
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "sppoti_id")
	private SppotiEntity sppoti;
	
	private GlobalAppStatusEnum status = GlobalAppStatusEnum.PENDING;
	
	public UserEntity getUser() {
		return this.user;
	}
	
	public void setUser(final UserEntity user) {
		this.user = user;
	}
	
	public SppotiEntity getSppoti() {
		return this.sppoti;
	}
	
	public void setSppoti(final SppotiEntity sppoti) {
		this.sppoti = sppoti;
	}
	
	public GlobalAppStatusEnum getStatus() {
		return this.status;
	}
	
	public void setStatus(final GlobalAppStatusEnum status) {
		this.status = status;
	}
	
	public Date getDatetimeCreated() {
		return this.datetimeCreated;
	}
}
