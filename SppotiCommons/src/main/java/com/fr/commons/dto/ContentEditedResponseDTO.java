package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fr.commons.utils.JsonDateSerializer;

import java.util.Date;

/**
 * Created by: Wail DJENANE on Aug 20, 2016
 */
@JsonInclude(Include.NON_DEFAULT)
public class ContentEditedResponseDTO
{
	
	private Long id;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date dateTime = new Date();
	
	private String text;
	
	private double latitude;
	private double longitude;
	
	private Long sportId;
	
	public Long getId()
	{
		return this.id;
	}
	
	public void setId(final Long id)
	{
		this.id = id;
	}
	
	public Date getDateTime()
	{
		return this.dateTime;
	}
	
	public void setDateTime(final Date dateTime)
	{
		this.dateTime = dateTime;
	}
	
	public String getText()
	{
		return this.text;
	}
	
	public void setText(final String text)
	{
		this.text = text;
	}
	
	public double getLatitude()
	{
		return this.latitude;
	}
	
	public void setLatitude(final double latitude)
	{
		this.latitude = latitude;
	}
	
	public double getLongitude()
	{
		return this.longitude;
	}
	
	public void setLongitude(final double longitude)
	{
		this.longitude = longitude;
	}
	
	public Long getSportId()
	{
		return this.sportId;
	}
	
	public void setSportId(final Long sportId)
	{
		this.sportId = sportId;
	}
	
}
