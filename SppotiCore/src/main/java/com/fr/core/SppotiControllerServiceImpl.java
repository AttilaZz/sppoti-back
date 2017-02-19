package com.fr.core;

import com.fr.commons.dto.SppotiRequestDTO;
import com.fr.commons.dto.SppotiResponseDTO;
import com.fr.commons.dto.TeamResponseDTO;
import com.fr.entities.*;
import com.fr.exceptions.NoRightToAcceptOrRefuseChallenge;
import com.fr.exceptions.SportNotFoundException;
import com.fr.exceptions.TeamMemberNotFoundException;
import com.fr.models.GlobalAppStatus;
import com.fr.rest.service.SppotiControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public SppotiResponseDTO saveSppoti(SppotiRequestDTO newSppoti) {

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
                hostTeam.setTeamMembers(getTeamMembersEntityFromDto(newSppoti.getMyTeam().getMembers(), hostTeam, sppoti));
            } catch (RuntimeException e) {
                LOGGER.error("Error when trying to get USERS from team members list: ", e);
                throw new TeamMemberNotFoundException("Host-TeamRequestDTO (members) one of the team doesn't exist");
            }

        } else if (newSppoti.getMyTeamId() != 0) {

            List<TeamEntity> tempTeams = teamRepository.findByUuid(newSppoti.getMyTeamId());

            if (tempTeams == null || tempTeams.isEmpty()) {
                throw new EntityNotFoundException("Host team not found in the request");
            }
            hostTeam = tempTeams.get(0);

            final TeamEntity sppoterTeam = hostTeam;
            //Convert team members to sppoters.
            Set<SppotiMember> sppotiMembers = hostTeam.getTeamMembers().stream()
                    .map(
                            sm -> {
                                SppotiMember sppotiMember = new SppotiMember();
                                sppotiMember.setTeamMember(sm);
                                sppotiMember.setSppoti(sppoti);
                                return sppotiMember;
                            }

                    ).collect(Collectors.toSet());
            sppoti.setSppotiMembers(sppotiMembers);

        } else {
            throw new EntityNotFoundException("Host team not found in the request");
        }

        SportEntity sportEntity = sportRepository.findOne(newSppoti.getSportId());
        if (sportEntity == null) {
            throw new SportNotFoundException("SportEntity id is incorrect");
        }

        hostTeam.setSport(sportEntity);
        sppoti.setSport(sportEntity);
        sppoti.setUserSppoti(getConnectedUser());
        sppoti.setTeamHost(hostTeam);

        if (newSppoti.getTags() != null) {
            sppoti.setTags(newSppoti.getTags());
        }

        if (newSppoti.getDescription() != null) {
            sppoti.setDescription(newSppoti.getDescription());
        }

        /** add team adverse. */
