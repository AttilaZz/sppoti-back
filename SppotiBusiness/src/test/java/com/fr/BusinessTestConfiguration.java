package com.fr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by djenanewail on 9/17/17.
 */
@SpringBootApplication
@PropertySources({
		@PropertySource(value = "classpath:pagination.properties", ignoreResourceNotFound = true, encoding = "UTF-8"),
		@PropertySource(value = "classpath:headerConfig.properties", ignoreResourceNotFound = true, encoding = "UTF-8"),
		@PropertySource(value = "classpath:email/email_en.properties", ignoreResourceNotFound = true,
				encoding = "UTF-8"),
		@PropertySource(value = "classpath:email/email_fr.properties", ignoreResourceNotFound = true,
				encoding = "UTF-8")
})
//@EnableWebSocketMessageBroker
//@EnableJpaRepositories(basePackages = {"com.fr.repositories", "com.fr.entities"})
public class BusinessTestConfiguration
{
	public static void main(final String[] args) {
		SpringApplication.run(BusinessTestConfiguration.class, args);
	}
	
	/** Init Spring security password encoder. */
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
}
