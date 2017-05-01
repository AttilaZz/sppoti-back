package com.fr.api.contact;

import com.fr.commons.dto.ContactDTO;
import com.fr.service.ContactControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by djenanewail on 4/7/17.
 */


@RestController
@RequestMapping("/contact")
public class ContactAddController
{
	
	/** Contact service. */
	private final ContactControllerService contactControllerService;
	
	/** Init contact service. */
	@Autowired
	public ContactAddController(ContactControllerService contactControllerService)
	{
		this.contactControllerService = contactControllerService;
	}
	
	/**
	 * Send Email to sppoti.contact@gmail.com
	 *
	 * @param contactDTO
	 * 		contact data.
	 *
	 * @return 200 status if email sent.
	 */
	@PostMapping
	public ResponseEntity sendContactEmail(@RequestBody @Valid ContactDTO contactDTO)
	{
		
		contactControllerService.sendGlobalContactEmail(contactDTO);
		return new ResponseEntity(HttpStatus.CREATED);
	}
	
}
