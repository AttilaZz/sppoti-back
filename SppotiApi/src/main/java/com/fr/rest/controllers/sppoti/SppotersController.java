package com.fr.rest.controllers.sppoti;

import com.fr.rest.service.SppotiControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by djenanewail on 2/4/17.
 */
@RestController
@RequestMapping("/sppoti/{sppotiId}/sppoter")
public class SppotersController {

    private SppotiControllerService sppotiControllerService;

    @Autowired
    public void setSppotiControllerService(SppotiControllerService sppotiControllerService) {
        this.sppotiControllerService = sppotiControllerService;
    }

    private Logger LOGGER = Logger.getLogger(SppotiAddController.class);


    @PutMapping("/accept/{userId}")
    public ResponseEntity<String> acceptSppoti(@PathVariable int sppotiId, @PathVariable int userId) {

        try {

            sppotiControllerService.acceptSppoti(sppotiId, userId);

        } catch (RuntimeException e) {
            LOGGER.error("Accept sppoti error: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }


        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

    @PutMapping("/refuse/{userId}")
    public ResponseEntity<String> refuseSppoti(@PathVariable int sppotiId, @PathVariable int userId) {

        try {

            sppotiControllerService.refuseSppoti(userId, sppotiId);

        } catch (RuntimeException e) {
            LOGGER.error("Accept sppoti error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

}