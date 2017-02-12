package com.fr.rest.controllers.team;

import com.fr.commons.dto.TeamRequestDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.models.GlobalAppStatus;
import com.fr.rest.controllers.sppoti.SppotiAddController;
import com.fr.rest.service.TeamControllerService;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Created by djenanewail on 2/4/17.
 */

@RestController
@RequestMapping("/team/{teamId}/members")
public class TeamMembersController {

    private TeamControllerService teamControllerService;

    @Autowired
    public void setTeamControllerService(TeamControllerService teamControllerService) {
        this.teamControllerService = teamControllerService;
    }

    private Logger LOGGER = Logger.getLogger(SppotiAddController.class);

    /**
     * Accept/Refuse team information
     * Update coordinate in the stadium (X, Y)
     *
     * @param memberId
     * @return The updated member information
     */
    @PutMapping("/{memberId}")
    public ResponseEntity<Void> updateInvitationStatus(@PathVariable("memberId") int memberId, @PathVariable int teamId, @RequestBody TeamRequestDTO request) {

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

    /**
     * Add member for a given team - only admin can add a memeber to his team
     *
     * @return 201 status if memeber has been added
     */
    @PostMapping
    public ResponseEntity<Void> addMember(@PathVariable int teamId, @RequestBody UserDTO user) {

        if (user.getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {

            teamControllerService.addMember(teamId, user);

        } catch (RuntimeException e) {
            LOGGER.error("Error adding user: " + e.getMessage() + "\n Member Id:" + user.getId());
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Delete memeber for a given team - only team admin can delete a member
     *
     * @return 200 status if memeber has been added
     */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable int teamId, @PathVariable int memberId, Authentication authentication) {

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        try {

            teamControllerService.deleteMemberFromTeam(teamId, memberId, accountUserDetails.getUuid());

        } catch (RuntimeException e) {

            LOGGER.error("Error deleting member: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}