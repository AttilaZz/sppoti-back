package com.fr.rest.controllers.team;

import com.fr.rest.service.TeamControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/14/17.
 */

@RestController
@RequestMapping("/team")
public class TeamDeleteTeam {

    private static final String ATT_USER_ID = "USER_ID";

    private TeamControllerService teamControllerService;
    private Logger LOGGER = Logger.getLogger(TeamAddController.class);

    @Autowired
    public void setTeamControllerService(TeamControllerService teamControllerService) {
        this.teamControllerService = teamControllerService;
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
            LOGGER.error("Error deleting team id:" + id + " - ", e);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        LOGGER.info("Team has been deleted (" + id + ")");
        return new ResponseEntity(HttpStatus.OK);
    }

}
