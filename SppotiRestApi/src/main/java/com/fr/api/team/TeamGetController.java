package com.fr.api.team;

import com.fr.commons.dto.team.TeamResponseDTO;
import com.fr.service.TeamControllerService;
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
    ResponseEntity<TeamResponseDTO> getTeamById(@PathVariable int teamId) {

        TeamResponseDTO teamResponseDTO;

        teamResponseDTO = teamControllerService.getTeamById(teamId);

        return new ResponseEntity<>(teamResponseDTO, HttpStatus.OK);
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

        List<TeamResponseDTO> response = teamControllerService.getAllTeamsByUserId(userId, page);

        return new ResponseEntity<>(response, HttpStatus.OK);
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

        List<TeamResponseDTO> response = teamControllerService.getAllJoinedTeamsByUserId(userId, page);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
