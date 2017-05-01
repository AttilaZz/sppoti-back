package com.fr.api.sppoti;

import com.fr.service.SppotiControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/5/17.
 */

@RestController
@RequestMapping("/sppoti")
class SppotiDeleteController
{
	
	private SppotiControllerService sppotiControllerService;
	
	@Autowired
	void setSppotiControllerService(SppotiControllerService sppotiControllerService)
	{
		this.sppotiControllerService = sppotiControllerService;
	}
	
	/**
	 * @param id
	 * 		sppoti id.
	 *
	 * @return 200 status if deleted, 400 status otherwise.
	 */
	@DeleteMapping("/{id}")
	ResponseEntity deleteSppoti(@PathVariable int id)
	{
		
		sppotiControllerService.deleteSppoti(id);
		return new ResponseEntity(HttpStatus.OK);
		
	}
	
}
