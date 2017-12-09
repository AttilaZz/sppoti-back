package com.fr.api.team;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.TeamStatus;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.service.TeamBusinessService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by djenanewail on 2/22/17.
 */

@RestController
@RequestMapping("/team/{teamId}")
@ApiVersion("1")
class TeamUpdateController
{
	
	private TeamBusinessService teamControllerService;
	
	@Autowired
	void setTeamControllerService(final TeamBusinessService teamControllerService)
	{
		this.teamControllerService = teamControllerService;
	}
	
	/**
	 * This method update general team informations.
	 * Title, Logos, Cover.
	 */
	@PutMapping
	ResponseEntity<TeamDTO> updateTeam(@PathVariable final String teamId, @RequestBody final TeamDTO teamRequestDTO,
									   final Authentication authentication)
	{
		
		boolean canUpdate = false;
		
		if (StringUtils.hasText(teamRequestDTO.getName())) {
			canUpdate = true;
		}
		
		if (StringUtils.hasText(teamRequestDTO.getLogoPath())) {
			canUpdate = true;
		}
		
		if (StringUtils.hasText(teamRequestDTO.getCoverPath())) {
			canUpdate = true;
		}
		
		if (StringUtils.hasText(teamRequestDTO.getColor())) {
			canUpdate = true;
		}
		
		if (teamRequestDTO.getType() != null && (TeamStatus.PRIVATE.equals(teamRequestDTO.getType()) ||
				TeamStatus.PUBLIC.equals(teamRequestDTO.getType()))) {
			canUpdate = true;
		}
		
		final String connectedUserId = ((AccountUserDetails) authentication.getPrincipal()).getUuid();
		teamRequestDTO.setId(teamId);
		if (canUpdate) {
			this.teamControllerService.updateTeam(connectedUserId, teamRequestDTO);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	/**
	 * Accept team invitation.
	 */
	@PutMapping("/accept")
	ResponseEntity<Void> acceptTeam(@PathVariable final String teamId)
	{
		this.teamControllerService.acceptTeamRequestSentFromTeamAdmin(teamId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	/**
	 * Refuse team invitation.
	 */
	@PutMapping("/refuse")
	ResponseEntity<Void> refuseTeam(@PathVariable final String teamId)
	{
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	/**
	 * Change team privacy type.
	 */
	@PutMapping("/type")
	ResponseEntity<TeamDTO> acceptOrRefuseSppotiAdminChallenge(@PathVariable final String teamId,
															   @RequestBody final TeamDTO dto)
	{
		
		if (!StringUtils.hasText(teamId) && dto.getType() == null) {
			throw new BusinessGlobalException("Team id and status are required to update team type");
		}
		
		if (!dto.getType().equals(TeamStatus.PRIVATE) && !dto.getType().equals(TeamStatus.PUBLIC)) {
			throw new BusinessGlobalException("Type can be Private or Public");
		}
		
		return ResponseEntity.accepted().body(this.teamControllerService.updateTeamType(teamId, dto.getType()));
	}
	
	/**
	 * Accept or Refuse team member request to join the team.
	 */
	@PutMapping("/answer/member/join/request")
	ResponseEntity acceptTeamMemberRequestToJoinTheTeam(@PathVariable final String teamId,
														@RequestBody final UserDTO dto)
	{
		if (!StringUtils.hasText(dto.getId()) || dto.getTeamStatus() == null || !StringUtils.hasText(teamId)) {
			throw new BusinessGlobalException("Team ID or Member ID or Status not found");
		}
		
		if (!dto.getTeamStatus().equals(GlobalAppStatusEnum.CONFIRMED) &&
				!dto.getTeamStatus().equals(GlobalAppStatusEnum.REFUSED)) {
			throw new BusinessGlobalException("Authorized status are: CONFIRMED / REFUSED");
		}
		
		if (dto.getTeamStatus().equals(GlobalAppStatusEnum.CONFIRMED)) {
			this.teamControllerService.confirmTeamRequestSentFromUser(teamId, dto);
		} else if (dto.getTeamStatus().equals(GlobalAppStatusEnum.REFUSED)) {
			this.teamControllerService.refuseTeamRequestSentFromUser(teamId, dto);
		}
		
		return ResponseEntity.accepted().build();
	}
	
	/**
	 * Respond to sppoti admin challenge request.
	 */
	@PutMapping("/respond/to/sppoti/admin/challenge/request")
	ResponseEntity<TeamDTO> requestChallenge(@PathVariable final String teamId, @RequestBody final SppotiDTO dto)
	{
		
		if (dto.getId() == null || !StringUtils.hasText(dto.getTeamAdverseStatus())) {
			throw new BusinessGlobalException("Sppoti id or team status missing");
		}
		
		boolean statusExist = false;
		for (final GlobalAppStatusEnum status : GlobalAppStatusEnum.values()) {
			if (status.name().equals(dto.getTeamAdverseStatus())) {
				statusExist = true;
			}
		}
		if (!statusExist) {
			throw new BusinessGlobalException("Status must be (CONFIRMED) or (REFUSED)");
		}
		
		final TeamDTO adverseTeam = this.teamControllerService.responseToSppotiAdminChallenge(dto, teamId);
		
		return new ResponseEntity<>(adverseTeam, HttpStatus.ACCEPTED);
		
	}
}
