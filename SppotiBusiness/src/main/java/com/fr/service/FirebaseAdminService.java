package com.fr.service;

/**
 * Created by djenanewail on 11/28/17.
 */
public interface FirebaseAdminService extends AbstractBusinessService
{
	/**
	 * /**
	 * If the provided ID token has the correct format,
	 * is not expired, and is properly signed, the method returns the decoded ID token.
	 */
	String verifyUserFirebaseToken(String idToken);
}
