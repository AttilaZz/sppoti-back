package com.fr.core;

import com.fr.commons.dto.User;
import com.fr.controllers.service.SppotiControllerService;
import com.fr.entities.Sport;
import com.fr.entities.Sppoti;
import com.fr.entities.Team;
import com.fr.entities.Users;
import com.fr.commons.dto.SppotiRequest;
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

    public Set<Users> getTeamMembersEntityFromDto(Long[] memberIdList) {

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

    @Override
    public void saveSppoti(SppotiRequest newSppoti, Long sppotiCreator) {

        Team hostTeam = new Team(), guestTeam = new Team();

        hostTeam.setName(newSppoti.getMyTeam().getName());
        hostTeam.setCoverPath(newSppoti.getMyTeam().getCoverPath());
        hostTeam.setLogoPath(newSppoti.getMyTeam().getLogoPath());
        try {
            hostTeam.setTeamMembers(getTeamMembersEntityFromDto(newSppoti.getMyTeam().getMemberIdList()));
        } catch (RuntimeException e) {
            LOGGER.error("One of the team id not found: " + e.getMessage());
            throw new HostMemberNotFoundException("Host-TeamRequest (members) one of the team dosn't exist");

        }

        guestTeam.setName(newSppoti.getVsTeam().getName());
        guestTeam.setCoverPath(newSppoti.getVsTeam().getCoverPath());
        guestTeam.setLogoPath(newSppoti.getVsTeam().getLogoPath());

        try {
            guestTeam.setTeamMembers(getTeamMembersEntityFromDto(newSppoti.getVsTeam().getMemberIdList()));
        } catch (RuntimeException e) {
            LOGGER.error("One of the team id not found: " + e.getMessage());
            throw new HostMemberNotFoundException("Guest-TeamRequest (members) one of the team dosn't exist");
        }

        Sport sport = sportRepository.findOne(newSppoti.getSportId());
        if (sport == null) {
            throw new SportNotFoundException("Sport id is incorrect");
        }

        Users owner = userRepository.findOne(sppotiCreator);
        if (owner == null) {
            throw new EntityNotFoundException("Stored used id in session has not been found in database");
        }

        Set<Users> admins = new HashSet<Users>();
        admins.add(owner);

        Sppoti sppoti = new Sppoti();
        sppoti.setRelatedSport(sport);
        sppoti.setUserSppoti(owner);
        sppoti.setTags(newSppoti.getTags());
        sppoti.setTeamGuest(guestTeam);
        hostTeam.setAdmins(admins);
        sppoti.setTeamHost(hostTeam);
        sppoti.setDescription(newSppoti.getDescription());
        sppoti.setLocation(newSppoti.getAddress());
        sppoti.setDateTimeStart(newSppoti.getDatetimeStart());
        sppoti.setTitre(newSppoti.getTitre());
        sppoti.setMaxMembersCount(newSppoti.getMaxTeamCount());

        sppotiRepository.save(sppoti);

    }
}