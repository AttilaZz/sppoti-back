package com.fr.rest.controllers.team;

import com.fr.commons.dto.team.TeamRequestDTO;
import com.fr.commons.dto.team.TeamResponseDTO;
import com.fr.rest.service.TeamControllerService;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

/**
 * Created by djenanewail on 1/22/17.
 */

@RestController
@RequestMapping("/team")
public class TeamAddController {

    private TeamControllerService teamControllerService;
    private Logger LOGGER = Logger.getLogger(TeamAddController.class);

    @Autowired
    public void setTeamControllerService(TeamControllerService teamControllerService) {
        this.teamControllerService = teamControllerService;
    }

    /**
     * This service create team
     *
     * @param team           team to add.
     * @param authentication auth object.
     * @return Created team data
     */
    @PostMapping
    public ResponseEntity<TeamResponseDTO> createTeam(@RequestBody TeamRequestDTO team, Authentication authentication) {

        if (team.getName() == null || team.getName().isEmpty()) {
            LOGGER.error("TeamEntity (name) not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (team.getMembers() == null || team.getMembers().isEmpty()) {
            LOGGER.error("TeamEntity (members) not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (team.getSportId() == null) {
            LOGGER.error("TeamEntity (sport id) not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        TeamResponseDTO teamResponseDTO;
        try {
            teamResponseDTO = teamControllerService.saveTeam(team, accountUserDetails.getId());
        } catch (EntityNotFoundException e) {
            LOGGER.error("Spport not found in the request: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(teamResponseDTO, HttpStatus.CREATED);
    }

}