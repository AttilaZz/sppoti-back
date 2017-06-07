package com.fr.api.team.member;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.service.TeamControllerService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Created by djenanewail on 2/4/17.
 */

@RestController
@RequestMapping("/team/{teamId}/member")
@ApiVersion("1")
class TeamUpdateMembersController
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
	 * Accept/Refuse team information.
	 * Update coordinate in the stadium (X, Y).
	 *
	 * @param memberId
	 * 		team member id.
	 *
	 * @return The updated member information.
	 */
	@PutMapping("/{memberId}")
	ResponseEntity<Void> updateInvitationStatus(@PathVariable("memberId") final int memberId,
												@PathVariable final int teamId, @RequestBody final TeamDTO teamDto)
	{
		
		boolean canUpdate = false;
		
		if (teamDto.getStatus() != null && !teamDto.getStatus().equals(0)) {
			for (final GlobalAppStatusEnum status : GlobalAppStatusEnum.values()) {
				if (status.getValue() == teamDto.getStatus()) {
					canUpdate = true;
				}
			}
		}
		
		if (teamDto.getxPosition() != null && teamDto.getyPosition() != null) {
			canUpdate = true;
		}
		
		if (!canUpdate) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		this.teamControllerService.updateTeamMembers(teamDto, memberId, teamId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
		
	}
	
	/**
	 * @param teamId
	 * 		team id.
	 * @param memberId
	 * 		memeber id.
	 *
	 * @return 202 status if captain updated.
	 */
	@PutMapping("/captain/{memberId}")
	ResponseEntity<TeamDTO> updateTeamCaptain(@PathVariable final int teamId, @PathVariable final int memberId,
											  final Authentication authentication)
	{
		
		final AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		
		this.teamControllerService.updateTeamCaptain(teamId, memberId, accountUserDetails.getUuid());
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	/**
	 * Add member for a given team - only admin can add a member to his team.
	 *
	 * @return 201 status if memeber has been added.
	 */
	@PostMapping
	ResponseEntity<UserDTO> addMember(@PathVariable final int teamId, @RequestBody final UserDTO user)
	{
		
		if (user.getId() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		final UserDTO savedTeamMember = this.teamControllerService.addMember(teamId, user);
		
		return new ResponseEntity<>(savedTeamMember, HttpStatus.CREATED);
	}
	
}