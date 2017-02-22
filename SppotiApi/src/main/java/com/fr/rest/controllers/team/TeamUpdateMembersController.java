package com.fr.rest.controllers.team;

import com.fr.commons.dto.TeamRequestDTO;
import com.fr.commons.dto.TeamResponseDTO;
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
@RequestMapping("/team/{teamId}/member")
public class TeamUpdateMembersController {

    private TeamControllerService teamControllerService;

    @Autowired
    public void setTeamControllerService(TeamControllerService teamControllerService) {
        this.teamControllerService = teamControllerService;
    }

    private Logger LOGGER = Logger.getLogger(SppotiAddController.class);

    /**
     * Accept/Refuse team information.
     * Update coordinate in the stadium (X, Y).
     *
     * @param memberId team member id.
     * @return The updated member information.
     */
    @PutMapping("/{memberId}")
    public ResponseEntity<Void> updateInvitationStatus(@PathVariable("memberId") int memberId, @PathVariable int teamId, @RequestBody TeamRequestDTO teamDto) {

        boolean canUpdate = false;

        if (teamDto.getStatus() != null && !teamDto.getStatus().equals(0)) {
            for (GlobalAppStatus status : GlobalAppStatus.values()) {
                if (status.getValue() == teamDto.getStatus()) {
                    canUpdate = true;
                }
            }
        }

        if (teamDto.getxPosition() != null && teamDto.getyPosition() != null) {
            canUpdate = true;
        }

        if (!canUpdate) {
            LOGGER.error("Nothing to update, check your json teamDto ! \n " + teamDto);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            teamControllerService.updateTeamMembers(teamDto, memberId, teamId);

        } catch (RuntimeException e) {
            LOGGER.error("Problem updating team member: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOGGER.info("TeamEntity member data updated ! \n " + teamDto.toString());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

    /**
     * @param teamId   team id.
     * @param memberId memeber id.
     * @return 202 status if captain updated.
     */
    @PutMapping("/captain/{memberId}")
    public ResponseEntity<TeamResponseDTO> updateTeamCaptain(@PathVariable int teamId, @PathVariable int memberId) {

        teamControllerService.updateTeamCaptain(teamId, memberId);

        LOGGER.info("Team captain has been changed to: " + memberId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * Delete memeber for a given team - only team admin can delete a member
     *
     * @return 200 status if memeber has been added
     */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable int teamId, @PathVariable int memberId, Authentication authentication) {

        AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();

        teamControllerService.deleteMemberFromTeam(teamId, memberId, accountUserDetails.getUuid());

        LOGGER.info("Team member has been deleted : " + memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Add member for a given team - only admin can add a memeber to his team.
     *
     * @return 201 status if memeber has been added.
     */
    @PostMapping
    public ResponseEntity<Void> addMember(@PathVariable int teamId, @RequestBody UserDTO user) {

        if (user.getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserDTO savedTeamMember = teamControllerService.addMember(teamId, user);

        LOGGER.info("Team member has been added ! " + savedTeamMember);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}