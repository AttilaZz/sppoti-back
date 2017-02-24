package com.fr.rest.controllers.team;

import com.fr.commons.dto.team.TeamRequestDTO;
import com.fr.commons.dto.team.TeamResponseDTO;
import com.fr.rest.service.TeamControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by djenanewail on 2/22/17.
 */

@RestController
@RequestMapping("/team")
public class UpdateTeamController {

    private TeamControllerService teamControllerService;
    private Logger LOGGER = Logger.getLogger(TeamAddController.class);

    @Autowired
    public void setTeamControllerService(TeamControllerService teamControllerService) {
        this.teamControllerService = teamControllerService;
    }

    /**
     * This method update general team informations,
     * Title, Logos, Cover
     *
     * @param teamId             team teamId.
     * @param teamRequestDTO data to update.
     * @return The updated team.
     */
    @PutMapping("/{teamId}")
    public ResponseEntity<TeamResponseDTO> updateTeam(@PathVariable int teamId, @RequestBody TeamRequestDTO teamRequestDTO) {

        boolean canUpdate = false;

        if (teamRequestDTO.getName() == null || teamRequestDTO.getName().isEmpty()) {
            canUpdate = true;
        }

        if(canUpdate){
            teamControllerService.updateTeam(teamId, teamRequestDTO);
        }else{
            LOGGER.error("Update impossible");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
