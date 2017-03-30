package com.fr.api.team;

import com.fr.service.TeamControllerService;
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
class TeamDeleteTeam {

    private static final String ATT_USER_ID = "USER_ID";

    private TeamControllerService teamControllerService;
    private Logger LOGGER = Logger.getLogger(TeamAddController.class);

    @Autowired
    void setTeamControllerService(TeamControllerService teamControllerService) {
        this.teamControllerService = teamControllerService;
    }

    /**
     * This method delete a team
     *
     * @param id team id.
     * @return 200 status if team was deleted, 400 status otherwise
     */
    @DeleteMapping("{/id}")
    ResponseEntity deleteTeam(@PathVariable int id) {

        teamControllerService.deleteTeam(id);

        LOGGER.info("TeamEntity has been deleted (" + id + ")");
        return new ResponseEntity(HttpStatus.OK);
    }

}