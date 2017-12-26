package com.fr.commons.dto;

/**
 * Created by djenanewail on 12/6/17.
 */
public class EmailUserDTO extends AbstractCommonDTO
{
	private String firstName;
	private String lastName;
	private String email;
	private final String username;
	private final String language;
	
	public EmailUserDTO(final String language, final String username, final String firstName, final String lastName,
						final String email, final String id)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.id = id;
		this.username = username;
		this.language = language;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(final String email) {
		this.email = email;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getLanguage() {
		return this.language;
	}
}
