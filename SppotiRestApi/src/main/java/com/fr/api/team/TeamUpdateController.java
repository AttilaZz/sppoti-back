package com.fr.api.team;

import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.TeamStatus;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.service.TeamControllerService;
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
	
	/** Team controller service. */
	private TeamControllerService teamControllerService;
	
	/** Init controller services. */
	@Autowired
	void setTeamControllerService(final TeamControllerService teamControllerService)
	{
		this.teamControllerService = teamControllerService;
	}
	
	/**
	 * This method update general team informations.
	 * Title, Logos, Cover
	 *
	 * @param teamId
	 * 		team teamId.
	 * @param teamRequestDTO
	 * 		data to update.
	 *
	 * @return The updated team.
	 */
	@PutMapping
	ResponseEntity<TeamDTO> updateTeam(@PathVariable final String teamId, @RequestBody final TeamDTO teamRequestDTO,
									   final Authentication authentication)
	{
		
		boolean canUpdate = false;
		
		//Update team name.
		if (StringUtils.hasText(teamRequestDTO.getName())) {
			canUpdate = true;
		}
		
		//Update team logos.
		if (StringUtils.hasText(teamRequestDTO.getLogoPath())) {
			canUpdate = true;
		}
		
		//Update team cover.
		if (StringUtils.hasText(teamRequestDTO.getCoverPath())) {
			canUpdate = true;
		}
		
		//Update team color.
		if (StringUtils.hasText(teamRequestDTO.getColor())) {
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
	 *
	 * @param teamId
	 * 		team id.
	 *
	 * @return 202 http status if updated succeed.
	 */
	@PutMapping("/accept")
	ResponseEntity<Void> acceptTeam(@PathVariable final String teamId, final Authentication authentication)
	{
		
		final AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		
		this.teamControllerService.acceptTeam(teamId, accountUserDetails.getUuid());
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	/**
	 * Refuse team invitation.
	 *
	 * @param teamId
	 * 		team id.
	 *
	 * @return 202 http status if updated succeed.
	 */
	@PutMapping("/refuse")
	ResponseEntity<Void> refuseTeam(@PathVariable final String teamId, final Authentication authentication)
	{
		
		final AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		
		this.teamControllerService.refuseTeam(teamId, accountUserDetails.getUuid());
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	/**
	 * Accept / Refuse sppoti admin challenge
	 *
	 * @param dto
	 * 		sppoti dto, containing sppoti id and response to challenge.
	 *
	 * @return 202 http status if updated succeed.
	 */
	@PutMapping("/challenge")
	ResponseEntity<TeamDTO> requestChallenge(@PathVariable final String teamId, @RequestBody final SppotiDTO dto)
	{
		
		if (dto.getId() == null || !StringUtils.hasText(dto.getTeamAdverseStatus())) {
			throw new BusinessGlobalException("Sppoti id or team status missing");
		}
		
		boolean statusExist = false;
		for (final GlobalAppStatusEnum status : GlobalAppStatusEnum.values()) {
			if (status.name().equals(dto.getTeamAdverseStatus()) &&
					(dto.getTeamAdverseStatus().equals(GlobalAppStatusEnum.CONFIRMED.name()) ||
							dto.getTeamAdverseStatus().equals(GlobalAppStatusEnum.REFUSED.name()))) {
				statusExist = true;
			}
		}
		
		if (!statusExist) {
			throw new BusinessGlobalException("Status must be (CONFIRMED) or (REFUSED)");
		}
		
		final TeamDTO adverseTeam = this.teamControllerService.responseToSppotiAdminChallenge(dto, teamId);
		
		return new ResponseEntity<>(adverseTeam, HttpStatus.ACCEPTED);
		
	}

	/**
	 * Update team type to private or public.
	 *
	 * @param teamId id of the tea to update.
	 * @param dto    dto containing the new type.
	 * @return 202 status if team has been updated.
	 */
	@PutMapping("/type")
	ResponseEntity<TeamDTO> requestChallenge(@PathVariable final String teamId, @RequestBody final TeamDTO dto) {

		if (!StringUtils.hasText(teamId) && dto.getType() == null) {
			throw new BusinessGlobalException("Team id and status are required to update team type");
		}

		if (!dto.getType().equals(TeamStatus.PRIVATE) && !dto.getType().equals(TeamStatus.PUBLIC)) {
			throw new BusinessGlobalException("Type can be Private or Public");
		}

		return ResponseEntity.accepted().body(this.teamControllerService.updateTeamType(teamId, dto.getType()));
	}
}
