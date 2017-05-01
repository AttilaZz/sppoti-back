package com.fr.api.team;

import com.fr.commons.dto.team.TeamDTO;
import com.fr.service.TeamControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/5/17.
 */

@RestController
@RequestMapping("/team")
class TeamGetController
{
	/** Team service. */
	private TeamControllerService teamControllerService;
	
	/** Init service. */
	@Autowired
	void setTeamControllerService(final TeamControllerService teamControllerService)
	{
		this.teamControllerService = teamControllerService;
	}
	
	/**
	 * Get team data from an id
	 *
	 * @param teamId
	 * 		team id.
	 *
	 * @return target team.
	 */
	@GetMapping("/{teamId}")
	ResponseEntity<TeamDTO> getTeamById(@PathVariable final int teamId)
	{
		
		return new ResponseEntity<>(this.teamControllerService.getTeamById(teamId), HttpStatus.OK);
	}
	
	/**
	 * Get all user teams.
	 *
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return All teams for a giver user Id.
	 */
	@GetMapping("/all/{userId}/{page}")
	ResponseEntity getAllTeams(@PathVariable final int userId, @PathVariable final int page)
	{
		
		return new ResponseEntity<>(this.teamControllerService.getAllTeamsByUserId(userId, page), HttpStatus.OK);
	}
	
	/**
	 * Get all joined teams (CONFIRMED/PENDING) except mine.
	 *
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return All teams for a giver user Id.
	 */
	@GetMapping("/all/joined/{userId}/{page}")
	ResponseEntity getAllJoinedTeams(@PathVariable final int userId, @PathVariable final int page)
	{
		
		return new ResponseEntity<>(this.teamControllerService.getAllJoinedTeamsByUserId(userId, page), HttpStatus.OK);
	}
	
	/**
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return deleted teams.
	 */
	@GetMapping("/all/deleted/{userId}/{page}")
	ResponseEntity getAllDeletedTeams(@PathVariable final int userId, @PathVariable final int page)
	{
		return new ResponseEntity<>(this.teamControllerService.getAllDeletedTeamsByUserId(userId, page), HttpStatus.OK);
	}
	
	/**
	 * Get all pending challenge requests from sppoti admin.
	 *
	 * @param teamId
	 * 		team id.
	 * @param page
	 * 		page number.
	 *
	 * @return 200 http status if found.
	 */
	@GetMapping("/all/challenge/pending/{teamId}/{page}")
	ResponseEntity getAllChallengeRequests(@PathVariable final int teamId, @PathVariable final int page)
	{
		return new ResponseEntity<>(this.teamControllerService.getAllPendingChallenges(teamId, page), HttpStatus.OK);
	}
}
