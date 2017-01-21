package com.fr.controllers;

import com.fr.controllers.service.SppotiControllerService;
import com.fr.models.SppotiRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@RestController
@RequestMapping("/sppoti")
public class SppotiController {

    private SppotiControllerService sppotiControllerService;

    @Autowired
    public void setSppotiControllerService(SppotiControllerService sppotiControllerService) {
        this.sppotiControllerService = sppotiControllerService;
    }

    private Logger LOGGER = Logger.getLogger(SppotiController.class);

    private static final String ATT_USER_ID = "USER_ID";

    @PostMapping
    public void addPost(@RequestBody SppotiRequest newSppoti, HttpServletResponse response, HttpServletRequest request) throws IOException {

        if (newSppoti.getAddress() == null || newSppoti.getAddress().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Address not found");
        }
        if (newSppoti.getDescription() == null || newSppoti.getDescription().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Description not found");
        }
        if (newSppoti.getMaxTeamCount() == 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Max-Team-Count not found");
        }
        if (newSppoti.getSportId() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sport-Id not found");
        }
        if (newSppoti.getTags() == null || newSppoti.getTags().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tags not found");
        }
        if (newSppoti.getTitre() == null || newSppoti.getTitre().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Title not found");
        }
        if (newSppoti.getDatetimeStart() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Date-start not found");
        }
        if (newSppoti.getMyTeam() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-Team not found");
        } else {
            if (newSppoti.getMyTeam().getCoverPath() == null || newSppoti.getMyTeam().getCoverPath().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-Team (cover path) not found");

            }
            if (newSppoti.getMyTeam().getLogoPath() == null || newSppoti.getMyTeam().getLogoPath().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-Team (logo path) not found");

            }
            if (newSppoti.getMyTeam().getName() == null || newSppoti.getMyTeam().getName().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-Team (name) not found");

            }
            if (newSppoti.getMyTeam().getMemberIdList() == null || newSppoti.getMyTeam().getMemberIdList().length == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Host-Team (members) not found");

            }

        }

        if (newSppoti.getVsTeam() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Guest-Team not found");
        } else {
            if (newSppoti.getVsTeam().getCoverPath() == null || newSppoti.getVsTeam().getCoverPath().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Guest-Team (cover path) not found");

            }
            if (newSppoti.getVsTeam().getLogoPath() == null || newSppoti.getVsTeam().getLogoPath().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Guest-Team (logo path) not found");

            }
            if (newSppoti.getVsTeam().getName() == null || newSppoti.getVsTeam().getName().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Guest-Team (name) not found");

            }
            if (newSppoti.getVsTeam().getMemberIdList() == null || newSppoti.getVsTeam().getMemberIdList().length == 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Guest-Team (members) not found");

            }
        }

        Long sppotiCreator = (Long) request.getSession().getAttribute(ATT_USER_ID);

        try {
            sppotiControllerService.saveSppoti(newSppoti, sppotiCreator);
        } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }

    }

}