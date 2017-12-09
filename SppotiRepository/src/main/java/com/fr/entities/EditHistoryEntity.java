package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: Wail DJENANE on Aug 20, 2016
 */

@Entity
@Table(name = "EDIT_HISTORY")
@JsonInclude(Include.NON_EMPTY)
public class EditHistoryEntity extends AbstractCommonEntity
{
	@Column(length = 500)
	private String text;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date datetimeEdited = new Date();
	
	@ManyToOne
	@JoinColumn(name = "post_id")
	@JsonIgnore
	private PostEntity post;
	
	@ManyToOne
	@JoinColumn(name = "comment_id")
	@JsonIgnore
	private CommentEntity comment;
	
	@ManyToOne
	@JoinColumn(name = "sport_id")
	@JsonIgnore
	private SportEntity sport;
	
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
	
	public String getText() {
		return this.text;
	}
	
	public void setText(final String text) {
		this.text = text;
	}
	
	public Date getDatetimeEdited() {
		return this.datetimeEdited;
	}
	
	public void setDatetimeEdited(final Date datetimeEdited) {
		this.datetimeEdited = datetimeEdited;
	}
	
	public SportEntity getSport() {
		return this.sport;
	}
	
	public void setSport(final SportEntity sport) {
		this.sport = sport;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (!(o instanceof EditHistoryEntity))
			return false;
		if (!super.equals(o))
			return false;
		
		final EditHistoryEntity that = (EditHistoryEntity) o;
		
		if (this.text != null ? !this.text.equals(that.text) : that.text != null)
			return false;
		return this.datetimeEdited != null ? this.datetimeEdited.equals(that.datetimeEdited) :
				that.datetimeEdited == null;
		
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (this.text != null ? this.text.hashCode() : 0);
		result = 31 * result + (this.datetimeEdited != null ? this.datetimeEdited.hashCode() : 0);
		return result;
	}
}
