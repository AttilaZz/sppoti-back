package com.fr.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by: Wail DJENANE on Jul 3, 2016
 */

@Entity
@JsonInclude(Include.NON_EMPTY)
public class FriendShip {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String datetime = new DateTime().toString();

	@Column(nullable = false)
	private boolean isPending = false;

	@Column(nullable = false)
	private boolean isConfirmed = false;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "friend_id")
	@JsonIgnore
	private Users friend;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private Users user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public boolean isPending() {
		return isPending;
	}

	public void setPending(boolean isPending) {
		this.isPending = isPending;
	}

	public boolean isConfirmed() {
		return isConfirmed;
	}

	public void setConfirmed(boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public Users getFriend() {
		return friend;
	}

	public void setFrienId(Users friend) {
		this.friend = friend;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}
