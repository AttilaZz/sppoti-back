package com.fr.rest.controllers.sppoti;

import com.fr.commons.dto.SppotiResponse;
import com.fr.rest.service.SppotiControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
     * @param id
     * @return 200 status with the target sppoti, 400 status otherwise.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SppotiResponse> getSppotiById(@PathVariable int id) {

        SppotiResponse response;

        try {
            response = sppotiControllerService.getSppotiByUuid(id);
        } catch (RuntimeException e) {
            e.printStackTrace();
            LOGGER.error("Sppoti not found: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * @param id
     * @param page
     * @param request
     * @return All sppoties for a given user
     */
    @GetMapping("/all/{userId}/{page}")
    public ResponseEntity<List<SppotiResponse>> getAllUserSppoties(@PathVariable("userId") int id, @PathVariable int page, HttpServletRequest request) {

        List<SppotiResponse> response;

        try {
            response = sppotiControllerService.getAllUserSppoties(id, page);

            if (response.isEmpty()) {
                LOGGER.info("The user (" + id + ") has no sppoties");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            LOGGER.error("Error getting sppoties: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
