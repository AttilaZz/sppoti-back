package com.fr.commons.enumeration;

/**
 * Created by djenanewail on 6/25/17.
 */
public enum ErrorMessageEnum
{
	USERNAME_ALREADY_EXISTS("Username already exists"), EMAIL_ALREADY_EXISTS("Email already exists"),
	EMAIL_ALREADY_ASSOCIATED_WITH_AN_ACCOUNT("Email is already associated with an unconfirmed account"),
	SPPOTI_NOT_FOUND("Sppoti not found"), TEAM_NOT_FOUND("Team not found"),
	FACEBOOK_ID_ALREADY_EXISTS("Facebook id already associated with an account"),
	GOOGLE_ID_ALREADY_EXISTS("Google id id already associated with an account"),
	TWITTER_ID_ALREADY_EXISTS("Twitter id already associated with an account"),
	SOCIAL_USER_ID_NOT_FOUND("Social ID not found"), PASSWORD_REQUIRED("Password is required"),
	SERIALIZE_FIREBASE_NOTIF_MESSAGE_FAILURE("Cannot serialize firebase notification object");
	
	private final String message;
	
	ErrorMessageEnum(final String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
