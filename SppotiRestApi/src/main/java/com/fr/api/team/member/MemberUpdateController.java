package com.fr.api.team.member;

import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.service.TeamBusinessService;
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
	private TeamBusinessService teamControllerService;
	
	/** Init services. */
	@Autowired
	void setTeamControllerService(final TeamBusinessService teamControllerService)
	{
		this.teamControllerService = teamControllerService;
	}
	
	/**
	 * update team member.
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
	 * edit team captain.
	 */
	@PutMapping("/captain/{memberId}")
	ResponseEntity<TeamDTO> updateTeamCaptain(@PathVariable final String teamId, @PathVariable final String memberId)
	{
		this.teamControllerService.updateTeamCaptain(teamId, memberId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	/**
	 * send a request to join team.
	 */
	@PutMapping("/request/join")
	ResponseEntity<TeamDTO> requestJoinTeam(@PathVariable final String teamId)
	{
		this.teamControllerService.sendRequestToJoinTeam(teamId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	/**
	 * Accept team invitation.
	 */
	@PutMapping("/accept/invitation")
	ResponseEntity<Void> acceptTeam(@PathVariable final String teamId)
	{
		
		this.teamControllerService.acceptTeamRequestSentFromTeamAdmin(teamId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	/**
	 * Refuse team invitation.
	 */
	@PutMapping("/refuse/invitation")
	ResponseEntity<Void> refuseTeam(@PathVariable final String teamId)
	{
		
		this.teamControllerService.refuseTeamRequestSentFromTeamAdmin(teamId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	/**
	 * Cancel request sent to join a team.
	 */
	@PutMapping("/cancel/join/request")
	ResponseEntity<Void> cancelJoinTeamRequest(@PathVariable final String teamId)
	{
		
		this.teamControllerService.cancelJoinTeamRequest(teamId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	/**
	 * Leave a team.
	 */
	@PutMapping("/leave")
	ResponseEntity<Void> leaveTeam(@PathVariable final String teamId)
	{
		
		this.teamControllerService.leaveTeam(teamId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}