package com.fr.api.sppoti;

import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.exception.NotAdminException;
import com.fr.security.AccountUserDetails;
import com.fr.service.SppotiControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

/**
 * Created by djenanewail on 2/5/17.
 */

@RestController
@RequestMapping("/sppoti")
class SppotiUpdateController {

    /**
     * Sppoti controller service.
     */
    private SppotiControllerService sppotiControllerService;

    /**
     * Init service.
     */
    @Autowired
    void setSppotiControllerService(SppotiControllerService sppotiControllerService) {
        this.sppotiControllerService = sppotiControllerService;
    }

    /**
     * Class logger.
     */
    private Logger LOGGER = Logger.getLogger(SppotiAddController.class);

    /**
     * @param sppotiId      sppotiId of sppoti.
     * @param sppotiRequest data to update.
     * @return 200 status with the updated sppoti, 400 status otherwise.
     */
    @PutMapping("/{sppotiId}")
    ResponseEntity<SppotiDTO> updateSppoti(@PathVariable int sppotiId, @RequestBody SppotiDTO sppotiRequest,
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

        if (!StringUtils.isEmpty(sppotiRequest.getTags())) {
            canUpdate = true;
        }

        if (!StringUtils.isEmpty(sppotiRequest.getDescription())) {
            canUpdate = true;
        }

        if (sppotiRequest.getDateTimeStart() != null) {
            canUpdate = true;
        }

        if (!StringUtils.isEmpty(sppotiRequest.getName())) {
            canUpdate = true;
        }

        if (!StringUtils.isEmpty(sppotiRequest.getLocation())) {
            canUpdate = true;
        }

        if (sppotiRequest.getVsTeam() != 0) {
            canUpdate = true;
        }

        if (sppotiRequest.getMaxTeamCount() != 0) {
            canUpdate = true;
        }

        if (canUpdate) {
            sppotiControllerService.updateSppoti(sppotiRequest, sppotiId);
        } else {
            throw new IllegalArgumentException("Update not accepted");
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * @param sppotiId id of the challenged sppoti.
     * @param teamId   team id to add in the challenge.
     */
    @PutMapping("/challenge/send/{sppotiId}/{teamId}")
    ResponseEntity<SppotiDTO> sendChallenge(@PathVariable int sppotiId, @PathVariable int teamId,
                                            Authentication authentication) {

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        sppotiControllerService.sendChallenge(sppotiId, teamId, accountUserDetails.getId());

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * Accept/Refuse a challenge.
     *
     * @param sppotiId sppoti id.
     * @param teamDTO  team DTO containing the id of the accepted team.
     * @return 202 if update done correctly, 400 if not
     */
    @PutMapping("/challenge/answer/{sppotiId}")
    ResponseEntity<Void> sendChallenge(@PathVariable int sppotiId, @RequestBody TeamDTO teamDTO) {

        if (StringUtils.isEmpty(teamDTO.getId()) || StringUtils.isEmpty(teamDTO.getTeamAdverseStatus())) {
            throw new BusinessGlobalException("Team id not found in the request.");
        }

        sppotiControllerService.chooseOneAdverseTeamFromAllRequests(sppotiId, teamDTO);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
