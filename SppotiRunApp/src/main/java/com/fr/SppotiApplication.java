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
		@PropertySource(value = "classpath:pagination.properties", ignoreResourceNotFound = true, encoding = "UTF-8"),
		@PropertySource(value = "classpath:headerConfig.properties", ignoreResourceNotFound = true, encoding = "UTF-8"),
		@PropertySource(value = "classpath:email/email_fr.properties", ignoreResourceNotFound = true,
				encoding = "UTF-8"),
		@PropertySource(value = "classpath:email/email_en.properties", ignoreResourceNotFound = true,
				encoding = "UTF-8")
})
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
	
	//
	//	/**
	//	 * {@inheritDoc}
	//	 */
	//	@Override
	//	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application)
	//	{
	//		return application.sources(SppotiApplication.class);
	//	}
	
	//	/**
	//	 * CROSS origin configuration
	//	 */
	//	@Bean
	//	public WebMvcConfigurer corsConfigurer()
	//	{
	//		return new WebMvcConfigurerAdapter()
	//		{
	//			@Override
	//			public void addCorsMappings(final CorsRegistry registry)
	//			{
	//				registry.addMapping("/**").allowedOrigins(Origins.getValue().split(",")).allowCredentials(true)
	//						.allowedHeaders(Allowed_Headers.getValue()).allowedMethods(AllMethods.getValue());
	//			}
	//		};
	//	}
}