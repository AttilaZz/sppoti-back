package com.fr.commons.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by djenanewail on 4/8/17.
 */
public class ContactDTO extends AbstractCommonDTO
{
	
	@NotEmpty
	private String message;
	@NotEmpty
	private String name;
	
	private String phone;
	@NotEmpty
	private String email;
	@NotEmpty
	private String object;
	
	public String getMessage()
	{
		return this.message;
	}
	
	public void setMessage(final String message)
	{
		this.message = message;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	public String getPhone()
	{
		return this.phone;
	}
	
	public void setPhone(final String phone)
	{
		this.phone = phone;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public void setEmail(final String email)
	{
		this.email = email;
	}
	
	public String getObject()
	{
		return this.object;
	}
	
	public void setObject(final String object)
	{
		this.object = object;
	}
	
}
