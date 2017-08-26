package com.fr.impl;

import com.fr.commons.dto.ContactDTO;
import com.fr.mail.ContactMailer;
import com.fr.service.ContactRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 4/8/17.
 */

@Component
public class ContactRestServiceImpl extends AbstractControllerServiceImpl implements ContactRestService
{
	
	/** Contact mailer. */
	private final ContactMailer contactMailer;
	
	/** Class logger. */
	private final Logger LOGGER = LoggerFactory.getLogger(ContactRestServiceImpl.class);
	
	/**
	 * Init class dependencies.
	 */
	@Autowired
	public ContactRestServiceImpl(final ContactMailer contactMailer)
	{
		this.contactMailer = contactMailer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendGlobalContactEmail(final ContactDTO contactDTO)
	{
		this.sendContactEmail(contactDTO);
	}
	
	/**
	 * Common method for sending contact emails.
	 *
	 * @param contact
	 * 		contact data.
	 */
	private void sendContactEmail(final ContactDTO contact)
	{
		final Thread thread = new Thread(() -> {
			this.contactMailer.sendContactEmail(contact);
			this.LOGGER.info("Confirmation email has been sent successfully !");
		});
		thread.start();
	}
}
