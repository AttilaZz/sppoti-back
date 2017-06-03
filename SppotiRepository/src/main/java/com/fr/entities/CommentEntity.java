package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by: Wail DJENANE on Aug 11, 2016
 */

@Entity
@Table(name = "COMMENT")
@JsonInclude(Include.NON_EMPTY)
public class CommentEntity extends AbstractCommonEntity implements Comparable<CommentEntity>
{
	
	@JsonProperty("text")
	private String content;
	private String imageLink;
	private String videoLink;
	
	private boolean deleted = false;
	
	@Column(nullable = false, columnDefinition = "DATETIME(6)", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date datetimeCreated = new Date();
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private UserEntity user;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "post_id", nullable = false)
	@JsonIgnore
	private PostEntity post;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "comment")
	private Set<LikeContentEntity> likes;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "comment")
	private Set<EditHistoryEntity> editList;
	
	public String getContent() {
		return this.content;
	}
	
	public void setContent(final String content) {
		this.content = content;
	}
	
	public String getImageLink() {
		return this.imageLink;
	}
	
	public void setImageLink(final String imageLink) {
		this.imageLink = imageLink;
	}
	
	public PostEntity getPost() {
		return this.post;
	}
	
	public void setPost(final PostEntity post) {
		this.post = post;
	}
	
	public Date getDatetimeCreated() {
		return this.datetimeCreated;
	}
	
	public void setDatetimeCreated(final Date datetimeCreated) {
		this.datetimeCreated = datetimeCreated;
	}
	
	public UserEntity getUser() {
		return this.user;
	}
	
	public void setUser(final UserEntity user) {
		this.user = user;
	}
	
	public String getVideoLink() {
		return this.videoLink;
	}
	
	public void setVideoLink(final String videoLink) {
		this.videoLink = videoLink;
	}
	
	public Set<LikeContentEntity> getLikes() {
		return this.likes;
	}
	
	public void setLikes(final Set<LikeContentEntity> likes) {
		this.likes = likes;
	}
	
	public Set<EditHistoryEntity> getEditList() {
		return this.editList;
	}
	
	public void setEditList(final Set<EditHistoryEntity> editList) {
		this.editList = editList;
	}
	
	public boolean isDeleted() {
		return this.deleted;
	}
	
	public void setDeleted(final boolean deleted) {
		this.deleted = deleted;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@SuppressWarnings("unused")
	@Override
	public int compareTo(final CommentEntity o) {
		
		return this.datetimeCreated.compareTo(o.datetimeCreated);
		
	}
}
