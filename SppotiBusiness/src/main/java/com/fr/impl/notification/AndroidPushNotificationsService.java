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
	private static final String FIREBASE_SERVER_KEY = "AIzaSyCdipI1ajRjO8ZN2Ukz4YTaRAWhM4F5pTc";
	private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
	
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
