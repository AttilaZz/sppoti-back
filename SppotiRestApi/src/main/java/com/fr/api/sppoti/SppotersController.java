package com.fr.api.sppoti;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.service.SppotiControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by djenanewail on 2/4/17.
 */
@RestController
@RequestMapping("/sppoti/{sppotiId}/sppoter")
class SppotersController
{
	
	private SppotiControllerService sppotiControllerService;
	
	@Autowired
	void setSppotiControllerService(SppotiControllerService sppotiControllerService)
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
	ResponseEntity<String> acceptSppoti(@PathVariable int sppotiId, @PathVariable int userId)
	{
		
		sppotiControllerService.acceptSppoti(sppotiId, userId);
		
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
	ResponseEntity<Void> refuseSppoti(@PathVariable int sppotiId, @PathVariable int userId)
	{
		
		sppotiControllerService.refuseSppoti(userId, sppotiId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
		
	}
	
	/**
	 * Add member for a given team - only admin can add a member to his team.
	 *
	 * @return 201 status if memeber has been added.
	 */
	@PostMapping("/add")
	ResponseEntity<UserDTO> addMember(@PathVariable int sppotiId, @RequestBody UserDTO user)
	{
		
		if (user.getId() == null) {
			throw new BusinessGlobalException("Sppoter id not found !!");
		}
		
		UserDTO savedTeamMember = sppotiControllerService.addSppoter(sppotiId, user);
		
		return new ResponseEntity<>(savedTeamMember, HttpStatus.CREATED);
	}
}