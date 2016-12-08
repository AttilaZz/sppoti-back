package com.fr.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by: Wail DJENANE on Nov 10, 2016
 */
@Entity
@JsonInclude(Include.NON_EMPTY)
public class Notifications {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id", nullable = true)
	@JsonIgnore
	private Users whoSentNotification;

	@Column(nullable = false, length = 70)
	private Long notifiedUserId;

	@Column(nullable = false)
	private boolean isTag = false;

	@Column(nullable = false)
	private boolean isContentShared = false;

	@Column(nullable = false)
	private boolean isViewed = false;

	@Column(nullable = false)
	private String datetimeCreated = new DateTime().toString();

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "post_id", nullable = true)
	@JsonIgnore
	private Post postTag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Users getWhoSentNotification() {
		return whoSentNotification;
	}

	public void setWhoSentNotification(Users whoSentNotification) {
		this.whoSentNotification = whoSentNotification;
	}

	public Long getNotifiedUserId() {
		return notifiedUserId;
	}

	public void setNotifiedUserId(Long notifiedUserId) {
		this.notifiedUserId = notifiedUserId;
	}

	public boolean isTag() {
		return isTag;
	}

	public void setTag(boolean isTag) {
		this.isTag = isTag;
	}

	public boolean isContentShared() {
		return isContentShared;
	}

	public void setContentShared(boolean isContentShared) {
		this.isContentShared = isContentShared;
	}

	public boolean isViewed() {
		return isViewed;
	}

	public void setViewed(boolean isViewed) {
		this.isViewed = isViewed;
	}

	public Post getPostTag() {
		return postTag;
	}

	public void setPostTag(Post postTag) {
		this.postTag = postTag;
	}

	public String getDatetimeCreated() {
		return datetimeCreated;
	}

	public void setDatetimeCreated(String datetimeCreated) {
		this.datetimeCreated = datetimeCreated;
	}

}
