package com.fr.rest.controllers.team;

import com.fr.commons.dto.TeamResponse;
import com.fr.rest.controllers.sppoti.SppotiAddController;
import com.fr.rest.service.TeamControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by djenanewail on 2/5/17.
 */

@RestController
@RequestMapping("/team")
public class TeamGetController {

    private static final String ATT_USER_ID = "USER_ID";

    private TeamControllerService teamControllerService;
    private Logger LOGGER = Logger.getLogger(SppotiAddController.class);

    @Autowired
    public void setTeamControllerService(TeamControllerService teamControllerService) {
        this.teamControllerService = teamControllerService;
    }

    /**
     * Get team data from an id
     *
     * @param teamId
     * @return target team
     */
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable int teamId) {

        TeamResponse teamResponse;

        try {
            teamResponse = teamControllerService.getTeamById(teamId);
        } catch (RuntimeException e) {
            LOGGER.error("Error retrieving team: " + e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(teamResponse, HttpStatus.OK);
    }

    /**
     * @param userId
     * @param page
     * @return All teams for a giver user Id
     */
    @GetMapping("/all/{userId}/{page}")
    public ResponseEntity getAllTeams(@PathVariable int userId, @PathVariable int page) {

        List<TeamResponse> response;

        try {
            response = teamControllerService.getAllTeamsByUserId(userId, page);

            if (response.isEmpty()) {
                LOGGER.info("User id (" + userId + ") has no teams");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            LOGGER.error("Error trying to get teams: " + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
