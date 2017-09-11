package com.fr.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by djenanewail on 3/12/17.
 */
@Entity
@Table(name = "rating")
public class RatingEntity extends AbstractCommonEntity
{
	
	@ManyToOne
	@JoinColumn(name = "sppoti_id", nullable = false)
	private SppotiEntity sppotiEntity;
	
	@ManyToOne
	@JoinColumn(name = "rated_sppoter", nullable = false)
	private UserEntity ratedSppoter;
	
	@ManyToOne
	@JoinColumn(name = "rater_sppoter", nullable = false)
	private UserEntity raterSppoter;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date ratingDate;
	
	@Column(nullable = false)
	private Integer stars;
	
	/**
	 * to get trace of the connected user when using transformers.
	 */
	private transient Long connectedUserId;
	
	public SppotiEntity getSppotiEntity() {
		return this.sppotiEntity;
	}
	
	public void setSppotiEntity(final SppotiEntity sppotiEntity) {
		this.sppotiEntity = sppotiEntity;
	}
	
	public UserEntity getRatedSppoter() {
		return this.ratedSppoter;
	}
	
	public void setRatedSppoter(final UserEntity ratedSppoter) {
		this.ratedSppoter = ratedSppoter;
	}
	
	public UserEntity getRaterSppoter() {
		return this.raterSppoter;
	}
	
	public void setRaterSppoter(final UserEntity raterSppoter) {
		this.raterSppoter = raterSppoter;
	}
	
	public Date getRatingDate() {
		return this.ratingDate;
	}
	
	public void setRatingDate(final Date ratingDate) {
		this.ratingDate = ratingDate;
	}
	
	public Integer getStars() {
		return this.stars;
	}
	
	public void setStars(final Integer stars) {
		this.stars = stars;
	}
	
	public Long getConnectedUserId() {
		return this.connectedUserId;
	}
	
	public void setConnectedUserId(final Long connectedUserId) {
		this.connectedUserId = connectedUserId;
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
		
		final RatingEntity that = (RatingEntity) o;
		
		if (this.ratingDate != null ? !this.ratingDate.equals(that.ratingDate) : that.ratingDate != null)
			return false;
		return this.stars != null ? this.stars.equals(that.stars) : that.stars == null;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (this.ratingDate != null ? this.ratingDate.hashCode() : 0);
		result = 31 * result + (this.stars != null ? this.stars.hashCode() : 0);
		return result;
	}
}
