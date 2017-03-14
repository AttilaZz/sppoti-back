package com.fr.rest.controllers.sppoti;

import com.fr.commons.dto.SppotiRatingDTO;
import com.fr.rest.service.SppotiControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

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

    /**
     *
     * @param sppotiId sppoti id.
     * @param userId user id.
     * @return 202 status if sppoti status updated.
     */
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

    /**
     *
     * @param sppotiId sppoti id.
     * @param userId user id.
     * @return 202 status if sppoti status updated.
     */
    @PutMapping("/refuse/{userId}")
    public ResponseEntity<Void> refuseSppoti(@PathVariable int sppotiId, @PathVariable int userId) {

        try {

            sppotiControllerService.refuseSppoti(userId, sppotiId);

        } catch (RuntimeException e) {
            LOGGER.error("Accept sppoti error: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

}