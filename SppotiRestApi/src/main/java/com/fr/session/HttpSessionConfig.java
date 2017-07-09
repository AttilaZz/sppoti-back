package com.fr.session;

import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created by djenanewail on 7/7/17.
 */
@EnableRedisHttpSession(redisNamespace = "sppoti", maxInactiveIntervalInSeconds = -1)
public class HttpSessionConfig
{

}
