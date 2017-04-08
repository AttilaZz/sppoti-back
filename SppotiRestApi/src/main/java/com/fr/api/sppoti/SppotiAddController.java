package com.fr.api.sppoti;

import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.service.SppotiControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@RestController
@RequestMapping("/sppoti")
class SppotiAddController {

    private SppotiControllerService sppotiControllerService;

    @Autowired
    void setSppotiControllerService(SppotiControllerService sppotiControllerService) {
        this.sppotiControllerService = sppotiControllerService;
    }

    /**
     * Logger.
     */
    private Logger LOGGER = Logger.getLogger(SppotiAddController.class);

    /**
     * @param newSppoti sppoti to save.
     * @return 201 status && SppotiEntity object with the inserted data, 400 status otherwise.
     */
    @PostMapping
    ResponseEntity<SppotiDTO> addSppoti(@RequestBody @Valid SppotiDTO newSppoti) {

        if (newSppoti.getMaxTeamCount() == 0) {
            LOGGER.error("Max-TeamRequestDTO-Count not found");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

        if (newSppoti.getMyTeamId() == 0 && newSppoti.getTeamHost() == null) {
            LOGGER.error("TeamHostModel && TeamHostId not found ");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        SppotiDTO sppotiDTO = sppotiControllerService.saveSppoti(newSppoti);

        return new ResponseEntity<>(sppotiDTO, HttpStatus.CREATED);

    }

}