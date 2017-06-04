package com.fr.api.contact;

import com.fr.commons.dto.ContactDTO;
import com.fr.service.ContactControllerService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by djenanewail on 4/7/17.
 */


@RestController
@RequestMapping("/contact")
@ApiVersion("1")
public class ContactAddController
{
	
	/** Contact service. */
	private final ContactControllerService contactControllerService;
	
	/** Init contact service. */
	@Autowired
	public ContactAddController(final ContactControllerService contactControllerService)
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
	public ResponseEntity sendContactEmail(@RequestBody @Valid final ContactDTO contactDTO)
	{
		
		this.contactControllerService.sendGlobalContactEmail(contactDTO);
		return new ResponseEntity(HttpStatus.CREATED);
	}
	
}
