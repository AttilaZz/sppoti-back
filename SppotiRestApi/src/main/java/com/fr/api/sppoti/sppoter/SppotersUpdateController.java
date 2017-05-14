package com.fr.api.sppoti.sppoter;

import com.fr.service.SppotiControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/4/17.
 */
@RestController
@RequestMapping("/sppoti/{sppotiId}/sppoter")
class SppotersUpdateController
{
	
	/** Sppoti service. */
	private SppotiControllerService sppotiControllerService;
	
	/** Init service. */
	@Autowired
	void setSppotiControllerService(final SppotiControllerService sppotiControllerService)
	{
		this.sppotiControllerService = sppotiControllerService;
	}
	
	/**
	 * @param sppotiId
	 * 		sppoti id.
	 * @param userId
	 * 		user id.
	 *
	 * @return 202 status if sppoti status updated.
	 */
	@PutMapping("/accept/{userId}")
	ResponseEntity<String> acceptSppoti(@PathVariable final int sppotiId, @PathVariable final int userId)
	{
		
		this.sppotiControllerService.acceptSppoti(sppotiId, userId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
		
	}
	
	/**
	 * @param sppotiId
	 * 		sppoti id.
	 * @param userId
	 * 		user id.
	 *
	 * @return 202 status if sppoti status updated.
	 */
	@PutMapping("/refuse/{userId}")
	ResponseEntity<Void> refuseSppoti(@PathVariable final int sppotiId, @PathVariable final int userId)
	{
		
		this.sppotiControllerService.refuseSppoti(userId, sppotiId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
		
	}
	
}