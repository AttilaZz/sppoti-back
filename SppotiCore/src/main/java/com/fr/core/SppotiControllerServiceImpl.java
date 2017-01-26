package com.fr.core;

import com.fr.commons.dto.SppotiRequest;
import com.fr.controllers.service.SppotiControllerService;
import com.fr.entities.Sport;
import com.fr.entities.Sppoti;
import com.fr.entities.Team;
import com.fr.entities.Users;
import com.fr.exceptions.HostMemberNotFoundException;
import com.fr.exceptions.SportNotFoundException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Component
public class SppotiControllerServiceImpl extends AbstractControllerServiceImpl implements SppotiControllerService {

    public static final String TEAM_ID_NOT_FOUND = "Team id not found";

    public Set<Users> getTeamMembersEntityFromDto(int[] memberIdList, Team team) {

        Set<Users> teamUsers = new HashSet<Users>();
        Set<Team> teams = new HashSet<Team>();
        teams.add(team);

        for (int userId : memberIdList) {

            Users u = userRepository.getByUuid(userId);

            if (u != null) {
                u.setTeam(teams);
                teamUsers.add(u);
            } else {
                throw new EntityNotFoundException();
            }

        }

        return teamUsers;

    }

    @Override
    public void saveSppoti(SppotiRequest newSppoti, Long sppotiCreator) {

        Team hostTeam = new Team();

        if (newSppoti.getMyTeam() != null) {

            if (newSppoti.getMyTeam().getName() != null) {
                hostTeam.setName(newSppoti.getMyTeam().getName());
            }

            if (newSppoti.getMyTeam().getLogoPath() != null) {
                hostTeam.setLogoPath(newSppoti.getMyTeam().getLogoPath());
            }

            if (newSppoti.getMyTeam().getCoverPath() != null) {
                hostTeam.setCoverPath(newSppoti.getMyTeam().getCoverPath());
            }

            try {
                hostTeam.setTeamMembers(getTeamMembersEntityFromDto(newSppoti.getMyTeam().getMemberIdList(), hostTeam));
            } catch (RuntimeException e) {
                LOGGER.error("One of the team id not found: " + e.getMessage());
                throw new HostMemberNotFoundException("Host-TeamRequest (members) one of the team dosn't exist");

            }

        } else if (newSppoti.getMyTeamId() != 0) {
            try {
                hostTeam = teamRepository.findByUuid(newSppoti.getMyTeamId());

                if(hostTeam == null){
                    throw new EntityNotFoundException("Host team not found in the request");
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                throw new EntityNotFoundException("Host team cannot be found in the request");
            }
        } else {
            throw new EntityNotFoundException("Host team not found in the request");
        }

        Sport sport = sportRepository.findOne(newSppoti.getSportId());
        if (sport == null) {
            throw new SportNotFoundException("Sport id is incorrect");
        }

        Users owner = userRepository.findOne(sppotiCreator);
        if (owner == null) {
            throw new EntityNotFoundException("Stored used id in session has not been found in database");
        }

        Sppoti sppoti = new Sppoti();
        sppoti.setRelatedSport(sport);

        Set<Users> admins = new HashSet<Users>();
        admins.add(owner);
        sppoti.setUserSppoti(owner);

        Set<Team> teams = new HashSet<Team>();
        teams.add(hostTeam);
        owner.setTeamAdmin(teams);

        hostTeam.setAdmins(admins);
        sppoti.setTeamHost(hostTeam);

        if (newSppoti.getTags() != null) {
            sppoti.setTags(newSppoti.getTags());
        }

        if (newSppoti.getDescription() != null) {
            sppoti.setDescription(newSppoti.getDescription());
        }


        if (newSppoti.getVsTeam() != 0) {
            Team team = null;
            try {
                team = teamRepository.findByUuid(newSppoti.getMyTeamId());

            } catch (RuntimeException e) {
                e.printStackTrace();
                LOGGER.error("Error when getting team from AdverseTeamId: " + newSppoti.getVsTeam());
            }

            if (team == null) {
                throw new EntityNotFoundException(TEAM_ID_NOT_FOUND);

            }
            sppoti.setTeamGuest(team);
        }

        sppoti.setLocation(newSppoti.getAddress());
        sppoti.setDateTimeStart(newSppoti.getDatetimeStart());
        sppoti.setTitre(newSppoti.getTitre());
        sppoti.setMaxMembersCount(newSppoti.getMaxTeamCount());

        sppotiRepository.save(sppoti);

    }
}