/**
 *
 */
package com.fr.controllers;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.fr.aop.TraceAuthentification;
import com.fr.controllers.service.SppotiControllerService;
import com.fr.exceptions.EmptyArgumentException;
import com.fr.models.JsonPostRequest;
import com.fr.models.SppotiResponse;
import com.fr.models.SppotiRequest;
import com.fr.entities.Sppoti;
import com.fr.entities.Users;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@RestController
@RequestMapping("/sppoti")
public class SppotiController {

    @Autowired
    private SppotiControllerService sppotiControllerService;

    private Logger LOGGER = Logger.getLogger(TraceAuthentification.class);

    private static final String ATT_USER_ID = "USER_ID";

    @PostMapping(value = "/add")
    public ResponseEntity<SppotiResponse> addPost(@RequestBody SppotiRequest newSppoti, HttpServletRequest request) {

        // get current logged user
        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users user = sppotiControllerService.getUserById(userId);
        LOGGER.info("LOGGED User: => " + userId);

        Sppoti sppotiToSave = new Sppoti();
        sppotiToSave.setUserGame(user);

		/*
         * For all element check if the value is not NULL
		 */
        String titre = newSppoti.getTitre();

        // check if the SportModel id is valid
        Long sportId = newSppoti.getSportId();

        String description = newSppoti.getDescription();
        String date = newSppoti.getDate();

        // check if id's refers to existing peoples
        Long[] teamPeopleId = newSppoti.getTeamPeopleId();

        // Check if all address element are present
        String spotAddress = newSppoti.getAddress();

        int membersCount = newSppoti.getMembersCount();

        int type = newSppoti.getType();

        String tags = newSppoti.getTags();

        try {
            sppotiControllerService.verifyAllDataBeforeSaving(titre, sportId, description, date, teamPeopleId,
                    spotAddress, membersCount, type, tags);

        } catch (Exception e) {
            e.getMessage();

        }

//TODO: create a set of USERS from a table of LONG
        sppotiToSave.setTeamMemnbers(null);
        // frToSave.setDatetime(datetime);
        sppotiToSave.setDescription(description);
        sppotiToSave.setTitre(titre);
        sppotiToSave.setGameAddress(spotAddress);

//TODO: create a set of SPORT from a table of LONG
        sppotiToSave.setRelatedSport(null);

        if (sppotiControllerService.saveSpoot(sppotiToSave)) {
            LOGGER.info("SPOT: has been saved");
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        LOGGER.info("SPOT: Saving problem -- Data Base problem !!");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

}