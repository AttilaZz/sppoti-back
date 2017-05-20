package com.fr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
public class SppotiApplication extends SpringBootServletInitializer
{
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application)
	{
		return application.sources(SppotiApplication.class);
	}
	
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
	//				registry.addMapping("/**").allowedOrigins(Origins.getValue()).allowCredentials(true)
	//						.allowedHeaders(Allowed_Headers.getValue()).allowedMethods(AllMethods.getValue());
	//			}
	//		};
	//	}
}