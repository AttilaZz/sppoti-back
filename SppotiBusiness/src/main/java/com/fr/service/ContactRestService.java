package com.fr.service;

import com.fr.commons.dto.ContactDTO;
import org.springframework.stereotype.Service;

/**
 * Created by djenanewail on 4/8/17.
 */
@Service
public interface ContactRestService extends AbstractBusinessService
{
	
	/**
	 * Contact sppoti admins for a bug or information.
	 *
	 * @param contactDTO
	 * 		contact data.
	 */
	void sendGlobalContactEmail(ContactDTO contactDTO);
}
