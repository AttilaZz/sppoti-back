package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by: Wail DJENANE on Aug 15, 2016
 */

@Entity
@Table(name = "LIKE_CONTENT")
@JsonInclude(Include.NON_EMPTY)
public class LikeContentEntity extends AbstractCommonEntity
{
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private UserEntity user;
	
	@ManyToOne
	@JoinColumn(name = "post_id")
	@JsonIgnore
	private PostEntity post;
	
	@ManyToOne
	@JoinColumn(name = "comment_id")
	@JsonIgnore
	private CommentEntity comment;
	
	@Column(nullable = false)
	private String datetimeCreated = new DateTime().toString();
	
	public UserEntity getUser() {
		return this.user;
	}
	
	public void setUser(final UserEntity user) {
		this.user = user;
	}
	
	public PostEntity getPost() {
		return this.post;
	}
	
	public void setPost(final PostEntity post) {
		this.post = post;
	}
	
	public CommentEntity getComment() {
		return this.comment;
	}
	
	public void setComment(final CommentEntity comment) {
		this.comment = comment;
	}
	
	public String getDatetimeCreated() {
		return this.datetimeCreated;
	}
	
	public void setDatetimeCreated(final String datetimeCreated) {
		this.datetimeCreated = datetimeCreated;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;
		
		final LikeContentEntity that = (LikeContentEntity) o;
		
		return this.datetimeCreated != null ? this.datetimeCreated.equals(that.datetimeCreated) :
				that.datetimeCreated == null;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (this.datetimeCreated != null ? this.datetimeCreated.hashCode() : 0);
		return result;
	}
}