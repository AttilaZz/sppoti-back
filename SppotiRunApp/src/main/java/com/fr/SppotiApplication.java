package com.fr;

import com.fr.filter.CsrfProperties;
import com.fr.versionning.ApiVersionRequestMappingHandlerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by djenanewail on 1/22/17.
 */
@SpringBootApplication
@PropertySources({
		@PropertySource(value = "classpath:pagination.properties", ignoreResourceNotFound = true, encoding = "UTF-8"),
		@PropertySource(value = "classpath:headerConfig.properties", ignoreResourceNotFound = true, encoding = "UTF-8"),
		@PropertySource(value = "classpath:email/email_fr.properties", ignoreResourceNotFound = true,
				encoding = "UTF-8"),
		@PropertySource(value = "classpath:email/email_en.properties", ignoreResourceNotFound = true,
				encoding = "UTF-8")
})
@EnableRedisHttpSession(redisNamespace = "sppoti", maxInactiveIntervalInSeconds = -1,
		redisFlushMode = RedisFlushMode.IMMEDIATE)
public class SppotiApplication extends WebMvcConfigurationSupport implements ServletContextInitializer
{
	
	/** CSRF properties. */
	@Autowired
	private CsrfProperties filterProperties;
	
	/** Main method to launch app. */
	public static void main(final String[] args) throws Exception
	{
		SpringApplication.run(SppotiApplication.class, args);
	}
	
	/** Init Spring security password encoder. */
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException
	{
		servletContext.getSessionCookieConfig().setDomain(this.filterProperties.getDomain());
		servletContext.getSessionCookieConfig().setSecure(this.filterProperties.isSecureConnexion());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		return new ApiVersionRequestMappingHandlerMapping("v");
	}
	
	@Bean
	public LettuceConnectionFactory connectionFactory() {
		return new LettuceConnectionFactory();
	}
	
	@Bean
	public HttpSessionStrategy httpSessionStrategy() {
		return new HeaderHttpSessionStrategy();
	}
}