package com.fr.api.team;

import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.service.TeamBusinessService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/5/17.
 */

@RestController
@RequestMapping("/team")
@ApiVersion("1")
class TeamGetController
{
	/** Team service. */
	private TeamBusinessService teamControllerService;
	
	/** Init service. */
	@Autowired
	void setTeamControllerService(final TeamBusinessService teamControllerService)
	{
		this.teamControllerService = teamControllerService;
	}
	
	/**
	 * Get team data from an id
	 */
	@GetMapping("/{teamId}")
	ResponseEntity<TeamDTO> getTeamById(@PathVariable final String teamId)
	{
		
		return new ResponseEntity<>(this.teamControllerService.getTeamById(teamId), HttpStatus.OK);
	}
	
	/**
	 * Get all user teams.
	 */
	@GetMapping("/all/{userId}/{page}")
	ResponseEntity getAllTeams(@PathVariable final String userId, @PathVariable final int page)
	{
		
		return new ResponseEntity<>(this.teamControllerService.getAllTeamsByUserId(userId, page), HttpStatus.OK);
	}
	
	/**
	 * Get all joined teams (CONFIRMED/PENDING) except mine.
	 */
	@GetMapping("/all/joined/{userId}/{page}")
	ResponseEntity getAllJoinedTeams(@PathVariable final String userId, @PathVariable final int page)
	{
		
		return new ResponseEntity<>(this.teamControllerService.getAllJoinedTeamsByUserId(userId, page), HttpStatus.OK);
	}
	
	/**
	 * Get all deleted teams for a user.
	 */
	@GetMapping("/all/deleted/{userId}/{page}")
	ResponseEntity getAllDeletedTeams(@PathVariable final String userId, @PathVariable final int page)
	{
		return new ResponseEntity<>(this.teamControllerService.getAllDeletedTeamsByUserId(userId, page), HttpStatus.OK);
	}
	
	/**
	 * Get all pending challenge requests from sppoti admin.
	 */
	@GetMapping("/all/challenge/pending/{teamId}/{page}")
	ResponseEntity getAllChallengeRequests(@PathVariable final String teamId, @PathVariable final int page)
	{
		return new ResponseEntity<>(this.teamControllerService.findAllPendingChallenges(teamId, page), HttpStatus.OK);
	}
	
	/**
	 * Get all teams allowed to challenge a sppoti. (same sport, not already in sppoti adverse)
	 */
	@GetMapping("/all/challenge/allowed/{sppotiId}/{page}")
	ResponseEntity getAllTeamSAllowedToChallengeSppoti(@PathVariable final int page,
													   @PathVariable final String sppotiId,
													   final Authentication authentication)
	{
		final Long userId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		
		return new ResponseEntity<>(
				this.teamControllerService.findAllAllowedTeamsToChallengeSppoti(userId, sppotiId, page), HttpStatus.OK);
	}
	
	/**
	 * Get all teams by the sport id.
	 */
	@GetMapping("/all/by/sport/{sportId}/{page}")
	ResponseEntity getAllTeamBySportType(@PathVariable final int page, @PathVariable final Long sportId)
	{
		
		return new ResponseEntity<>(this.teamControllerService.findAllTeamsBySportType(sportId, page), HttpStatus.OK);
	}
	
}
