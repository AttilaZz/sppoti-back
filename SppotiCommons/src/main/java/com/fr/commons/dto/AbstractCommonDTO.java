package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by djenanewail on 2/24/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbstractCommonDTO implements Serializable
{
	
	protected Integer version;
	
	protected String id;
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
	
	public Integer getVersion()
	{
		return this.version;
	}
	
	public void setVersion(final Integer version)
	{
		this.version = version;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(final String id) {
		this.id = id;
	}
}
