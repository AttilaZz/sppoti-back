package com.fr.rest.controllers;

import com.fr.commons.dto.TeamRequest;
import com.fr.rest.service.TeamControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by djenanewail on 1/22/17.
 */

@RestController
@RequestMapping("/team")
public class TeamController {

    private static final String ATT_USER_ID = "USER_ID";

    private TeamControllerService teamControllerService;

    @Autowired
    public void setTeamControllerService(TeamControllerService teamControllerService) {
        this.teamControllerService = teamControllerService;
    }

    @PostMapping
    public void createTeam(@RequestBody TeamRequest team, HttpServletResponse response, HttpServletRequest request) throws IOException {

//        if (team.getCoverPath() == null || team.getCoverPath().isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-TeamRequest (cover path) not found");
//
//        }
//        if (team.getLogoPath() == null || team.getLogoPath().isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-TeamRequest (logo path) not found");
//
//        }
        if (team.getName() == null || team.getName().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-TeamRequest (name) not found");

        }
        if (team.getMemberIdList() == null || team.getMemberIdList().length == 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-TeamRequest (members) not found");

        }

        Long teamCreator = (Long) request.getSession().getAttribute(ATT_USER_ID);


        try {
            teamControllerService.saveTeam(team, teamCreator);
        } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @PutMapping("{/id}")
    public ResponseEntity updateTeam(@PathVariable int id, @RequestBody TeamRequest teamRequest) {

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("{/id}")
    public ResponseEntity deleteTeam(@PathVariable int id) {

        return new ResponseEntity(HttpStatus.OK);
    }
}