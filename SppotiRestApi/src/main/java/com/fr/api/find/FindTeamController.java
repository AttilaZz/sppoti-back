package com.fr.api.find;

import com.fr.commons.dto.team.TeamDTO;
import com.fr.service.TeamControllerService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wdjenane on 10/02/2017.
 */
@RestController
@RequestMapping("/find/team")
@ApiVersion("1")
class FindTeamController
{
	
	/** Team find types. */
	private static final int MY_TEAM_SEARCH = 1;
	private static final int ALL_TEAM_SEARCH = 0;
	private static final int ALL_ADVERSE_TEAMS_SEARCH = 2;
	
	/**
	 * Team controller service.
	 */
	private final TeamControllerService teamControllerService;
	
	/**
	 * Init team service.
	 */
	@Autowired
	public FindTeamController(final TeamControllerService teamControllerService)
	{
		this.teamControllerService = teamControllerService;
	}
	
	/**
	 * @param team
	 * 		team to find.
	 * @param page
	 * 		page number.
	 *
	 * @return All found teams containing the String (team).
	 */
	@GetMapping("/" + MY_TEAM_SEARCH + "/{team}/{page}")
	ResponseEntity<List<TeamDTO>> findMyTeams(@PathVariable final String team, @PathVariable final int page)
	{
		
		return new ResponseEntity<>(this.teamControllerService.findAllMyTeams(team, page), HttpStatus.OK);
	}
	
	/**
	 * @param team
	 * 		team to find.
	 * @param page
	 * 		page number.
	 *
	 * @return All found teams containing the String (team).
	 */
	@GetMapping("/" + ALL_TEAM_SEARCH + "/{team}/{page}")
	ResponseEntity<List<TeamDTO>> findAllTeams(@PathVariable final String team, @PathVariable final int page)
	{
		
		return new ResponseEntity<>(this.teamControllerService.findAllTeams(team, page), HttpStatus.OK);
		
	}
	
	/**
	 * @param team
	 * 		team to find.
	 * @param page
	 * 		page number.
	 * @param sport
	 * 		sport id.
	 *
	 * @return All found teams containing the String (team) and linked to the sport in parameter.
	 */
	@GetMapping("/" + ALL_TEAM_SEARCH + "/{team}/{sportId}/{page}")
	ResponseEntity<List<TeamDTO>> findAllTeams(@PathVariable final String team, @PathVariable final int page,
											   @PathVariable final Long sport)
	{
		
		return new ResponseEntity<>(this.teamControllerService.findAllMyTeamsBySport(team, sport, page), HttpStatus.OK);
	}
	
	/**
	 * Find all teams that sppoti admin can challenge.
	 *
	 * @param team
	 * 		team prefix.
	 * @param sppotiId
	 * 		sppoti id.
	 * @param page
	 * 		page number.
	 *
	 * @return list of teams.
	 */
	@GetMapping("/" + ALL_ADVERSE_TEAMS_SEARCH + "/{sppotiId}/{team}/{page}")
	ResponseEntity<List<TeamDTO>> findAllAdverseTeams(@PathVariable final String team, @PathVariable final int sppotiId,
													  @PathVariable final int page)
	{
		
		return new ResponseEntity<>(
				this.teamControllerService.findAllAdverseTeamsAllowedToBeChallengedBySppotiAdmin(sppotiId, team, page),
				HttpStatus.OK);
	}
	
}