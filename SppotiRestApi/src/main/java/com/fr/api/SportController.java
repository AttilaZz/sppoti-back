package com.fr.api;

import com.fr.entities.SportEntity;
import com.fr.service.SportBusinessService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Djenane Wail on 12/10/16.
 */

@RestController
@RequestMapping("/sport")
@ApiVersion("1")
class SportController
{
	
	/** Sport service. */
	@Autowired
	private SportBusinessService sportService;
	
	@GetMapping(value = "/all")
	ResponseEntity<Object> getAllSports()
	{
		
		final List<SportEntity> allSports = this.sportService.getAllSports();
		
		if (allSports.isEmpty()) {
			return new ResponseEntity<>(allSports, HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(allSports, HttpStatus.OK);
		
	}
	
}