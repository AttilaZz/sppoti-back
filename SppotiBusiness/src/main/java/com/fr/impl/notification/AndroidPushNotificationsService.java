package com.fr.impl.notification;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * Created by djenanewail on 10/9/17.
 */
@Service
public class AndroidPushNotificationsService
{
	private static final String FIREBASE_SERVER_KEY
			= "AAAAeuyMDEQ:APA91bHXO-6U-9UlFu--_itD1i54YZh5DTKtRj2tZpEa0POLgXs7gheX-zxK_itM-pSZijFwK5S8DQSem4IxNzBrxiNW44T3X4DTIygTWLz1XWTGflD0WsB45svexizshmEpC5ne3IW5";
	private static final String FIREBASE_API_URL = "https://gcm-http.googleapis.com/gcm/send";
	
	@Async
	public CompletableFuture<String> send(final HttpEntity<String> entity) {
		
		final RestTemplate restTemplate = new RestTemplate();
		
		/**
		 https://fcm.googleapis.com/fcm/send
		 Content-Type:application/json
		 Authorization:key=FIREBASE_SERVER_KEY*/
		
		final ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
		interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
		restTemplate.setInterceptors(interceptors);
		
		final String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);
		
		return CompletableFuture.completedFuture(firebaseResponse);
	}
}
