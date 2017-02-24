package com.fr.rest.controllers.sppoti;

import com.fr.commons.dto.sppoti.SppotiRequestDTO;
import com.fr.commons.dto.sppoti.SppotiResponseDTO;
import com.fr.exceptions.NoRightToAcceptOrRefuseChallenge;
import com.fr.rest.service.SppotiControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @param id            id of sppoti.
     * @param sppotiRequest data to update.
     * @return 200 status with the updated sppoti, 400 status otherwise.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SppotiResponseDTO> updateSppoti(@PathVariable int id, @RequestBody SppotiRequestDTO sppotiRequest) {

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

        if (sppotiRequest.getMaxTeamCount() != 0) {
            canUpdate = true;
        }

        try {

            if (canUpdate) {
                sppotiControllerService.updateSppoti(sppotiRequest, id);
            } else {
                throw new IllegalArgumentException("Update not acceptable");
            }

        } catch (IllegalArgumentException e) {
            LOGGER.error("Update not acceptable due to an illegal argument or database problem: \n ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * Accept sppoti challenge: 4
     * Refuse sppoti challenge: 5
     *
     * @param adverseTeamResponseStatus confirm/refuse challenge.
     * @return All sppoti with updated status.
     */
    @PutMapping("/team/adverse/{sppotiId}/{response}")
    public ResponseEntity<SppotiResponseDTO> updateTeamAdverseStatus(@PathVariable("response") int adverseTeamResponseStatus, @PathVariable int sppotiId) {

        if (adverseTeamResponseStatus != 4 && adverseTeamResponseStatus != 5) {
            LOGGER.error("Accepted status are 4 && 5: found:" + adverseTeamResponseStatus);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        SppotiResponseDTO sppotiResponseDTO;
        try {
            sppotiResponseDTO = sppotiControllerService.updateTeamAdverseChallengeStatus(sppotiId, adverseTeamResponseStatus);
        } catch (NoRightToAcceptOrRefuseChallenge e) {
            LOGGER.error("User must be the admin to update status, sppoti id: " + sppotiId, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(sppotiResponseDTO, HttpStatus.ACCEPTED);
    }

}
