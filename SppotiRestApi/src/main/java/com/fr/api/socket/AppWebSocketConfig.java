/**
 *
 */
package com.fr.api.socket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * Created by: Wail DJENANE on Nov 13, 2016
 */
@Configuration
@EnableWebSocketMessageBroker
public class AppWebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer
{
	
	@Value("${spring.app.originBack}")
	private String backOrigin;
	
	@Value("${spring.app.appName}")
	private String applicationName;
	
	@Value("${spring.app.originFront}")
	private String originFront;
	
	@Value("${spring.app.socket.endpoint}")
	private String socketEndPoint;
	
	@Value("${spring.app.socket.version}")
	private String socketEndPointVersion;
	
	@Value("${server.contextPath}")
	private String contextPath;
	
	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer()
	{
		final ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(8192);
		container.setMaxBinaryMessageBufferSize(8192);
		return container;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configureMessageBroker(final MessageBrokerRegistry config)
	{
		config.enableSimpleBroker("/queue", "/topic");
		config.setApplicationDestinationPrefixes("/app", "/trade");
		config.setUserDestinationPrefix("/user");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStompEndpoints(final StompEndpointRegistry registry)
	{
		//		registry.addEndpoint("/add").setAllowedOrigins(Origins.getValue()).withSockJS()
		//				.setClientLibraryUrl(this.backOrigin + this.applicationName + "/assets/js/sockjs-client.js");
		final String[] origins = this.originFront.split(",");
		
		final String endPoint = this.socketEndPoint;
		registry.addEndpoint(endPoint).setAllowedOrigins(origins).withSockJS();
	}
}
