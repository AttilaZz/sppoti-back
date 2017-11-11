package com.fr.service.email;

import com.fr.commons.dto.ContactDTO;

/**
 * Created by djenanewail on 11/10/17.
 */
public interface ContactMailerService extends ApplicationMailerService
{
	void sendContactEmail(ContactDTO contactDTO);
}
