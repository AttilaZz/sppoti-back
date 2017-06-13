package com.fr.api.team.member;

import com.fr.commons.dto.UserDTO;
import com.fr.service.TeamControllerService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by djenanewail on 5/14/17.
 */

@RestController
@RequestMapping("/team/{teamId}/member")
@ApiVersion("1")
public class MemberAddController
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
	 * Add member for a given team - only admin can add a member to his team.
	 *
	 * @return 201 status if memeber has been added.
	 */
	@PostMapping
	ResponseEntity<UserDTO> addMember(@PathVariable final String teamId, @RequestBody final UserDTO user)
	{
		
		if (user.getId() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		final UserDTO savedTeamMember = this.teamControllerService.addMember(teamId, user);
		
		return new ResponseEntity<>(savedTeamMember, HttpStatus.CREATED);
	}
}