//        if (newSppoti.getVsTeam() != 0) {
//            TeamEntity team = null;
//            try {
//                List<TeamEntity> tempTeams = teamRepository.findByUuid(newSppoti.getVsTeam());
//
//                if (!tempTeams.isEmpty()) {
//                    team = tempTeams.get(0);
//                }
//
//            } catch (RuntimeException e) {
//                e.printStackTrace();
//                LOGGER.error("Error when getting team from AdverseTeamId: " + newSppoti.getVsTeam());
//            }
//
//            if (team == null) {
//                throw new EntityNotFoundException(TEAM_ID_NOT_FOUND);
//
//            }
//            sppoti.setTeamAdverse(team);
//        }

        sppoti.setLocation(newSppoti.getAddress());
        sppoti.setDateTimeStart(newSppoti.getDatetimeStart());
        sppoti.setTitre(newSppoti.getTitre());
        sppoti.setMaxMembersCount(newSppoti.getMaxTeamCount());

        SppotiEntity savedSppoti = sppotiRepository.save(sppoti);

        return new SppotiResponseDTO(savedSppoti.getUuid());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SppotiResponseDTO getSppotiByUuid(Integer uuid) {

        SppotiEntity sppoti = sppotiRepository.findByUuid(uuid);

        if (sppoti != null && sppoti.isDeleted()) {
            throw new EntityNotFoundException("Trying to get a deleted sppoti");
        }

        return getSppotiResponse(sppoti);

    }

    /**
     * Map sppoti entity to DTO.
     *
     * @param sppoti sppoti to retuen.
     * @return sppoti DTO.
     */
    private SppotiResponseDTO getSppotiResponse(SppotiEntity sppoti) {

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
            TeamResponseDTO teamAdverseResponse = fillTeamResponse(sppoti.getTeamAdverse(), null);
            sppotiResponseDTO.setTeamAdverse(teamAdverseResponse);
            sppotiResponseDTO.setTeamAdverseStatus(GlobalAppStatus.valueOf(sppoti.getTeamAdverseStatus()).getValue());
        }

        sppotiResponseDTO.setTeamHost(teamHostResponse);
        sppotiResponseDTO.setId(sppoti.getUuid());

        List<SppotiMember> sppotiMembers = sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiSportId(sppoti.getUserSppoti().getUuid(), sppoti.getSport().getId());

        sppotiResponseDTO.setSppotiCounter(sppotiMembers.size());
        sppotiResponseDTO.setMySppoti(getConnectedUser().getUuid() == sppoti.getUserSppoti().getUuid());

        sppotiResponseDTO.setAdminTeamId(teamMembersRepository.findByUsersUuidAndTeamUuidAndAdminTrue(sppoti.getUserSppoti().getUuid(), sppoti.getTeamHost().getUuid()).getUuid());
        sppotiResponseDTO.setAdminUserId(sppoti.getUserSppoti().getUuid());
        sppotiResponseDTO.setConnectedUserId(getConnectedUser().getUuid());

        return sppotiResponseDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SppotiResponseDTO> getAllUserSppoties(Integer id, int page) {

        Pageable pageable = new PageRequest(page, sppotiSize);

        List<SppotiEntity> sppoties = sppotiRepository.findByUserSppotiUuid(id, pageable);

        List<SppotiResponseDTO> sppotiResponseDTOs = new ArrayList<SppotiResponseDTO>();

        for (SppotiEntity sppoti : sppoties) {
            sppotiResponseDTOs.add(getSppotiResponse(sppoti));
        }

        return sppotiResponseDTOs;

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
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

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public SppotiResponseDTO updateSppoti(SppotiRequestDTO sppotiRequest, int id) {

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

        return getSppotiResponse(updatedSppoti);

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void acceptSppoti(int sppotiId, int userId) {

        SppotiMember sppotiMembers = sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiUuid(userId, sppotiId);

        if (sppotiMembers == null) {
            throw new EntityNotFoundException("Sppoter not found");
        }
        sppotiMembers.setStatus(GlobalAppStatus.CONFIRMED.name());
        sppotiMembersRepository.save(sppotiMembers);

        TeamMemberEntity teamMembers = sppotiMembers.getTeamMember();
        teamMembers.setStatus(GlobalAppStatus.CONFIRMED.name());

        teamMembersRepository.save(teamMembers);

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void refuseSppoti(int sppotiId, int userId) {

        SppotiMember sppotiMembers = sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiUuid(sppotiId, userId);

        if (sppotiMembers == null) {
            throw new EntityNotFoundException("Sppoter not found");
        }

        sppotiMembers.setStatus(GlobalAppStatus.REFUSED.name());
        sppotiMembersRepository.save(sppotiMembers);

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public SppotiResponseDTO updateTeamAdverseChallengeStatus(int sppotiId, int adverseTeamResponseStatus) {

        SppotiEntity sppotiEntity = sppotiRepository.findByUuid(sppotiId);

        Optional adverseTeamAdmin = sppotiEntity.getTeamAdverse().getTeamMembers().stream()
                .filter(TeamMemberEntity::getAdmin)
                .filter(t -> t.getId().equals(getConnectedUser().getId()))
                .findFirst();

        if (!adverseTeamAdmin.isPresent()) {
            throw new NoRightToAcceptOrRefuseChallenge("User (" + getConnectedUser().toString() + " is not admin of tema (" + sppotiEntity.getTeamAdverse() + ")");
        }

        for (GlobalAppStatus status : GlobalAppStatus.values()) {
            if (status.getValue() == adverseTeamResponseStatus) {
                sppotiEntity.setTeamAdverseStatus(status.name());
            }
        }

        SppotiEntity savedSppoti = sppotiRepository.save(sppotiEntity);

        return getSppotiResponse(savedSppoti);

    }


}