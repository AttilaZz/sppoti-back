package com.fr.rest.controllers.team;

import com.fr.commons.dto.TeamRequestDTO;
import com.fr.commons.dto.TeamResponseDTO;
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
public class TeamAddController {

    private static final String ATT_USER_ID = "USER_ID";

    private TeamControllerService teamControllerService;
    private Logger LOGGER = Logger.getLogger(TeamAddController.class);

    @Autowired
    public void setTeamControllerService(TeamControllerService teamControllerService) {
        this.teamControllerService = teamControllerService;
    }

    /**
     * This service create team
     *
     * @param team
     * @param request
     * @return Created team data
     */
    @PostMapping
    public ResponseEntity<TeamResponseDTO> createTeam(@RequestBody TeamRequestDTO team, HttpServletRequest request) {

//        if (team.getCoverPath() == null || team.getCoverPath().isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-TeamRequestDTO (cover path) not found");
//
//        }
//        if (team.getLogoPath() == null || team.getLogoPath().isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-TeamRequestDTO (logo path) not found");
//
//        }
        if (team.getName() == null || team.getName().isEmpty()) {
            LOGGER.error("TeamEntity (name) not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (team.getMembers() == null || team.getMembers().isEmpty()) {
            LOGGER.error("TeamEntity (members) not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(team.getSportId() == null){
            LOGGER.error("TeamEntity (sport id) not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long teamCreator = (Long) request.getSession().getAttribute(ATT_USER_ID);

        TeamResponseDTO teamResponseDTO;
        try {

            teamResponseDTO = teamControllerService.saveTeam(team, teamCreator);

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Problème de création de la team: " + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>(teamResponseDTO, HttpStatus.CREATED);
    }

    /**
     * This method update general team informations,
     * Title, Logos, Cover
     *
     * @param id
     * @param teamRequestDTO
     * @return The updated team
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> updateTeam(@PathVariable int id, @RequestBody TeamRequestDTO teamRequestDTO) {


        return new ResponseEntity<>(HttpStatus.OK);
    }

}