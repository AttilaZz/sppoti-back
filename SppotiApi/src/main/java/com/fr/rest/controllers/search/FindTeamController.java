package com.fr.rest.controllers.search;

import com.fr.commons.dto.TeamResponseDTO;
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
 * Created by wdjenane on 10/02/2017.
 */
@RestController
@RequestMapping("/find/team")
public class FindTeamController {

    private Logger LOGGER = Logger.getLogger(FindTeamController.class);
    private static final int MY_TEAM_SEARCH = 1;
    private static final int ALL_TEAM_SEARCH = 0;

    private final TeamControllerService teamControllerService;

    @Autowired
    public FindTeamController(TeamControllerService teamControllerService) {
        this.teamControllerService = teamControllerService;
    }

    /**
     * @param team team to find.
     * @param page page number.
     * @return All found teams containing the String (team).
     */
    @GetMapping("/" + MY_TEAM_SEARCH + "/{team}/{page}")
    public ResponseEntity<List<TeamResponseDTO>> findMyTeams(@PathVariable String team, @PathVariable int page) {

        try {
            return new ResponseEntity<>(teamControllerService.findAllMyTeams(team, page), HttpStatus.OK);
        } catch (RuntimeException e) {
            LOGGER.error("Find All my teams error: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * @param team team to find.
     * @param page page number.
     * @return All found teams containing the String (team).
     */
    @GetMapping("/" + ALL_TEAM_SEARCH + "/{team}/{page}")
    public ResponseEntity<List<TeamResponseDTO>> findAllTeams(@PathVariable String team, @PathVariable int page) {

        try {
            return new ResponseEntity<>(teamControllerService.findAllTeams(team, page), HttpStatus.OK);
        } catch (RuntimeException e) {
            LOGGER.error("Find All teams error: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * @param team  team to find.
     * @param page  page number.
     * @param sport sport id.
     * @return All found teams containing the String (team) and linked to the sport in parameter.
     */
    @GetMapping("/" + ALL_TEAM_SEARCH + "/{team}/{sportId}/{page}")
    public ResponseEntity<List<TeamResponseDTO>> findAllTeams(@PathVariable String team, @PathVariable int page, @PathVariable Long sport) {

        try {
            return new ResponseEntity<>(teamControllerService.findAllTeamsBySport(team, sport, page), HttpStatus.OK);
        } catch (RuntimeException e) {
            LOGGER.error("Find All teams error: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}