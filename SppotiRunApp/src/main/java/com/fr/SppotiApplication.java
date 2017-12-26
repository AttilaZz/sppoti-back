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
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by djenanewail on 1/22/17.
 */
@SpringBootApplication
@PropertySources({
		@PropertySource(value = "classpath:properties/pagination.properties", ignoreResourceNotFound = true,
				encoding = "UTF-8"),
		@PropertySource(value = "classpath:properties/headerConfig.properties", ignoreResourceNotFound = true,
				encoding = "UTF-8"),
		@PropertySource(value = "classpath:properties/emailRedirectionPath.properties", ignoreResourceNotFound = true,
				encoding = "UTF-8")
})
public class SppotiApplication extends WebMvcConfigurationSupport implements ServletContextInitializer
{
	@Autowired
	private CsrfProperties filterProperties;
	
	public static void main(final String[] args) throws Exception
	{
		SpringApplication.run(SppotiApplication.class, args);
	}
	
	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		final ApiVersionRequestMappingHandlerMapping apiVersionRequestMappingHandlerMapping
				= new ApiVersionRequestMappingHandlerMapping("v");
		apiVersionRequestMappingHandlerMapping.setInterceptors(deviceResolverHandlerInterceptor());
		return apiVersionRequestMappingHandlerMapping;
	}
	
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
	
	@Bean
	public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
		return new DeviceResolverHandlerInterceptor();
	}
	
	@Bean
	public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() {
		return new DeviceHandlerMethodArgumentResolver();
	}
}