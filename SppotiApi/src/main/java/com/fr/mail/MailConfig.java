/**
 * 
 */
package com.fr.mail;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Created by: Wail DJENANE on Aug 7, 2016
 */
@Configuration
public class MailConfig {

	@Autowired
	private Environment environment;

	@Value("${email.from}")
	private String from;

	@Value("${email.to}")
	private String to;

	@Value("${email.subject}")
	private String subject;

	@Bean
	public JavaMailSender javaMailService() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setJavaMailProperties(javaMailProperties());

		javaMailSender.setUsername(javaMailProperties().getProperty("mail.smtp.user"));
		javaMailSender.setPassword(javaMailProperties().getProperty("mail.smtp.pass"));

		return javaMailSender;
	}

	private Properties javaMailProperties() {
		Properties properties = new Properties();
		properties.put("mail.transport.protocol", environment.getRequiredProperty("mail.transport.protocol"));
		properties.put("mail.smtp.host", environment.getRequiredProperty("mail.smtp.host"));
		properties.put("mail.smtp.port", environment.getRequiredProperty("mail.smtp.port"));
		properties.put("mail.smtp.auth", environment.getRequiredProperty("mail.smtp.auth"));
		properties.put("mail.smtp.user", environment.getRequiredProperty("mail.smtp.user"));
		properties.put("mail.smtp.pass", environment.getRequiredProperty("mail.smtp.pass"));
		properties.put("mail.smtp.starttls.enable", environment.getRequiredProperty("mail.smtp.starttls.enable"));
		properties.put("mail.debug", environment.getRequiredProperty("mail.debug"));

		return properties;
	}

	@Bean
	public SimpleMailMessage simpleMailMessage() {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(from);
		simpleMailMessage.setTo(to);
		simpleMailMessage.setSubject(subject);
		return simpleMailMessage;
	}
}