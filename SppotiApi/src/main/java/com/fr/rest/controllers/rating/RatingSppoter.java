package com.fr.rest.controllers.rating;

import com.fr.commons.dto.SppotiRatingDTO;
import com.fr.exceptions.BusinessGlobalException;
import com.fr.rest.controllers.sppoti.SppotiAddController;
import com.fr.rest.service.SppotiControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

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
     *  Evaluate other sppoters in same sppoti.
     */
    @PostMapping("/sppoter/{sppotiId}")
    public ResponseEntity<Void> rateSppoter(@PathVariable int sppotiId, @RequestBody List<SppotiRatingDTO> sppotiRatingDTO) {

        sppotiRatingDTO.forEach(sp -> {
            if (sp.getStars() == null || (sp.getStars() > 10 || sp.getStars() < 0)) {
                throw new BusinessGlobalException("Stars count are not correct in the request");
            }

            if(sp.getSppoterRatedId() == null){
                throw new BusinessGlobalException("Rated sppoter id is required");
            }
        });

        try {
            sppotiControllerService.rateSppoters(sppotiRatingDTO, sppotiId);
        } catch (EntityNotFoundException e) {
            LOGGER.error(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}