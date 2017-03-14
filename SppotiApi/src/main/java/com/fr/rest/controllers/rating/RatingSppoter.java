package com.fr.rest.controllers.rating;

import com.fr.commons.dto.SppotiRatingDTO;
import com.fr.rest.controllers.sppoti.SppotiAddController;
import com.fr.rest.service.SppotiControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

/**
 * Created by wdjenane on 14/03/2017.
 */
@RestController
@RequestMapping("/rating")
public class RatingSppoter {

    private SppotiControllerService sppotiControllerService;

    @Autowired
    public void setSppotiControllerService(SppotiControllerService sppotiControllerService) {
        this.sppotiControllerService = sppotiControllerService;
    }

    private Logger LOGGER = Logger.getLogger(SppotiAddController.class);

    /**
     *
     * @return rate.
     */
    @PostMapping("/sppoter")
    public ResponseEntity<Void> rateSppoter(@RequestBody @Valid SppotiRatingDTO sppotiRatingDTO){

        try{
            sppotiControllerService.rateSppoter(sppotiRatingDTO);
        }catch (EntityNotFoundException e){
            LOGGER.error(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
