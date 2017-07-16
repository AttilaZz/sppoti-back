package com.fr.session;

import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created by djenanewail on 7/7/17.
 */
@EnableRedisHttpSession(redisNamespace = "sppoti", maxInactiveIntervalInSeconds = -1,
		redisFlushMode = RedisFlushMode.IMMEDIATE)
public class HttpSessionConfig
{
	//	@Bean
	//	public LettuceConnectionFactory connectionFactory() {
	//		return new LettuceConnectionFactory();
	//	}
	
}
