package com.fr.commons.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by djenanewail on 3/26/17.
 */
public class SportDTO
{
	
	private String name;
	private Long id;
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	public Long getId()
	{
		return this.id;
	}
	
	public void setId(final Long id)
	{
		this.id = id;
	}
	
	@Override
	public String toString() {
		final ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (final JsonProcessingException e) {
			return null;
		}
	}
	
	@Override
	public int hashCode() {
		int result = this.name != null ? this.name.hashCode() : 0;
		result = 31 * result + (this.id != null ? this.id.hashCode() : 0);
		return result;
	}
}