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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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

    @RequestMapping(value = "/add", method = RequestMethod.POST, headers = "content-type=application/x-www-form-urlencoded")
    public ResponseEntity<SppotiResponse> addPost(@ModelAttribute JsonPostRequest json, UriComponentsBuilder ucBuilder,
                                                  HttpServletRequest request) {

        Gson gson = new Gson();
        SppotiRequest newfr = null;
        if (json != null) {
            try {
                newfr = gson.fromJson(json.getJson(), SppotiRequest.class);

                LOGGER.info("SPPOTI data sent by user: " + new ObjectMapper().writeValueAsString(newfr));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.info("SPPOTI: Data sent by user are invalid");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // get current logged user
        Long userId = (Long) request.getSession().getAttribute(ATT_USER_ID);
        Users user = sppotiControllerService.getUserById(userId);
        LOGGER.info("LOGGED User: => " + userId);

        Sppoti frToSave = new Sppoti();
        frToSave.setUserGame(user);

		/*
         * For all element check if the value is not NULL
		 */
        String titre = newfr.getTitre();

        // check if the sport id is valid
        Long sportId = newfr.getSportId();

        String description = newfr.getDescription();
        String date = newfr.getDate();

        // check if id's refers to existing peoples
        Long[] teamPeopleId = newfr.getTeamPeopleId();

        // Check if all address element are present
        String spotAddress = newfr.getAddress();

        int membersCount = newfr.getMembersCount();

        int type = newfr.getType();

        String tags = newfr.getTags();

        try {
            sppotiControllerService.verifyAllDataBeforeSaving(titre, sportId, description, date, teamPeopleId,
                    spotAddress, membersCount, type, tags);

        } catch (Exception e) {
            if (e instanceof EmptyArgumentException) {
                LOGGER.info(e.getMessage());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else if (e instanceof EntityNotFoundException) {
                LOGGER.info(e.getMessage());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else if (e instanceof InterruptedException) {
                // problem in thread sleep -- see the service
            }
        }

//TODO: create a set of USERS from a table of LONG
        frToSave.setTeamMemnbers(null);

        // frToSave.setDatetime(datetime);
        frToSave.setDescription(description);
        frToSave.setTitre(titre);
        frToSave.setGameAddress(spotAddress);

//TODO: create a set of SPORT from a table of LONG
        frToSave.setRelatedSport(null);

        if (sppotiControllerService.saveSpoot(frToSave)) {
            LOGGER.info("SPOT: has been saved");
            return new ResponseEntity<SppotiResponse>(HttpStatus.ACCEPTED);
        }

        LOGGER.info("SPOT: Saving problem -- Data Base problem !!");
        return new ResponseEntity<SppotiResponse>(HttpStatus.FORBIDDEN);

    }

}
