package com.fr.api.team;

import com.fr.commons.dto.team.TeamRequestDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.security.AccountUserDetails;
import com.fr.service.TeamControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

/**
 * Created by djenanewail on 1/22/17.
 */

@RestController
@RequestMapping("/team")
class TeamAddController {

    private TeamControllerService teamControllerService;
    private Logger LOGGER = Logger.getLogger(TeamAddController.class);

    @Autowired
    void setTeamControllerService(TeamControllerService teamControllerService) {
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
    ResponseEntity<TeamDTO> createTeam(@RequestBody TeamRequestDTO team, Authentication authentication) {

        if (StringUtils.isEmpty(team.getName())) {
            LOGGER.error("TeamEntity (name) not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isEmpty(team.getMembers())) {
            LOGGER.error("TeamEntity (members) not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (team.getSportId() == null) {
            LOGGER.error("TeamEntity (sport id) not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        TeamDTO teamDTO;
        try {
            teamDTO = teamControllerService.saveTeam(team, accountUserDetails.getId());
        } catch (EntityNotFoundException e) {
            LOGGER.error("Spport not found in the request: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("team name already exist: ", e);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(teamDTO, HttpStatus.CREATED);
    }

}