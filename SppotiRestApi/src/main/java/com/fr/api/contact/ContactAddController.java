package com.fr.api.contact;

import com.fr.commons.dto.ContactDTO;
import com.fr.service.ContactRestService;
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
	
	private final ContactRestService contactControllerService;
	
	@Autowired
	public ContactAddController(final ContactRestService contactControllerService)
	{
		this.contactControllerService = contactControllerService;
	}
	
	@PostMapping
	public ResponseEntity sendContactEmail(@RequestBody @Valid final ContactDTO contactDTO)
	{
		
		this.contactControllerService.sendGlobalContactEmail(contactDTO);
		return new ResponseEntity(HttpStatus.CREATED);
	}
	
}
