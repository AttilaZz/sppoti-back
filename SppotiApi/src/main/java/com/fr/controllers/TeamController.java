package com.fr.controllers;

import com.fr.commons.dto.SppotiRequest;
import com.fr.controllers.service.TeamControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    TeamControllerService teamControllerService;

    @Autowired
    public TeamControllerService getTeamControllerService() {
        return teamControllerService;
    }

    @PostMapping
    public void createTeam(@RequestBody SppotiRequest.Team team, HttpServletResponse response, HttpServletRequest request) throws IOException {

        if (team.getCoverPath() == null || team.getCoverPath().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-Team (cover path) not found");

        }
        if (team.getLogoPath() == null || team.getLogoPath().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-Team (logo path) not found");

        }
        if (team.getName() == null || team.getName().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-Team (name) not found");

        }
        if (team.getMemberIdList() == null || team.getMemberIdList().length == 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-Team (members) not found");

        }

        Long teamCreator = (Long) request.getSession().getAttribute(ATT_USER_ID);


        try {
            teamControllerService.saveTeam(team, teamCreator);
        } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_CREATED);
    }

}
