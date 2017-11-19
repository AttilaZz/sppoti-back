package com.fr.impl.email;

import com.fr.commons.dto.ContactDTO;
import com.fr.commons.dto.MailResourceContent;
import com.fr.service.email.ContactMailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djenanewail on 4/8/17.
 */
@Component
public class ContactMailerImpl extends ApplicationMailerServiceImpl implements ContactMailerService
{
	@Value("${spring.app.contact.email}")
	private String emailContact;
	
	@Value("${spring.app.contact.subject}")
	private String emailContactSubject;
	
	private final TemplateEngine templateEngine;
	
	@Autowired
	public ContactMailerImpl(final TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}
	
	/**
	 * Send contact email to sppoti admins.
	 *
	 * @param contactDTO
	 * 		contact data.
	 */
	@Override
	public void sendContactEmail(final ContactDTO contactDTO)
	{
		this.prepareAndSendEmail(contactDTO, this.emailContactSubject, this.emailContact);
	}
	
	/**
	 * Prepare email to send
	 *
	 * @param contactDTO
	 * 		data to send.
	 * @param subject
	 * 		email subject.
	 */
	private void prepareAndSendEmail(final ContactDTO contactDTO, final String subject, final String emailContact)
	{
		final List<MailResourceContent> resourceContents = new ArrayList<>();
		final MailResourceContent content = new MailResourceContent();
		content.setPath(IMAGES_DIRECTORY + logoResourceName);
		content.setResourceName(logoResourceName);
		resourceContents.add(content);
		
		final Context context = new Context();
		context.setVariable("name", contactDTO.getName());
		context.setVariable("email", contactDTO.getEmail());
		context.setVariable("object", contactDTO.getObject());
		context.setVariable("body", contactDTO.getMessage());
		context.setVariable("imageResourceName", content.getResourceName());
		
		final String text = this.templateEngine.process(PATH_TO_CONTACT_TEMPLATE, context);
		
		
		super.prepareAndSendEmail(emailContact, subject, text, resourceContents);
		
	}
}
