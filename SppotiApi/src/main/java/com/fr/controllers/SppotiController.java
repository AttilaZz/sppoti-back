package com.fr.controllers;

import com.fr.controllers.service.SppotiControllerService;
import com.fr.commons.dto.SppotiRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity addPost(@RequestBody SppotiRequest newSppoti, HttpServletResponse response, HttpServletRequest request) throws IOException {

        if (newSppoti.getAddress() == null || newSppoti.getAddress().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Address not found");
        }
//        if (newSppoti.getDescription() == null || newSppoti.getDescription().isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Description not found");
//        }
        if (newSppoti.getMaxTeamCount() == 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Max-TeamRequest-Count not found");
        }
        if (newSppoti.getSportId() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sport-Id not found");
        }
//        if (newSppoti.getTags() == null || newSppoti.getTags().isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tags not found");
//        }
        if (newSppoti.getTitre() == null || newSppoti.getTitre().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Title not found");
        }
        if (newSppoti.getDatetimeStart() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Date-start not found");
        }
        if (newSppoti.getMyTeamId() == 0 && newSppoti.getMyTeam() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "TeamHostModel && TeamHostId not found");

        }

//        if (newSppoti.getVsTeam() == 0) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Adverse team id not found");
//        }

        Long sppotiCreator = (Long) request.getSession().getAttribute(ATT_USER_ID);

        try {
            sppotiControllerService.saveSppoti(newSppoti, sppotiCreator);
            return ResponseEntity.status(HttpStatus.CREATED).body("");
        } catch (RuntimeException e) {
            e.printStackTrace();
            LOGGER.error("Ajout de sppoti imposssible: " + e.getMessage());
            return ResponseEntity.badRequest().body("Ajout de sppoti imposssible");

        }

    }

}