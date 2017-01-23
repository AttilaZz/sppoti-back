package com.fr.core;

import com.fr.commons.dto.TeamRequest;
import com.fr.controllers.service.TeamControllerService;
import com.fr.entities.Team;
import com.fr.entities.Users;
import com.fr.exceptions.HostMemberNotFoundException;
import com.fr.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
public class TeamControllerServiceImpl extends AbstractControllerServiceImpl implements TeamControllerService {

    private TeamRepository teamRepository;

    @Autowired
    public void setTeamRepository(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public void saveTeam(TeamRequest team, Long adminId) {
        Team teamToSave = new Team();

        teamToSave.setName(team.getName());
        teamToSave.setCoverPath(team.getCoverPath());
        teamToSave.setLogoPath(team.getLogoPath());

        try {
            teamToSave.setTeamMembers(getTeamMembersEntityFromDto(teamToSave, team.getMemberIdList()));
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

    private Set<Users> getTeamMembersEntityFromDto(Team teamToSave, Long[] memberIdList) {
        Set<Users> teamUsers = new HashSet<Users>();
        Set<Team> teams = new HashSet<Team>();
        teams.add(teamToSave);

        for (Long userId : memberIdList) {

            Users u = userRepository.findOne(userId);
            u.setTeam(teams);

            if (u != null) {
                teamUsers.add(u);
            } else {
                throw new EntityNotFoundException();
            }

        }
        return teamUsers;
    }
}
