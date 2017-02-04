package com.fr.rest.controllers.team;

import com.fr.commons.dto.TeamRequest;
import com.fr.models.GlobalAppStatus;
import com.fr.rest.controllers.SppotiController;
import com.fr.rest.service.TeamControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by djenanewail on 2/4/17.
 */

@RestController
@RequestMapping("/team/members")
public class TeamMembersController {

    private TeamControllerService teamControllerService;

    @Autowired
    public void setTeamControllerService(TeamControllerService teamControllerService) {
        this.teamControllerService = teamControllerService;
    }

    private Logger LOGGER = Logger.getLogger(SppotiController.class);

    /**
     * Accept/Refuse team information
     * Update coordinate in the stadium (X, Y)
     *
     * @param memberId
     * @return The updated member information
     */
    @PutMapping("/{teamId}/{memberId}")
    public ResponseEntity<Void> updateInvitationStatus(@PathVariable("memberId") int memberId, @PathVariable int teamId, @RequestBody TeamRequest request) {

        boolean canUpdate = false;

        if (request.getStatus() != null && !request.getStatus().equals(0)) {
            for (GlobalAppStatus status : GlobalAppStatus.values()) {
                if (status.getValue() == request.getStatus()) {
                    canUpdate = true;
                }
            }
        }

        if (request.getxPosition() != null && request.getyPosition() != null) {
            canUpdate = true;
        }

        if (!canUpdate) {
            LOGGER.error("Nothing to update, check your json request ! \n " + request.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            teamControllerService.updateTeamMembers(request, memberId, teamId);

        } catch (RuntimeException e) {
            LOGGER.error("Problem updating team member: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOGGER.info("Team member data updated ! \n " + request.toString());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

}