package com.fr.api.team;

import com.fr.service.TeamControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/14/17.
 */

@RestController
@RequestMapping("/team")
class TeamDeleteTeam
{
	
	/**
	 * Team service.
	 */
	private TeamControllerService teamControllerService;
	
	@Autowired
	void setTeamControllerService(final TeamControllerService teamControllerService)
	{
		this.teamControllerService = teamControllerService;
	}
	
	/**
	 * This method delete a team
	 *
	 * @param id
	 * 		team id.
	 *
	 * @return 200 status if team was deleted, 400 status otherwise
	 */
	@DeleteMapping("{/id}")
	ResponseEntity deleteTeam(@PathVariable final int id)
	{
		this.teamControllerService.deleteTeam(id);
		return new ResponseEntity(HttpStatus.OK);
	}
	
}