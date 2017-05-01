package com.fr.api;

import com.fr.entities.SportEntity;
import com.fr.service.SportControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Djenane Wail on 12/10/16.
 */

@RestController
class SportController
{
	
	/** Sport service. */
	@Autowired
	private SportControllerService sportService;
	
	@GetMapping(value = "/sport/all")
	ResponseEntity<Object> getAllSports()
	{
		
		final List<SportEntity> allSports = this.sportService.getAllSports();
		
		if (allSports.isEmpty()) {
			return new ResponseEntity<>(allSports, HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(allSports, HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/sport")
	ResponseEntity<Object> addSports()
	{
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
}