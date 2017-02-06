package com.fr.rest.controllers.team;

import com.fr.commons.dto.TeamRequest;
import com.fr.commons.dto.TeamResponse;
import com.fr.rest.controllers.sppoti.SppotiAddController;
import com.fr.rest.service.TeamControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by djenanewail on 1/22/17.
 */

@RestController
@RequestMapping("/team")
public class TeamController {

    private static final String ATT_USER_ID = "USER_ID";

    private TeamControllerService teamControllerService;
    private Logger LOGGER = Logger.getLogger(SppotiAddController.class);

    @Autowired
    public void setTeamControllerService(TeamControllerService teamControllerService) {
        this.teamControllerService = teamControllerService;
    }

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@RequestBody TeamRequest team, HttpServletResponse response, HttpServletRequest request) {

//        if (team.getCoverPath() == null || team.getCoverPath().isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-TeamRequest (cover path) not found");
//
//        }
//        if (team.getLogoPath() == null || team.getLogoPath().isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-TeamRequest (logo path) not found");
//
//        }

        if (team.getName() == null || team.getName().isEmpty()) {
            LOGGER.error("Team (name) not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (team.getMembers() == null || team.getMembers().isEmpty()) {
            LOGGER.error("Team (members) not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long teamCreator = (Long) request.getSession().getAttribute(ATT_USER_ID);

        TeamResponse teamResponse;
        try {

            teamResponse = teamControllerService.saveTeam(team, teamCreator);

        } catch (RuntimeException e) {

            LOGGER.error("Problème de création de la team: \n" + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>(teamResponse, HttpStatus.CREATED);
    }

    /**
     * This methos update general team informations,
     * Title, Logos, Cover
     *
     * @param id
     * @param teamRequest
     * @return The updated team
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable int id, @RequestBody TeamRequest teamRequest) {


        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * This method delete a team
     *
     * @param id
     * @return 200 status if team was deleted, 400 status otherwise
     */
    @DeleteMapping("{/id}")
    public ResponseEntity deleteTeam(@PathVariable int id) {

        try {

            teamControllerService.deleteTeam(id);

        } catch (RuntimeException e) {
            LOGGER.error("Error deleting team id:" + id);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

}