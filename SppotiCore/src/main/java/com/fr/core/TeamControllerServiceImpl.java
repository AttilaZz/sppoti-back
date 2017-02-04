package com.fr.core;

import com.fr.commons.dto.TeamRequest;
import com.fr.commons.dto.TeamResponse;
import com.fr.entities.Users_team;
import com.fr.models.GlobalAppStatus;
import com.fr.rest.service.TeamControllerService;
import com.fr.entities.Team;
import com.fr.entities.Users;
import com.fr.exceptions.HostMemberNotFoundException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
public class TeamControllerServiceImpl extends AbstractControllerServiceImpl implements TeamControllerService {

    @Override
    public void saveTeam(TeamRequest team, Long adminId) {
        Team teamToSave = new Team();

        teamToSave.setName(team.getName());

        if (team.getCoverPath() != null && !team.getCoverPath().isEmpty()) {
            teamToSave.setCoverPath(team.getCoverPath());
        }

        if (team.getLogoPath() != null && !team.getLogoPath().isEmpty()) {
            teamToSave.setLogoPath(team.getLogoPath());
        }

        try {

            teamToSave.setUsers_teams(getTeamMembersEntityFromDto(team.getMembers(), teamToSave));

        } catch (RuntimeException e) {
            LOGGER.error("One of the team id not found: " + e.getMessage());
            throw new HostMemberNotFoundException("TeamRequest (members) one of the team dosn't exist");

        }

        Users owner = userRepository.findOne(adminId);
        Set<Users> admins = new HashSet<Users>();
        admins.add(owner);
        teamToSave.setAdmins(admins);

        Set<Team> teams = new HashSet<Team>();
        teams.add(teamToSave);
        owner.setTeamAdmin(teams);

        teamRepository.save(teamToSave);

    }

    @Override
    public TeamResponse getTeamById(int teamId) {

        Team team = teamRepository.findByUuid(teamId);
        if (team == null) {
            throw new EntityNotFoundException("Team id not found");
        }

        TeamResponse teamResponse = new TeamResponse();

        //TODO: Fill response

        return teamResponse;
    }

    @Override
    public void updateTeamMembers(TeamRequest request, int memberId, int teamId) {

        Users_team usersTeam = teamMembersRepository.findByUsersUuidAndTeamsUuid(memberId, teamId);

        if (usersTeam == null) {
            throw new EntityNotFoundException("Member not found");
        }

        if (request.getStatus() != null || !request.getStatus().equals(0)) {
            for (GlobalAppStatus status : GlobalAppStatus.values()) {
                if (status.getValue() == request.getStatus()) {
                    usersTeam.setStatus(status.name());
                }
            }
        }

        if (request.getxPosition() != null && request.getyPosition() != null) {
            usersTeam.setyPosition(request.getyPosition());
            usersTeam.setxPosition(request.getxPosition());
        }

        teamMembersRepository.save(usersTeam);
    }

}