package com.fr.api.sppoti;

import com.fr.commons.dto.sppoti.SppotiRequestDTO;
import com.fr.commons.dto.sppoti.SppotiResponseDTO;
import com.fr.commons.exception.NoRightToAcceptOrRefuseChallenge;
import com.fr.commons.exception.NotAdminException;
import com.fr.service.SppotiControllerService;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

/**
 * Created by djenanewail on 2/5/17.
 */

@RestController
@RequestMapping("/sppoti")
class SppotiUpdateController {


    private SppotiControllerService sppotiControllerService;

    @Autowired
    void setSppotiControllerService(SppotiControllerService sppotiControllerService) {
        this.sppotiControllerService = sppotiControllerService;
    }

    private Logger LOGGER = Logger.getLogger(SppotiAddController.class);


    /**
     * @param sppotiId      sppotiId of sppoti.
     * @param sppotiRequest data to update.
     * @return 200 status with the updated sppoti, 400 status otherwise.
     */
    @PutMapping("/{sppotiId}")
    ResponseEntity<SppotiResponseDTO> updateSppoti(@PathVariable int sppotiId, @RequestBody SppotiRequestDTO sppotiRequest,
                                                   Authentication authentication) {

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        //throws exception if user is not the sppoti admin
        try {
            sppotiControllerService.isSppotiAdmin(sppotiId, accountUserDetails.getId());
        } catch (EntityNotFoundException e) {
            LOGGER.error("Sppoti not found", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NotAdminException e) {
            LOGGER.error("Acceess denied, u're not the sppoti admin");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

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
                sppotiControllerService.updateSppoti(sppotiRequest, sppotiId);
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
    @PutMapping("/challenge/{sppotiId}/{response}")
    ResponseEntity<SppotiResponseDTO> updateTeamAdverseChallengeStatus(@PathVariable("response")
                                                                               int adverseTeamResponseStatus,
                                                                       @PathVariable int sppotiId) {

        if (adverseTeamResponseStatus != 4 && adverseTeamResponseStatus != 5) {
            LOGGER.error("Accepted status are 4 && 5: found:" + adverseTeamResponseStatus);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        SppotiResponseDTO sppotiResponseDTO;
        try {
            sppotiResponseDTO = sppotiControllerService.updateTeamAdverseChallengeStatus(sppotiId,
                    adverseTeamResponseStatus);
        } catch (NoRightToAcceptOrRefuseChallenge e) {
            LOGGER.error("User must be the admin to update status, sppoti sppotiId: " + sppotiId, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(sppotiResponseDTO, HttpStatus.ACCEPTED);
    }

    /**
     * @param sppotiId id of the challenged sppoti.
     * @param teamId   team id to add in the challenge.
     */
    @PutMapping("/challenge/send/{sppotiId}/{teamId}")
    ResponseEntity<SppotiResponseDTO> sendChallenge(@PathVariable int sppotiId, @PathVariable int teamId,
                                                    Authentication authentication) {

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        try {
            sppotiControllerService.sendChallenge(sppotiId, teamId, accountUserDetails.getId());
        } catch (EntityNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
