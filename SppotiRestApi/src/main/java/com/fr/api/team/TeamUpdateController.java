package com.fr.api.team;

import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.security.AccountUserDetails;
import com.fr.service.TeamControllerService;
import org.apache.log4j.Logger;
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
class TeamUpdateController {

    /**
     * Team controller service.
     */
    private TeamControllerService teamControllerService;

    /**
     * Class logger.
     */
    private Logger LOGGER = Logger.getLogger(TeamAddController.class);

    /**
     * Init controller services.
     */
    @Autowired
    void setTeamControllerService(TeamControllerService teamControllerService) {
        this.teamControllerService = teamControllerService;
    }

    /**
     * This method update general team informations,
     * Title, Logos, Cover
     *
     * @param teamId         team teamId.
     * @param teamRequestDTO data to update.
     * @return The updated team.
     */
    @PutMapping
    ResponseEntity<TeamDTO> updateTeam(@PathVariable int teamId, @RequestBody TeamDTO teamRequestDTO, Authentication authentication) {

        boolean canUpdate = false;

        //Update team name.
        if (!StringUtils.isEmpty(teamRequestDTO.getName())) {
            canUpdate = true;
        }

        //Update team logos.
        if (!StringUtils.isEmpty(teamRequestDTO.getLogoPath())) {
            canUpdate = true;
        }

        //Update team cover.
        if (!StringUtils.isEmpty(teamRequestDTO.getCoverPath())) {
            canUpdate = true;
        }

        //Update team color.
        if (!StringUtils.isEmpty(teamRequestDTO.getColor())) {
            canUpdate = true;
        }

        int connectedUserId = ((AccountUserDetails) authentication.getPrincipal()).getUuid();
        teamRequestDTO.setId(teamId);
        if (canUpdate) {
            teamControllerService.updateTeam(connectedUserId, teamRequestDTO);
        } else {
            LOGGER.error("Update impossible");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * Accept team invitation.
     *
     * @param teamId team id.
     * @return 202 http status if updated succeed.
     */
    @PutMapping("/accept")
    ResponseEntity<Void> acceptTeam(@PathVariable int teamId, Authentication authentication) {

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        teamControllerService.acceptTeam(teamId, accountUserDetails.getUuid());

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * Refuse team invitation.
     *
     * @param teamId team id.
     * @return 202 http status if updated succeed.
     */
    @PutMapping("/refuse")
    ResponseEntity<Void> refuseTeam(@PathVariable int teamId, Authentication authentication) {

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        teamControllerService.refuseTeam(teamId, accountUserDetails.getUuid());

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * Accept / Refuse sppoti admin challenge
     *
     * @param dto sppoti dto, containing sppoti id and response to challenge.
     * @return 202 http status if updated succeed.
     */
    @PutMapping("/challenge")
    ResponseEntity<TeamDTO> requestChallenge(@PathVariable int teamId, @RequestBody SppotiDTO dto) {

        if (dto.getId() == null || !StringUtils.hasText(dto.getTeamAdverseStatus())) {
            throw new BusinessGlobalException("Sppoti id or team status missing");
        }

        boolean statusExist = false;
        for (GlobalAppStatusEnum status : GlobalAppStatusEnum.values()) {
            if (status.name().equals(dto.getTeamAdverseStatus())) {
                statusExist = true;
            }
        }
        if (!statusExist) {
            throw new BusinessGlobalException("Status must be (CONFIRMED) or (REFUSED)");
        }

        TeamDTO adverseTeam = teamControllerService.requestToSppotiAdminChallenge(dto, teamId);

        return new ResponseEntity<>(adverseTeam, HttpStatus.ACCEPTED);

    }

}
