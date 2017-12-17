package com.fr.commons.dto;

/**
 * Created by djenanewail on 12/6/17.
 */
public class EmailUserDTO extends AbstractCommonDTO
{
	private String firstName;
	private String lastName;
	private String email;
	
	public EmailUserDTO() {
	}
	
	public EmailUserDTO(final String firstName, final String lastName, final String email, final String id) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.id = id;
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
}
