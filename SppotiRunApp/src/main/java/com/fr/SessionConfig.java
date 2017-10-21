package com.fr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created by djenanewail on 10/4/17.
 */

@EnableRedisHttpSession(redisNamespace = "sppoti", maxInactiveIntervalInSeconds = -1,
		redisFlushMode = RedisFlushMode.IMMEDIATE)
public class SessionConfig
{
	@Value("${spring.redis.host}")
	private String redisHostName;
	
	@Bean
	public LettuceConnectionFactory connectionFactory() {
		final LettuceConnectionFactory connection = new LettuceConnectionFactory();
		connection.setHostName(this.redisHostName);
		return connection;
	}
	
	//	@Bean
	//	public HttpSessionStrategy httpSessionStrategy() {
	//		return new HeaderHttpSessionStrategy();
	//	}
}
