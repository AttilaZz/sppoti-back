package com.fr.impl;

import com.fr.commons.dto.ContactDTO;
import com.fr.service.ContactRestService;
import com.fr.service.email.ContactMailerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 4/8/17.
 */

@Component
public class ContactRestServiceImpl extends CommonControllerServiceImpl implements ContactRestService
{
	
	private final ContactMailerService contactMailerService;
	
	private final Logger LOGGER = LoggerFactory.getLogger(ContactRestServiceImpl.class);
	
	@Autowired
	public ContactRestServiceImpl(final ContactMailerService contactMailerService)
	{
		this.contactMailerService = contactMailerService;
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
		this.LOGGER.info("Confirmation email has been sent successfully !");
		this.contactMailerService.sendContactEmail(contact);
	}
}
