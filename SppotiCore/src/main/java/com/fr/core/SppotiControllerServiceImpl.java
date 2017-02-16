package com.fr.core;

import com.fr.commons.dto.SppotiRequestDTO;
import com.fr.commons.dto.SppotiResponseDTO;
import com.fr.commons.dto.TeamResponseDTO;
import com.fr.entities.*;
import com.fr.exceptions.TeamMemberNotFoundException;
import com.fr.exceptions.SportNotFoundException;
import com.fr.models.GlobalAppStatus;
import com.fr.rest.service.SppotiControllerService;
import org.apache.log4j.Logger;
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

    private static final String TEAM_ID_NOT_FOUND = "TeamEntity id not found";
    private Logger LOGGER = Logger.getLogger(SppotiControllerServiceImpl.class);

    @Value("${key.sppotiesPerPage}")
    private int sppotiSize;

    /**
     * @param newSppoti
     * @param sppotiCreator
     */
    @Override
    public SppotiResponseDTO saveSppoti(SppotiRequestDTO newSppoti, Long sppotiCreator) {

        TeamEntity hostTeam = new TeamEntity();
        SppotiEntity sppoti = new SppotiEntity();

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
                LOGGER.error("Error when trying to get USERS from team members list: ", e);
                throw new TeamMemberNotFoundException("Host-TeamRequestDTO (members) one of the team dosn't exist");

            }

        } else if (newSppoti.getMyTeamId() != 0) {

            List<TeamEntity> tempTeams = teamRepository.findByUuid(newSppoti.getMyTeamId());

            if (tempTeams == null || tempTeams.isEmpty()) {
                throw new EntityNotFoundException("Host team not found in the request");
            }

            hostTeam = tempTeams.get(0);

        } else {
            throw new EntityNotFoundException("Host team not found in the request");
        }

        SportEntity sportEntity = sportRepository.findOne(newSppoti.getSportId());
        if (sportEntity == null) {
            throw new SportNotFoundException("SportEntity id is incorrect");
        }

        UserEntity owner = userRepository.findOne(sppotiCreator);
        if (owner == null) {
            throw new EntityNotFoundException("Stored used id in session has not been found in database");
        }

        hostTeam.setSport(sportEntity);
        sppoti.setSport(sportEntity);
        sppoti.setUserSppoti(owner);
        sppoti.setTeamHost(hostTeam);

        if (newSppoti.getTags() != null) {
            sppoti.setTags(newSppoti.getTags());
        }

        if (newSppoti.getDescription() != null) {
            sppoti.setDescription(newSppoti.getDescription());
        }

        if (newSppoti.getVsTeam() != 0) {
            TeamEntity team = null;
            try {
                List<TeamEntity> tempTeams = teamRepository.findByUuid(newSppoti.getVsTeam());

                if (!tempTeams.isEmpty()) {
                    team = tempTeams.get(0);
                }

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

        SppotiEntity sppoti1 = sppotiRepository.save(sppoti);

        return new SppotiResponseDTO(sppoti1.getUuid());

    }

    /**
     * @param uuid
     * @return
     */
    @Override
    public SppotiResponseDTO getSppotiByUuid(Integer uuid, Integer connectedUSer) {

        SppotiEntity sppoti = sppotiRepository.findByUuid(uuid);

        if (sppoti != null && sppoti.isDeleted()) {
            throw new EntityNotFoundException("Trying to get a deleted sppoti");
        }

        return getSppotiResponse(sppoti, connectedUSer);

    }

    private SppotiResponseDTO getSppotiResponse(SppotiEntity sppoti, Integer connectedUser) {

        if (sppoti == null) {
            throw new EntityNotFoundException("SppotiEntity not found");
        }

        SppotiResponseDTO sppotiResponseDTO = new SppotiResponseDTO(sppoti.getTitre(), sppoti.getDatetimeCreated(), sppoti.getDateTimeStart(), sppoti.getLocation(), sppoti.getMaxMembersCount(), sppoti.getSport());

        if (sppoti.getDescription() != null) {
            sppotiResponseDTO.setDescription(sppoti.getDescription());
        }

        if (sppoti.getTags() != null) {
            sppotiResponseDTO.setTags(sppoti.getTags());
        }

        TeamResponseDTO teamHostResponse = fillTeamResponse(sppoti.getTeamHost(), sppoti.getUserSppoti().getId());
        if (sppoti.getTeamAdverse() != null) {
            TeamResponseDTO teamGuestResponse = fillTeamResponse(sppoti.getTeamAdverse(), null);
            sppotiResponseDTO.setTeamGuest(teamGuestResponse);
        }

        sppotiResponseDTO.setTeamHost(teamHostResponse);
        sppotiResponseDTO.setId(sppoti.getUuid());

        List<SppotiMember> sppotiMembers = sppotiMembersRepository.findByUsersTeamUsersUuidAndSppotiSportId(sppoti.getUserSppoti().getUuid(), sppoti.getSport().getId());

        sppotiResponseDTO.setSppotiCounter(sppotiMembers.size());
        sppotiResponseDTO.setMySppoti(connectedUser.equals(sppoti.getUserSppoti().getUuid()));

        sppotiResponseDTO.setAdminTeamId(teamMembersRepository.findByUsersUuidAndTeamUuidAndAdminTrue(sppoti.getUserSppoti().getUuid(), sppoti.getTeamHost().getUuid()).getUuid());
        sppotiResponseDTO.setAdminUserId(sppoti.getUserSppoti().getUuid());
        sppotiResponseDTO.setConnectedUserId(connectedUser);

        return sppotiResponseDTO;
    }

    /**
     * @param id
     * @return all sppoties created by a user
     */
    @Override
    public List<SppotiResponseDTO> getAllUserSppoties(Integer id, int page, Integer connectedUser) {

        Pageable pageable = new PageRequest(page, sppotiSize);

        List<SppotiEntity> sppoties = sppotiRepository.findByUserSppotiUuid(id, pageable);

        List<SppotiResponseDTO> sppotiResponseDTOs = new ArrayList<SppotiResponseDTO>();

        for (SppotiEntity sppoti : sppoties) {
            sppotiResponseDTOs.add(getSppotiResponse(sppoti, connectedUser));
        }

        return sppotiResponseDTOs;

    }

    /**
     * @param id
     */
    @Override
    public void deleteSppoti(int id) {

        SppotiEntity sppoti = sppotiRepository.findByUuid(id);

        if (sppoti != null && !sppoti.isDeleted()) {
            sppoti.setDeleted(true);
            sppotiRepository.save(sppoti);
        } else {
            throw new EntityNotFoundException("Trying to delete non existing sppoti");
        }

    }

    @Override
    public SppotiResponseDTO updateSppoti(SppotiRequestDTO sppotiRequest, int id, Integer connectedUser) {

        SppotiEntity sppoti = sppotiRepository.findByUuid(id);

        if (sppoti == null) {
            throw new EntityNotFoundException("SppotiEntity not found with id: " + id);
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
            List<TeamEntity> adverseTeam = teamRepository.findByUuid(sppotiRequest.getVsTeam());

            if (adverseTeam == null || adverseTeam.isEmpty()) {
                throw new EntityNotFoundException("TeamEntity id not found: " + sppotiRequest.getVsTeam());
            }

            sppoti.setTeamAdverse(adverseTeam.get(0));
        }

        SppotiEntity updatedSppoti = sppotiRepository.save(sppoti);

        return getSppotiResponse(updatedSppoti, connectedUser);

    }

    /**
     * ACCEPT sppoti and add notification
     *
     * @param sppotiId
     * @param userId
     */
    @Override
    public void acceptSppoti(int sppotiId, int userId) {

        SppotiMember sppotiMembers = sppotiMembersRepository.findByUsersTeamUsersUuidAndSppotiUuid(userId, sppotiId);

        if (sppotiMembers == null) {
            throw new EntityNotFoundException("Sppoter not found");
        }
        sppotiMembers.setStatus(GlobalAppStatus.CONFIRMED.name());
        sppotiMembersRepository.save(sppotiMembers);

        TeamMemberEntity teamMembers = sppotiMembers.getUsersTeam();
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

        SppotiMember sppotiMembers = sppotiMembersRepository.findByUsersTeamUsersUuidAndSppotiUuid(sppotiId, userId);

        if (sppotiMembers == null) {
            throw new EntityNotFoundException("Sppoter not found");
        }

        sppotiMembers.setStatus(GlobalAppStatus.REFUSED.name());
        sppotiMembersRepository.save(sppotiMembers);

    }


}