package com.fr.core;

import com.fr.commons.dto.SppotiRequest;
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

    TeamRepository teamRepository;

    @Autowired
    public void setTeamRepository(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public void saveTeam(SppotiRequest.Team team, Long adminId) {
        Team teamToSave = new Team();

        teamToSave.setName(team.getName());
        teamToSave.setCoverPath(team.getCoverPath());
        teamToSave.setLogoPath(team.getLogoPath());

        try {
            teamToSave.setTeamMembers(getTeamMembersEntityFromDto(team.getMemberIdList()));
        } catch (RuntimeException e) {
            LOGGER.error("One of the team id not found: " + e.getMessage());
            throw new HostMemberNotFoundException("Team (members) one of the team dosn't exist");

        }

        Users admin = userRepository.findOne(adminId);
        Set<Users> admins = new HashSet<Users>();
        admins.add(admin);

        teamToSave.setAdmins(admins);

        teamRepository.save(teamToSave);

    }

    private Set<Users> getTeamMembersEntityFromDto(Long[] memberIdList) {
        Set<Users> team = new HashSet<Users>();

        for (Long userId : memberIdList) {

            Users u = userRepository.findOne(userId);
            if (u != null) {
                team.add(u);
            } else {
                throw new EntityNotFoundException();
            }

        }
        return team;
    }
}
