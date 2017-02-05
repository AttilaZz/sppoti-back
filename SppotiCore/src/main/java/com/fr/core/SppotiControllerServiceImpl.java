package com.fr.core;

import com.fr.commons.dto.SppotiRequest;
import com.fr.commons.dto.SppotiResponse;
import com.fr.commons.dto.TeamResponse;
import com.fr.entities.*;
import com.fr.exceptions.HostMemberNotFoundException;
import com.fr.exceptions.SportNotFoundException;
import com.fr.models.GlobalAppStatus;
import com.fr.rest.service.SppotiControllerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
        Sppoti sppoti = new Sppoti();

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
                hostTeam.setTeamMemberss(getTeamMembersEntityFromDto(newSppoti.getMyTeam().getMembers(), hostTeam, sppotiCreator, sppoti));

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

        hostTeam.setSport(sport);

        sppoti.setRelatedSport(sport);

        sppoti.setUserSppoti(owner);

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
                team = teamRepository.findByUuid(newSppoti.getVsTeam());

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

        if (sppoti != null && sppoti.isDeleted()) {
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

        TeamResponse teamHostResponse = fillTeamResponse(sppoti.getTeamHost(), sppoti.getUserSppoti().getId());
        if (sppoti.getTeamAdverse() != null) {
            TeamResponse teamGuestResponse = fillTeamResponse(sppoti.getTeamAdverse(), null);
            sppotiResponse.setTeamGuest(teamGuestResponse);
        }

        sppotiResponse.setTeamHost(teamHostResponse);
        sppotiResponse.setId(sppoti.getUuid());

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

        if (sppotiRequest.getAddress() != null) {
            sppoti.setLocation(sppotiRequest.getAddress());
        }

        if (sppotiRequest.getMaxTeamCount() != 0) {
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
     * ACCEPT sppoti and add notification
     *
     * @param sppotiId
     * @param userId
     */
    @Override
    public void acceptSppoti(int sppotiId, int userId) {

        SppotiMembers sppotiMembers = sppotiMembersRepository.findByUsersTeamUsersUuidAndSppotiUuid(userId, sppotiId);

        if (sppotiMembers == null) {
            throw new EntityNotFoundException("Sppoter not found");
        }
        sppotiMembers.setStatus(GlobalAppStatus.CONFIRMED.name());
        sppotiMembersRepository.save(sppotiMembers);

        TeamMembers teamMembers = sppotiMembers.getUsersTeam();
        teamMembers.setStatus(GlobalAppStatus.CONFIRMED.name());

        teamMembersRepository.save(teamMembers);

    }

    /**
     * REFUSE sppoti and add notification
     *
     * @param sppotiId
     * @param userId
     */
    @Override
    public void refuseSppoti(int sppotiId, int userId) {

        SppotiMembers sppotiMembers = sppotiMembersRepository.findByUsersTeamUsersUuidAndSppotiUuid(sppotiId, userId);

        if (sppotiMembers == null) {
            throw new EntityNotFoundException("Sppoter not found");
        }

        sppotiMembers.setStatus(GlobalAppStatus.REFUSED.name());
        sppotiMembersRepository.save(sppotiMembers);

    }


}