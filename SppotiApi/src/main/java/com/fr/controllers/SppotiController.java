/**
 *
 */
package com.fr.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fr.aop.TraceAuthentification;
import com.fr.controllers.service.SppotiControllerService;
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

    private SppotiControllerService sppotiControllerService;

    @Autowired
    public void setSppotiControllerService(SppotiControllerService sppotiControllerService) {
        this.sppotiControllerService = sppotiControllerService;
    }

    private Logger LOGGER = Logger.getLogger(TraceAuthentification.class);

    private static final String ATT_USER_ID = "USER_ID";

    @PostMapping
    public ResponseEntity<Sppoti> addPost(@RequestBody SppotiRequest newSppoti, HttpServletRequest request) {

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
        String date = newSppoti.getDatetimeCreated();

        // check if id's refers to existing peoples
        Long[] teamPeopleId = newSppoti.getTeamPeopleId();

        // Check if all address element are present
        String spotAddress = newSppoti.getAddress();

        int membersCount = newSppoti.getMaxTeamCount();

        String tags = newSppoti.getTags();

        try {
            sppotiControllerService.verifyAllDataBeforeSaving(titre, sportId, description, date, teamPeopleId,
                    spotAddress, membersCount, tags);

        } catch (Exception e) {
            LOGGER.error("SPPOT-ADD: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        sppotiToSave.setTeamMemnbers(sppotiControllerService.getTeamGame());
        sppotiToSave.setDatetimeCreated(date);
        sppotiToSave.setDescription(description);
        sppotiToSave.setTitre(titre);
        sppotiToSave.setLocation(spotAddress);
        sppotiToSave.setMaxMembersCount(membersCount);

        sppotiToSave.setRelatedSport(sppotiControllerService.getSportGame());

        try {
            sppotiControllerService.saveSpoot(sppotiToSave);

            LOGGER.info("SPOT: has been saved");
            return new ResponseEntity<>(sppotiToSave, HttpStatus.CREATED);
        } catch (Exception e) {

            LOGGER.error("SPOT: Saving problem -- Data Base problem !!");
            LOGGER.error(e.getMessage());

            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }

}