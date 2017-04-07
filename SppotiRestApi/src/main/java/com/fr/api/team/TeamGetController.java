package com.fr.api.team;

import com.fr.commons.dto.team.TeamDTO;
import com.fr.service.TeamControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/5/17.
 */

@RestController
@RequestMapping("/team")
class TeamGetController {

    private TeamControllerService teamControllerService;

    @Autowired
    void setTeamControllerService(TeamControllerService teamControllerService) {
        this.teamControllerService = teamControllerService;
    }

    /**
     * Get team data from an id
     *
     * @param teamId team id.
     * @return target team.
     */
    @GetMapping("/{teamId}")
    ResponseEntity<TeamDTO> getTeamById(@PathVariable int teamId) {

        return new ResponseEntity<>(teamControllerService.getTeamById(teamId), HttpStatus.OK);
    }

    /**
     * Get all user teams.
     *
     * @param userId user id.
     * @param page   page number.
     * @return All teams for a giver user Id.
     */
    @GetMapping("/all/{userId}/{page}")
    ResponseEntity getAllTeams(@PathVariable int userId, @PathVariable int page) {

        return new ResponseEntity<>(teamControllerService.getAllTeamsByUserId(userId, page), HttpStatus.OK);
    }

    /**
     * Get all joined teams except created by me.
     *
     * @param userId user id.
     * @param page   page number.
     * @return All teams for a giver user Id.
     */
    @GetMapping("/all/joined/{userId}/{page}")
    ResponseEntity getAllJoinedTeams(@PathVariable int userId, @PathVariable int page) {

        return new ResponseEntity<>(teamControllerService.getAllJoinedTeamsByUserId(userId, page), HttpStatus.OK);
    }

    /**
     *
     * @param userId user id.
     * @param page page number.
     * @return deleted teams.
     */
    @GetMapping("/all/deleted/{userId}/{page}")
    ResponseEntity getAllDeletedTeams(@PathVariable int userId, @PathVariable int page){
        return new ResponseEntity<>(teamControllerService.getAllDeletedTeamsByUserId(userId, page), HttpStatus.OK);
    }

}
