package com.fr.rest.controllers.team;

import com.fr.rest.controllers.sppoti.SppotiAddController;
import com.fr.rest.service.TeamControllerService;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/5/17.
 */

@RestController
@RequestMapping("/team/{teamId}/")
public class TeamUpdateStatusController {

    private TeamControllerService teamControllerService;
    private Logger LOGGER = Logger.getLogger(SppotiAddController.class);

    @Autowired
    public void setTeamControllerService(TeamControllerService teamControllerService) {
        this.teamControllerService = teamControllerService;
    }

    @PutMapping("/accept")
    public ResponseEntity<Void> acceptTeam(@PathVariable int teamId, Authentication authentication) {

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        try {
            teamControllerService.acceptTeam(teamId, accountUserDetails.getUuid());

        } catch (RuntimeException e) {
            LOGGER.error("Error accepting team " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        LOGGER.info("TeamEntity has been accepted");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/refuse")
    public ResponseEntity<Void> refuseTeam(@PathVariable int teamId, Authentication authentication) {

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        try {
            teamControllerService.refuseTeam(teamId, accountUserDetails.getUuid());

        } catch (RuntimeException e) {
            LOGGER.error("Error refusing team " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOGGER.info("TeamEntity has been refused");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
