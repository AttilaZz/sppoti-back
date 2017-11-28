package com.fr.impl;

import com.fr.service.FirebaseAdminService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.auth.FirebaseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by djenanewail on 11/28/17.
 */
@Component
public class FirebaseAdminServiceImpl extends CommonControllerServiceImpl implements FirebaseAdminService
{
	private final Logger LOGGER = LoggerFactory.getLogger(FirebaseAdminServiceImpl.class);
	
	private static final String DATABASE_URL = "https://sppoti-2017baw.firebaseio.com";
	private static final String PATH_TO_SERVICE_ACCOUNT_KEY_JSON = "/firebase/sppotiServiceAccountKey.json";
	
	public FirebaseAdminServiceImpl() {
		initFirebaseAppInstance();
	}
	
	@Override
	public String verifyUserFirebaseToken(final String idToken) {
		try {
			final FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdTokenAsync(idToken).get();
			return decodedToken.getUid();
		} catch (InterruptedException | ExecutionException e) {
			this.LOGGER.info("Error contacting firebase ", e);
			return null;
		}
	}
	
	private FirebaseApp initFirebaseAppInstance() {
		try {
			final InputStream serviceAccount = getClass().getResourceAsStream(PATH_TO_SERVICE_ACCOUNT_KEY_JSON);
			final FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredential(FirebaseCredentials.fromCertificate(serviceAccount)).setDatabaseUrl(DATABASE_URL)
					.build();
			
			return FirebaseApp.initializeApp(options);
			
		} catch (final java.io.IOException e) {
			this.LOGGER.info("Failed to initialize firebase SDK ", e);
			return null;
		}
	}
}
