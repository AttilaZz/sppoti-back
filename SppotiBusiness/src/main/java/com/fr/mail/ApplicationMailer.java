package com.fr.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by wdjenane on 09/02/2017.
 * <p>
 * Email super class configuration.
 **/
@Component
abstract class ApplicationMailer
{
	
	/** Class logger. */
	protected static Logger LOGGER = LoggerFactory.getLogger(ApplicationMailer.class);
	
	/** Templates. */
	protected static final String PATH_TO_ACCOUNT_TEMPLATE = "account/account";
	protected static final String PATH_TO_CONTACT_TEMPLATE = "contact/contact";
	protected static final String PATH_TO_TEAM_TEMPLATE = "team/team";
	protected static final String PATH_TO_SPPOTI_TEMPLATE = "sppoti/sppoti";
	
	/** Email charset. */
	protected static final String CHARSET_NAME = "UTF-8";
	
	/** Error message. */
	protected static final String ERROR_SENDING_MAIL = "Error sending email";
	private static final String ERROR_OPENING_RESOURCE_FILE = "Resource file not found";
	
	/** Java mail sender. */
	final JavaMailSender sender;
	/** Mail properties. */
	final MailProperties mailProperties;
	/** Template engine. */
	final TemplateEngine templateEngine;
	
	/** Front app path. */
	@Value("${spring.app.originFront}")
	protected String frontRootPath;
	
	/** resource content type. */
	private static final String IMAGE_PNG = "image/png";
	
	/** name resources to use inside templates. */
	protected final String logoResourceName = "logo.png";
	protected final String teamAvatarResourceName = "team_avatar.png";
	protected final String sppotiCoverResourceName = "sppoti_bg.png";
	
	/** Init dependencies. */
	@Autowired
	public ApplicationMailer(final JavaMailSender sender, final MailProperties mailProperties,
							 final TemplateEngine templateEngine)
	{
		this.sender = sender;
		this.mailProperties = mailProperties;
		this.templateEngine = templateEngine;
	}
	
	
	/**
	 * @param to
	 * 		email receiver.
	 * @param subject
	 * 		email subject.
	 * @param content
	 * 		email content.
	 */
	protected void prepareAndSendEmail(final String to, final String subject, final String content,
									   final ResourceContent resourceContent)
	{
		
		final MimeMessage mail = this.sender.createMimeMessage();
		
		try {
			final MimeMessageHelper helper = new MimeMessageHelper(mail, true, CHARSET_NAME);
			
			helper.setFrom(this.mailProperties.getUsername());
			helper.setTo(to);
			helper.setSubject(subject);
			
			helper.setText(content, true);
			
			if (resourceContent != null) {
				// Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
				final ClassLoader classLoader = getClass().getClassLoader();
				final File file = new File(classLoader.getResource(resourceContent.getPath()).getFile());
				
				final byte[] imageBytes = Files.readAllBytes(file.toPath());
				final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
				helper.addInline(resourceContent.getResourceName(), imageSource, IMAGE_PNG);
			}
			
			this.sender.send(mail);
			
		} catch (final MessagingException e) {
			LOGGER.error(ERROR_SENDING_MAIL, e);
		} catch (final IOException e) {
			LOGGER.error(ERROR_OPENING_RESOURCE_FILE, e);
		}
	}
	
}
