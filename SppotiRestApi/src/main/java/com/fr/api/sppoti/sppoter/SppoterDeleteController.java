package com.fr.api.sppoti.sppoter;

import com.fr.service.SppotiBusinessService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 6/8/17.
 */


@RestController
@RequestMapping("/sppoti/{sppotiId}/sppoter")
@ApiVersion("1")
public class SppoterDeleteController
{
	/** Sppoti service. */
	private SppotiBusinessService sppotiControllerService;
	
	/** Init service. */
	@Autowired
	void setSppotiControllerService(final SppotiBusinessService sppotiControllerService)
	{
		this.sppotiControllerService = sppotiControllerService;
	}
	
	@DeleteMapping("/{userId}")
	ResponseEntity<String> acceptSppoti(@PathVariable final String sppotiId, @PathVariable final String userId)
	{
		
		this.sppotiControllerService.deleteSppoter(sppotiId, userId);
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
}
