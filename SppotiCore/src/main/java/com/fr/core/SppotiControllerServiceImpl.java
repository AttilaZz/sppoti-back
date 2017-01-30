package com.fr.core;

import com.fr.commons.dto.SppotiRequest;
import com.fr.commons.dto.SppotiResponse;
import com.fr.commons.dto.TeamResponse;
import com.fr.commons.dto.User;
import com.fr.rest.service.SppotiControllerService;
import com.fr.entities.Sport;
import com.fr.entities.Sppoti;
import com.fr.entities.Team;
import com.fr.entities.Users;
import com.fr.exceptions.HostMemberNotFoundException;
import com.fr.exceptions.SportNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Component
public class SppotiControllerServiceImpl extends AbstractControllerServiceImpl implements SppotiControllerService {

    private static final String TEAM_ID_NOT_FOUND = "Team id not found";

    @Value("${key.sppotiesPerPage}")
    private int sppoti_size;

    /**
     * @param newSppoti
     * @param sppotiCreator
     */
    @Override
    public SppotiResponse saveSppoti(SppotiRequest newSppoti, Long sppotiCreator) {

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

                if (hostTeam == null) {
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
            sppoti.setTeamAdverse(team);
        }

        sppoti.setLocation(newSppoti.getAddress());
        sppoti.setDateTimeStart(newSppoti.getDatetimeStart());
        sppoti.setTitre(newSppoti.getTitre());
        sppoti.setMaxMembersCount(newSppoti.getMaxTeamCount());

        Sppoti sppoti1 = sppotiRepository.save(sppoti);
        return new SppotiResponse(sppoti1.getUuid());

    }

    /**
     * @param uuid
     * @return
     */
    @Override
    public SppotiResponse getSppotiByUuid(int uuid) {

        Sppoti sppoti = sppotiRepository.findByUuid(uuid);

        if (sppoti.isDeleted()) {
            throw new EntityNotFoundException("Trying to get a deleted sppoti");
        }

        return getSppotiResponse(sppoti);

    }

    private SppotiResponse getSppotiResponse(Sppoti sppoti) {

        if (sppoti == null) {
            throw new EntityNotFoundException("Sppoti not found");
        }

        SppotiResponse sppotiResponse = new SppotiResponse(sppoti.getTitre(), sppoti.getDatetimeCreated(), sppoti.getDateTimeStart(), sppoti.getLocation(), sppoti.getMaxMembersCount(), sppoti.getRelatedSport());

        if (sppoti.getDescription() != null) {
            sppotiResponse.setDescription(sppoti.getDescription());
        }

        if (sppoti.getTags() != null) {
            sppotiResponse.setTags(sppoti.getTags());
        }

        User user_cover_avatar = getUserCoverAndAvatar(sppoti.getUserSppoti());

        User sppotiOwner = new User(sppoti.getUserSppoti().getUuid(), sppoti.getUserSppoti().getFirstName(), sppoti.getUserSppoti().getLastName(), sppoti.getUserSppoti().getUsername(), user_cover_avatar.getCover(), user_cover_avatar.getAvatar(), user_cover_avatar.getCoverType());

        sppotiResponse.setUserSppoti(sppotiOwner);

        TeamResponse teamHostResponse = fillTeamResponse(sppoti.getTeamHost());
        if (sppoti.getTeamAdverse() != null) {
            TeamResponse teamGuestResponse = fillTeamResponse(sppoti.getTeamAdverse());
            sppotiResponse.setTeamGuest(teamGuestResponse);
        }

        sppotiResponse.setTeamHost(teamHostResponse);
        return sppotiResponse;
    }

    /**
     * @param id
     * @return all sppoties created by a user
     */
    @Override
    public List<SppotiResponse> getAllUserSppoties(int id, int page) {

        Pageable pageable = new PageRequest(page, sppoti_size);

        List<Sppoti> sppoties = sppotiRepository.findByUserSppotiUuid(id, pageable);

        List<SppotiResponse> sppotiResponses = new ArrayList<SppotiResponse>();

        for (Sppoti sppoti : sppoties) {
            sppotiResponses.add(getSppotiResponse(sppoti));
        }

        return sppotiResponses;

    }

    /**
     * @param id
     */
    @Override
    public void deleteSppoti(int id) {

        Sppoti sppoti = sppotiRepository.findByUuid(id);

        if (sppoti != null && !sppoti.isDeleted()) {
            sppoti.setDeleted(true);
            sppotiRepository.save(sppoti);
        } else {
            throw new EntityNotFoundException("Trying to delete non existing sppoti");
        }

    }

    @Override
    public SppotiResponse updateSppoti(SppotiRequest sppotiRequest, int id) {

        Sppoti sppoti = sppotiRepository.findByUuid(id);

        if (sppoti == null) {
            throw new EntityNotFoundException("Sppoti not found with id: " + id);
        }

        if (sppotiRequest.getTags() != null) {
            sppoti.setTags(sppotiRequest.getTags());
        }

        if (sppotiRequest.getDescription() != null) {
            sppoti.setDescription(sppotiRequest.getDescription());
        }

        if (sppotiRequest.getDatetimeStart() != null) {
            sppoti.setDateTimeStart(sppotiRequest.getDatetimeStart());
        }

        if (sppotiRequest.getTitre() != null) {
            sppoti.setTitre(sppotiRequest.getTitre());
        }

        if(sppotiRequest.getAddress() != null){
            sppoti.setLocation(sppotiRequest.getAddress());
        }

        if(sppotiRequest.getMaxTeamCount() != 0){
            sppoti.setMaxMembersCount(sppotiRequest.getMaxTeamCount());
        }

        if (sppotiRequest.getVsTeam() != 0) {
            Team adverseTeam = teamRepository.findByUuid(sppotiRequest.getVsTeam());
            if (adverseTeam == null) {
                throw new EntityNotFoundException("Team id not found: " + sppotiRequest.getVsTeam());
            }
            sppoti.setTeamAdverse(adverseTeam);
        }

        Sppoti updatedSppoti = sppotiRepository.save(sppoti);

        return getSppotiResponse(updatedSppoti);

    }

    /**
     * @param team
     * @return a teamResponse object from Team entity
     */
    private TeamResponse fillTeamResponse(Team team) {

        TeamResponse teamResponse = new TeamResponse();

        List<User> teamUsers = new ArrayList<User>();

        for (Users user : team.getTeamMembers()) {
            User user_cover_avatar = getUserCoverAndAvatar(user);

            teamUsers.add(new User(user.getUuid(), user.getFirstName(), user.getLastName(), user.getUsername(), user_cover_avatar.getCover(), user_cover_avatar.getAvatar(), user_cover_avatar.getCoverType()));

        }

        teamResponse.setTeamMembers(teamUsers);

        teamResponse.setCoverPath(team.getCoverPath());
        teamResponse.setLogoPath(team.getLogoPath());
        teamResponse.setName(team.getName());

        List<User> adminUsers = new ArrayList<User>();

        for (Users user : team.getAdmins()) {
            User user_cover_avatar = getUserCoverAndAvatar(user);

            adminUsers.add(new User(user.getUuid(), user.getFirstName(), user.getLastName(), user.getUsername(), user_cover_avatar.getCover(), user_cover_avatar.getAvatar(), user_cover_avatar.getCoverType()));

        }

        teamResponse.setTeamAdmin(adminUsers);

        return teamResponse;

    }

}