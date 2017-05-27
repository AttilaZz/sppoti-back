package com.fr.commons.dto.message;

import com.fr.commons.dto.AbstractCommonDTO;

/**
 * Created by djenanewail on 5/25/17.
 */
public class MessageDTO extends AbstractCommonDTO
{
	private String datetime;
	private String object;
	private String content;
	
	public String getDatetime()
	{
		return this.datetime;
	}
	
	public void setDatetime(final String datetime)
	{
		this.datetime = datetime;
	}
	
	public String getObject()
	{
		return this.object;
	}
	
	public void setObject(final String object)
	{
		this.object = object;
	}
	
	public String getContent()
	{
		return this.content;
	}
	
	public void setContent(final String content)
	{
		this.content = content;
	}
}
