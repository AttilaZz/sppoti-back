package com.fr.commons.dto;

import java.util.Date;

/**
 * Created by djenanewail on 3/12/17.
 */
public class RatingDTO extends AbstractCommonDTO
{
	
	//set automatically using the connected user.
	private String sppoterRaterId;
	private String sppoterRatedId;
	private String sppotiId;
	private Date ratingDate;
	private Integer stars;
	
	public String getSppoterRaterId() {
		return this.sppoterRaterId;
	}
	
	public void setSppoterRaterId(final String sppoterRaterId) {
		this.sppoterRaterId = sppoterRaterId;
	}
	
	public String getSppoterRatedId() {
		return this.sppoterRatedId;
	}
	
	public void setSppoterRatedId(final String sppoterRatedId) {
		this.sppoterRatedId = sppoterRatedId;
	}
	
	public String getSppotiId() {
		return this.sppotiId;
	}
	
	public void setSppotiId(final String sppotiId) {
		this.sppotiId = sppotiId;
	}
	
	public Integer getStars() {
		return this.stars;
	}
	
	public void setStars(final Integer stars) {
		this.stars = stars;
	}
	
	public Date getRatingDate() {
		return this.ratingDate;
	}
	
	public void setRatingDate(final Date ratingDate) {
		this.ratingDate = ratingDate;
	}
}
