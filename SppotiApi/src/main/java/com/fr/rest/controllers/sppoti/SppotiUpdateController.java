package com.fr.rest.controllers.sppoti;

import com.fr.commons.dto.SppotiRequest;
import com.fr.commons.dto.SppotiResponse;
import com.fr.rest.service.SppotiControllerService;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Created by djenanewail on 2/5/17.
 */

@RestController
@RequestMapping("/sppoti")
public class SppotiUpdateController {


    private SppotiControllerService sppotiControllerService;

    @Autowired
    public void setSppotiControllerService(SppotiControllerService sppotiControllerService) {
        this.sppotiControllerService = sppotiControllerService;
    }

    private Logger LOGGER = Logger.getLogger(SppotiAddController.class);


    /**
     * @param id
     * @param sppotiRequest
     * @return 200 status with the updated sppoti, 400 status otherwise.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SppotiResponse> updateSppoti(@PathVariable int id, @RequestBody SppotiRequest sppotiRequest, Authentication authentication) {

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        boolean canUpdate = false;

        if (sppotiRequest.getTags() != null && !sppotiRequest.getTags().isEmpty()) {
            canUpdate = true;
        }

        if (sppotiRequest.getDescription() != null && !sppotiRequest.getDescription().isEmpty()) {
            canUpdate = true;
        }

        if (sppotiRequest.getAddress() != null && !sppotiRequest.getAddress().isEmpty()) {
            canUpdate = true;
        }

        if (sppotiRequest.getDatetimeStart() != null) {
            canUpdate = true;
        }

        if (sppotiRequest.getTitre() != null && !sppotiRequest.getTitre().isEmpty()) {
            canUpdate = true;
        }

        if (sppotiRequest.getVsTeam() != 0) {
            canUpdate = true;
        }

        if(sppotiRequest.getMaxTeamCount() != 0){
            canUpdate = true;
        }

        try {

            if (canUpdate) {
                sppotiControllerService.updateSppoti(sppotiRequest, id, accountUserDetails.getUuid());
            } else {
                throw new IllegalArgumentException("Update not acceptable");
            }

        } catch (RuntimeException e) {
            LOGGER.error("Update not acceptable due to an illegal argument or database problem: \n " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
