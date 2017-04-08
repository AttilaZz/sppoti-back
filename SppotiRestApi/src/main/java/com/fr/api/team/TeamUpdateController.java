package com.fr.api.team;

import com.fr.commons.dto.team.TeamDTO;
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
@RequestMapping("/team")
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
    @PutMapping("/{teamId}")
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

}
