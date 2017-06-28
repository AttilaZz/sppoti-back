package com.fr.api.sppoti.sppoter;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppoterDTO;
import com.fr.service.SppotiControllerService;
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
 * Created by djenanewail on 5/14/17.
 */

@RestController
@RequestMapping("/sppoti/sppoter")
@ApiVersion("1")
public class SppotersAddController
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
	 * Add member for a given team - only admin can add a member to his team.
	 *
	 * @return 201 status if memeber has been added.
	 */
	@PostMapping("/add")
	ResponseEntity<UserDTO> addMember(@RequestBody @Valid final SppoterDTO sppoter)
	{
		
		return new ResponseEntity<>(this.sppotiControllerService
				.addSppoter(sppoter.getSppotiId(), sppoter.getUserId(), sppoter.getTeamId()), HttpStatus.CREATED);
	}
}

