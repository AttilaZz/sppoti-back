package com.fr.api.sppoti;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.SppotiStatus;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.exception.NotAdminException;
import com.fr.service.SppotiBusinessService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by djenanewail on 2/5/17.
 */

@RestController
@RequestMapping("/sppoti")
@ApiVersion("1")
class SppotiUpdateController
{
	
	/** Sppoti controller service. */
	private SppotiBusinessService sppotiControllerService;
	
	/** Init service. */
	@Autowired
	void setSppotiControllerService(final SppotiBusinessService sppotiControllerService)
	{
		this.sppotiControllerService = sppotiControllerService;
	}
	
	/**
	 * Update sppoti information.
	 */
	@PutMapping("/{sppotiId}")
	ResponseEntity<SppotiDTO> updateSppoti(@PathVariable final String sppotiId,
										   @RequestBody final SppotiDTO sppotiRequest,
										   final Authentication authentication)
	{
		//throws exception if user is not the sppoti admin
		if (!this.sppotiControllerService.isSppotiAdmin(sppotiId)) {
			throw new NotAdminException("You must be the sppoti admin to continue");
		}
		
		boolean canUpdate = false;
		
		if (StringUtils.hasText(sppotiRequest.getTags())) {
			canUpdate = true;
		}
		
		//		if (StringUtils.hasText(sppotiRequest.getDescription())) {
		//			canUpdate = true;
		//		}
		
		if (sppotiRequest.getDateTimeStart() != null) {
			canUpdate = true;
		}
		
		if (StringUtils.hasText(sppotiRequest.getName())) {
			canUpdate = true;
		}
		
		if (StringUtils.hasText(sppotiRequest.getLocation())) {
			canUpdate = true;
		}
		
		if (StringUtils.hasText(sppotiRequest.getVsTeam())) {
			canUpdate = true;
		}
		
		if (sppotiRequest.getType() != null && (SppotiStatus.PRIVATE.equals(sppotiRequest.getType()) ||
				SppotiStatus.PUBLIC.equals(sppotiRequest.getType()))) {
			canUpdate = true;
		}
		
		if (sppotiRequest.getMaxTeamCount() != null && sppotiRequest.getMaxTeamCount() != 0) {
			canUpdate = true;
		}
		
		if (canUpdate) {
			this.sppotiControllerService.updateSppoti(sppotiRequest, sppotiId);
		} else {
			throw new BusinessGlobalException("Update not accepted");
		}
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	/**
	 * Send a challenge.
	 */
	@PutMapping("/challenge/send/{sppotiId}/{teamId}")
	ResponseEntity<SppotiDTO> sendChallenge(@PathVariable final String sppotiId, @PathVariable final String teamId,
											final Authentication authentication)
	{
		
		final AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		
		this.sppotiControllerService.sendChallengeToSppotiHostTeam(sppotiId, teamId, accountUserDetails.getId());
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	/**
	 * Accept/Refuse a challenge.
	 */
	@PutMapping("/challenge/answer/{sppotiId}")
	ResponseEntity<Void> sendChallenge(@PathVariable final String sppotiId, @RequestBody final TeamDTO teamDTO)
	{
		
		if (StringUtils.isEmpty(teamDTO.getId()) || StringUtils.isEmpty(teamDTO.getTeamAdverseStatus())) {
			throw new BusinessGlobalException("Team id not found in the request.");
		}
		
		this.sppotiControllerService.chooseOneAdverseTeamFromAllChallengeRequests(sppotiId, teamDTO);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	/**
	 * Update sppoti privacy type PRIVATE / PUBLIC.
	 */
	@PutMapping("/type")
	ResponseEntity<SppotiDTO> updateType(@RequestBody final SppotiDTO dto) {
		
		
		if (!StringUtils.hasText(dto.getId()) && dto.getType() == null) {
			throw new BusinessGlobalException("Sppoti id and status are required to update sppoti type");
		}
		
		if (!dto.getType().equals(SppotiStatus.PRIVATE) && !dto.getType().equals(SppotiStatus.PUBLIC)) {
			throw new BusinessGlobalException("Type can be Private or Public");
		}
		
		return ResponseEntity.accepted()
				.body(this.sppotiControllerService.updateSppotiType(dto.getId(), dto.getType()));
	}
	
	/**
	 * Accept or Refuse team member request to join the team.
	 */
	@PutMapping("/answer/member/join/request")
	ResponseEntity acceptTeamMemberRequestToJoinTheTeam(@PathVariable final String sppotiId,
														@RequestBody final UserDTO dto)
	{
		if (!StringUtils.hasText(dto.getId()) || dto.getTeamStatus() == null || !StringUtils.hasText(sppotiId)) {
			throw new BusinessGlobalException("Sppoti-ID, User-ID or Status not found");
		}
		
		if (!dto.getTeamStatus().equals(GlobalAppStatusEnum.CONFIRMED) &&
				!dto.getTeamStatus().equals(GlobalAppStatusEnum.REFUSED)) {
			throw new BusinessGlobalException("Authorized status are: CONFIRMED / REFUSED");
		}
		
		if (dto.getTeamStatus().equals(GlobalAppStatusEnum.CONFIRMED)) {
			this.sppotiControllerService.confirmTeamRequestSentFromUser(sppotiId, dto);
		} else if (dto.getTeamStatus().equals(GlobalAppStatusEnum.REFUSED)) {
			this.sppotiControllerService.refuseTeamRequestSentFromUser(sppotiId, dto);
		}
		
		return ResponseEntity.accepted().build();
	}
}
