package com.fr.api.team.member;

import com.fr.service.TeamControllerService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 5/14/17.
 */

@RestController
@RequestMapping("/team/{teamId}/member")
@ApiVersion("1")
public class MemberDeleteController
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
	 * Delete memeber for a given team - only team admin can delete a member
	 *
	 * @return 200 status if memeber has been added
	 */
	@DeleteMapping("/{memberId}")
	ResponseEntity<Void> deleteMember(@PathVariable final String teamId, @PathVariable final String memberId)
	{
		
		this.teamControllerService.deleteMemberFromTeam(teamId, memberId);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
