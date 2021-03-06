package com.fr.api;

import com.fr.service.AccountBusinessService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Init somme front services.
 *
 * Created by wdjenane on 28/06/2017.
 */
@RestController
@RequestMapping("/init")
@ApiVersion("1")
public class InitController
{
	@Autowired
	private AccountBusinessService accountService;
	
	@GetMapping("/token")
	ResponseEntity<Void> initTokens() {
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
