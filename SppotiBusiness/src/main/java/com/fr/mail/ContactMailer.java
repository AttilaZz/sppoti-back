package com.fr.mail;

import com.fr.commons.dto.ContactDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Created by djenanewail on 4/8/17.
 */
@Component
public class ContactMailer extends ApplicationMailer
{
	
	
	/**
	 * Admins email.
	 */
	@Value("${spring.app.contact.email}")
	private String emailContact;
	
	/**
	 * Email contact subject.
	 */
	@Value("${spring.app.contact.subject}")
	private String emailContactSubject;
	
	
	/**
	 * Init contact mailer.
	 */
	public ContactMailer(final JavaMailSender sender, final MailProperties mailProperties,
						 final TemplateEngine templateEngine)
	{
		super(sender, mailProperties, templateEngine);
	}
	
	/**
	 * Send contact email to sppoti admins.
	 *
	 * @param contactDTO
	 * 		contact data.
	 */
	public void sendContactEmail(final ContactDTO contactDTO)
	{
		this.prepareAndSendEmail(contactDTO, this.emailContactSubject, this.emailContact, null);
	}
	
	/**
	 * Prepare email to send
	 *
	 * @param contactDTO
	 * 		data to send.
	 * @param subject
	 * 		email subject.
	 */
	public void prepareAndSendEmail(final ContactDTO contactDTO, final String subject, final String emailContact,
									final ResourceContent resourceContent)
	{
		
		final Context context = new Context();
		context.setVariable("name", contactDTO.getName());
		context.setVariable("email", contactDTO.getEmail());
		context.setVariable("object", contactDTO.getObject());
		context.setVariable("body", contactDTO.getMessage());
		
		final String text = this.templateEngine.process(PATH_TO_CONTACT_TEMPLATE, context);
		
		
		super.prepareAndSendEmail(emailContact, subject, text, resourceContent);
		
	}
}
