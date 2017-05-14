package com.fr.api.sppoti.sppoter;

import com.fr.commons.dto.UserDTO;
import com.fr.service.SppotiControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by djenanewail on 5/14/17.
 */

@RestController
@RequestMapping("/sppoti/sppoter")
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
	ResponseEntity<UserDTO> addMember(@RequestBody @Valid final Sppoter sppoter)
	{
		
		return new ResponseEntity<>(this.sppotiControllerService
				.addSppoter(sppoter.getSppotiId(), sppoter.getUserId(), sppoter.getTeamId()), HttpStatus.CREATED);
	}
	
	class Sppoter
	{
		@NotNull
		private Integer userId;
		@NotNull
		private Integer sppotiId;
		@NotNull
		private Integer teamId;
		
		public Integer getUserId()
		{
			return this.userId;
		}
		
		public void setUserId(final Integer userId)
		{
			this.userId = userId;
		}
		
		public Integer getSppotiId()
		{
			return this.sppotiId;
		}
		
		public void setSppotiId(final Integer sppotiId)
		{
			this.sppotiId = sppotiId;
		}
		
		public Integer getTeamId()
		{
			return this.teamId;
		}
		
		public void setTeamId(final Integer teamId)
		{
			this.teamId = teamId;
		}
	}
}

