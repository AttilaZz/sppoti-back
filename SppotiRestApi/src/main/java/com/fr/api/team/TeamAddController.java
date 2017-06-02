package com.fr.api.team;

import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.service.TeamControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by djenanewail on 1/22/17.
 */

@RestController
@RequestMapping("/team")
class TeamAddController
{
	
	/** Team controller service. */
	private TeamControllerService teamControllerService;
	
	/** Init services. */
	@Autowired
	void setTeamControllerService(final TeamControllerService teamControllerService)
	{
		this.teamControllerService = teamControllerService;
	}
	
	/**
	 * This service create team
	 *
	 * @param team
	 * 		team to add.
	 * @param authentication
	 * 		auth object.
	 *
	 * @return Created team data
	 */
	@PostMapping
	ResponseEntity<TeamDTO> createTeam(@RequestBody @Valid final TeamDTO team, final Authentication authentication)
	{
		
		final AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		
		final TeamDTO teamDTO = this.teamControllerService.saveTeam(team, accountUserDetails.getId());
		
		return new ResponseEntity<>(teamDTO, HttpStatus.CREATED);
	}
	
}