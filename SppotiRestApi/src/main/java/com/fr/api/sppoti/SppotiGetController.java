package com.fr.api.sppoti;

import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.service.SppotiControllerService;
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
class SppotiGetController {

    /**
     * Sppoti controller service.
     */
    private SppotiControllerService sppotiControllerService;

    /**
     * Init Service.
     */
    @Autowired
    void setSppotiControllerService(SppotiControllerService sppotiControllerService) {
        this.sppotiControllerService = sppotiControllerService;
    }

    /**
     * Controller logger.
     */
    private Logger LOGGER = Logger.getLogger(SppotiAddController.class);


    /**
     * @param id sppoti id to find.
     * @return 200 status with the target sppoti, 400 status otherwise.
     */
    @GetMapping("/{id}")
    ResponseEntity<SppotiDTO> getSppotiById(@PathVariable Integer id) {

        return new ResponseEntity<>(sppotiControllerService.getSppotiByUuid(id), HttpStatus.OK);

    }

    /**
     * Get All sppoties created by the given user id.
     *
     * @param id   user id.
     * @param page page number.
     * @return List of {@link SppotiDTO}
     */
    @GetMapping("/all/{userId}/{page}")
    ResponseEntity<List<SppotiDTO>> getAllUserSppoties(@PathVariable("userId") int id, @PathVariable int page) {

        try {
            List<SppotiDTO> response = sppotiControllerService.getAllUserSppoties(id, page);

            LOGGER.info("The user (" + id + ") has created (" + response.size() + ") sppoties");

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            LOGGER.error("Error getting sppoties: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Get All sppoties that user joined.
     *
     * @param id   user id.
     * @param page page number.
     * @return List of {@link SppotiDTO}
     */
    @GetMapping("/all/joined/{userId}/{page}")
    ResponseEntity<List<SppotiDTO>> getAllJoinedUserSppoties(@PathVariable("userId") int id, @PathVariable int page) {

        List<SppotiDTO> response = sppotiControllerService.getAllJoinedSppoties(id, page);
        LOGGER.info("The user (" + id + ") has joined (" + response.size() + ") sppoties");

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * All confirmed sppoties that user joined.
     *
     * @param userId user id.
     * @param page   page number.
     * @return List of {@link SppotiDTO}
     */
    @GetMapping("/all/confirmed/{userId}/{page}")
    ResponseEntity<List<SppotiDTO>> getAllConfirmedSppoties(@PathVariable int userId, @PathVariable int page) {

        List<SppotiDTO> response = sppotiControllerService.getAllConfirmedSppoties(userId, page);
        LOGGER.info("The user (" + userId + ") has joined (" + response.size() + ") sppoties");

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * Get All refused sppoties that user asked to join.
     *
     * @param userId user id.
     * @param page   page number.
     * @return List of {@link SppotiDTO}
     */
    @GetMapping("/all/refused/{userId}/{page}")
    ResponseEntity<List<SppotiDTO>> getAllRefusedSppoties(@PathVariable int userId, @PathVariable int page) {

        List<SppotiDTO> response = sppotiControllerService.getAllRefusedSppoties(userId, page);
        LOGGER.info("The user (" + userId + ") has joined (" + response.size() + ") sppoties");

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
