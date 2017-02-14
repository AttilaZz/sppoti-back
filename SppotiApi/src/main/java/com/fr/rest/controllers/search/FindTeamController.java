package com.fr.rest.controllers.search;

import com.fr.commons.dto.TeamResponseDTO;
import com.fr.rest.service.TeamControllerService;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    private final TeamControllerService teamControllerService;

    @Autowired
    public FindTeamController(TeamControllerService teamControllerService) {
        this.teamControllerService = teamControllerService;
    }

    /**
     * @param team
     * @param page
     * @param authentication
     * @return All found teams containing the String (team).
     */
    @GetMapping("/{team}/{page}")
    public ResponseEntity<List<TeamResponseDTO>> findTeam(@PathVariable String team, @PathVariable int page, Authentication authentication) {

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        List<TeamResponseDTO> teamResponseDTOs;
        try {

            teamResponseDTOs = teamControllerService.findAllTeams(team, accountUserDetails.getUuid(), page);

            if (teamResponseDTOs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            }

        } catch (RuntimeException e) {
            LOGGER.error("Find All teams error: " + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>(teamResponseDTOs, HttpStatus.OK);
    }

}
