package com.fr.commons.dto;

/**
 * Created by djenanewail on 3/12/17.
 */
public class SppotiRatingDTO extends AbstractCommonDTO
{
	
	//set automatically using the connected user.
	private Integer sppoterRaterId;
	
	private String sppoterRatedId;
	
	private Integer stars;
	
	public Integer getStars()
	{
		return this.stars;
	}
	
	public void setStars(final Integer stars)
	{
		this.stars = stars;
	}
	
	public Integer getSppoterRaterId()
	{
		return this.sppoterRaterId;
	}
	
	public void setSppoterRaterId(final Integer sppoterRaterId)
	{
		this.sppoterRaterId = sppoterRaterId;
	}
	
	public String getSppoterRatedId()
	{
		return this.sppoterRatedId;
	}
	
	public void setSppoterRatedId(final String sppoterRatedId)
	{
		this.sppoterRatedId = sppoterRatedId;
	}
}
