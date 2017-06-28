package com.fr.api.team.member;

import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
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
public class MemberUpdateController
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
	 * Update coordinate in the stadium (X, Y).
	 *
	 * @param memberId
	 * 		team member id.
	 *
	 * @return The updated member information.
	 */
	@PutMapping("/{memberId}")
	ResponseEntity<Void> updateInvitationStatus(@PathVariable("memberId") final String memberId,
												@PathVariable final String teamId, @RequestBody final TeamDTO teamDto)
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
	ResponseEntity<TeamDTO> updateTeamCaptain(@PathVariable final String teamId, @PathVariable final String memberId)
	{

		this.teamControllerService.updateTeamCaptain(teamId, memberId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
