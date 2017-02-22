package com.fr.rest.controllers.sppoti;

import com.fr.commons.dto.SppotiResponseDTO;
import com.fr.rest.service.SppotiControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by djenanewail on 2/5/17.
 */
@RestController
@RequestMapping("/sppoti")
public class SppotiGetController {

    private SppotiControllerService sppotiControllerService;

    @Autowired
    public void setSppotiControllerService(SppotiControllerService sppotiControllerService) {
        this.sppotiControllerService = sppotiControllerService;
    }

    private Logger LOGGER = Logger.getLogger(SppotiAddController.class);


    /**
     * @param id sppoti id to find.
     * @return 200 status with the target sppoti, 400 status otherwise.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SppotiResponseDTO> getSppotiById(@PathVariable Integer id) {

        SppotiResponseDTO response = sppotiControllerService.getSppotiByUuid(id);

        LOGGER.info("Sppoti has been returned: " + response);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * @param id   user id.
     * @param page page number.
     * @return All sppoties created by the given user id.
     */
    @GetMapping("/all/{userId}/{page}")
    public ResponseEntity<List<SppotiResponseDTO>> getAllUserSppoties(@PathVariable("userId") int id, @PathVariable int page) {

        try {
            List<SppotiResponseDTO> response = sppotiControllerService.getAllUserSppoties(id, page);

            LOGGER.info("The user (" + id + ") has created (" + response.size() + ") sppoties");

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            LOGGER.error("Error getting sppoties: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * @param id   user id.
     * @param page page number.
     * @return All sppoties that user joined.
     */
    @GetMapping("/all/joined/{userId}/{page}")
    public ResponseEntity<List<SppotiResponseDTO>> getAllJoinedUserSppoties(@PathVariable("userId") int id, @PathVariable int page) {

        try {
            List<SppotiResponseDTO> response = sppotiControllerService.getAllJoinedSppoties(id, page);
            LOGGER.info("The user (" + id + ") has joined (" + response.size() + ") sppoties");

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            LOGGER.error("Error getting sppoties: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
