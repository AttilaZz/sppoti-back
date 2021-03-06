package com.fr.api.sppoti.sppoter;

import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.service.SppotiBusinessService;
import com.fr.versionning.ApiVersion;
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
@ApiVersion("1")
class SppotersUpdateController
{
	
	private SppotiBusinessService sppotiControllerService;
	
	@Autowired
	void setSppotiControllerService(final SppotiBusinessService sppotiControllerService)
	{
		this.sppotiControllerService = sppotiControllerService;
	}
	
	@PutMapping("/accept/{userId}")
	ResponseEntity<String> acceptSppoti(@PathVariable final String sppotiId, @PathVariable final String userId)
	{
		this.sppotiControllerService.acceptSppotiInvitation(sppotiId, userId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/refuse/{userId}")
	ResponseEntity<Void> refuseSppoti(@PathVariable final String sppotiId, @PathVariable final String userId)
	{
		this.sppotiControllerService.rejectSppotiInvitation(userId, sppotiId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/request/join")
	ResponseEntity<SppotiDTO> requestJoinTeam(@PathVariable final String sppotiId)
	{
		this.sppotiControllerService.requestJoinSppoti(sppotiId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/leave")
	ResponseEntity<SppotiDTO> leaveSppoti(@PathVariable final String sppotiId)
	{
		this.sppotiControllerService.leaveSppoti(sppotiId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}