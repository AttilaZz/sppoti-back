package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 * Created by: Wail DJENANE on Jun 15, 2016
 */
public class SignUpDTO extends AbstractCommonDTO
{
	
	@NotEmpty
	private String lastName;
	
	@NotEmpty
	private String firstName;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date dateBorn;
	
	@NotEmpty
	//TODO: check genderType type (MALE, FEMALE) and reject others
	@JsonProperty("gender")
	private String genderType;
	
	@NotEmpty
	@Email
	private String email;
	
	@NotEmpty
	private String password;
	
	@NotEmpty
	private String username;
	
	private Long[] sportId;
	
	@JsonProperty("new_password")
	private String newPassword;
	@JsonProperty("old_password")
	private String oldPassword;
	
	public String getLastName()
	{
		return this.lastName;
	}
	
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}
	
	public String getFirstName()
	{
		return this.firstName;
	}
	
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}
	
	public Date getDateBorn()
	{
		return this.dateBorn;
	}
	
	public void setDateBorn(final Date dateBorn)
	{
		this.dateBorn = dateBorn;
	}
	
	public String getGenderType()
	{
		return this.genderType;
	}
	
	public void setGenderType(final String genderType)
	{
		this.genderType = genderType;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public void setEmail(final String email)
	{
		this.email = email;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public void setPassword(final String password)
	{
		this.password = password;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public void setUsername(final String username)
	{
		this.username = username;
	}
	
	public Long[] getSportId()
	{
		return this.sportId;
	}
	
	public void setSportId(final Long[] sportId)
	{
		this.sportId = sportId;
	}
	
	public String getNewPassword()
	{
		return this.newPassword;
	}
	
	public void setNewPassword(final String newPassword)
	{
		this.newPassword = newPassword;
	}
	
	public String getOldPassword()
	{
		return this.oldPassword;
	}
	
	public void setOldPassword(final String oldPassword)
	{
		this.oldPassword = oldPassword;
	}
}
