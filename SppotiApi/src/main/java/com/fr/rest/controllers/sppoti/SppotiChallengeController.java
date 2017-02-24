package com.fr.rest.controllers.sppoti;

import com.fr.commons.dto.sppoti.SppotiResponseDTO;
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
 * Created by djenanewail on 2/20/17.
 */

@RestController
@RequestMapping("/sppoti/challenge")
public class SppotiChallengeController {

    private Logger LOGGER = Logger.getLogger(SppotiChallengeController.class);

    private SppotiControllerService sppotiControllerService;

    @Autowired
    public void setSppotiControllerService(SppotiControllerService sppotiControllerService) {
        this.sppotiControllerService = sppotiControllerService;
    }

    @PutMapping("/{sppotiId}/{teamId}")
    public ResponseEntity<SppotiResponseDTO> sendChallenge(@PathVariable int sppotiId, @PathVariable int teamId) {


        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/accept/{sppotiId}/{teamId}")
    public ResponseEntity<SppotiResponseDTO> sacceptChallenge(@PathVariable int sppotiId, @PathVariable int teamId) {


        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/refuse/{sppotiId}/{teamId}")
    public ResponseEntity<SppotiResponseDTO> refuseChallenge(@PathVariable int sppotiId, @PathVariable int teamId) {


        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
