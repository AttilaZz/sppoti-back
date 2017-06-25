package com.fr.commons.enumeration;

/**
 * Created by djenanewail on 6/25/17.
 */
public enum ErrorMessageEnum
{
	USERNAME_ALREADY_EXISTS("Username already exists"), EMAIL_ALREADY_EXISTS("Email already exists"),
	EMAIL_ALREADY_ASSOCIATED_WITH_AN_ACCOUNT("Email is already associated with an unconfirmed account");
	
	private final String message;
	
	ErrorMessageEnum(final String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
