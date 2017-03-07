package com.fr.core;

import com.fr.commons.dto.sppoti.SppotiRequestDTO;
import com.fr.commons.dto.sppoti.SppotiResponseDTO;
import com.fr.commons.dto.team.TeamResponseDTO;
import com.fr.entities.*;
import com.fr.exceptions.NoRightToAcceptOrRefuseChallenge;
import com.fr.exceptions.SportNotFoundException;
import com.fr.models.GlobalAppStatus;
import com.fr.models.NotificationType;
import com.fr.rest.service.SppotiControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
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

        SportEntity sportEntity = sportRepository.findOne(newSppoti.getSportId());
        if (sportEntity == null) {
            throw new SportNotFoundException("SportEntity id is incorrect");
        }
        sppoti.setSport(sportEntity);

        sppoti.setLocation(newSppoti.getAddress());
        sppoti.setDateTimeStart(newSppoti.getDatetimeStart());
        sppoti.setTitre(newSppoti.getTitre());
        sppoti.setMaxMembersCount(newSppoti.getMaxTeamCount());

        if (newSppoti.getTags() != null) {
            sppoti.setTags(newSppoti.getTags());
        }

        if (newSppoti.getDescription() != null) {
            sppoti.setDescription(newSppoti.getDescription());
        }

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

            hostTeam.setTeamMembers(getTeamMembersEntityFromDto(newSppoti.getMyTeam().getMembers(), hostTeam, sppoti));
            hostTeam.setSport(sportEntity);
//            teamRepository.save(hostTeam);

        } else if (newSppoti.getMyTeamId() != 0) {

            List<TeamEntity> tempTeams = teamRepository.findByUuid(newSppoti.getMyTeamId());

            if (tempTeams == null || tempTeams.isEmpty()) {
                throw new EntityNotFoundException("Host team not found in the request");
            }
            hostTeam = tempTeams.get(0);

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

        sppoti.setUserSppoti(getConnectedUser());
        sppoti.setTeamHost(hostTeam);

        Set<SppotiEntity> sppotiEntities = new HashSet<>();
        sppotiEntities.add(sppoti);
        hostTeam.setSppotiEntity(sppotiEntities);


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

        TeamResponseDTO teamHostResponse = fillTeamResponse(sppoti.getTeamHost(), sppoti);
        if (sppoti.getTeamAdverse() != null) {
            TeamResponseDTO teamAdverseResponse = fillTeamResponse(sppoti.getTeamAdverse(), sppoti);
            sppotiResponseDTO.setTeamAdverse(teamAdverseResponse);
            sppotiResponseDTO.setTeamAdverseStatus(GlobalAppStatus.valueOf(sppoti.getTeamAdverseStatus()).getValue());
        } else {
            sppotiResponseDTO.setTeamAdverseStatus(GlobalAppStatus.NO_CHALLENGE_YET.getValue());
        }

        sppotiResponseDTO.setTeamHost(teamHostResponse);
        sppotiResponseDTO.setId(sppoti.getUuid());

        List<SppotiMember> sppotiMembers = sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiSportId(sppoti.getUserSppoti().getUuid(), sppoti.getSport().getId());

        sppotiResponseDTO.setSppotiCounter(sppotiMembers.size());
        sppotiResponseDTO.setMySppoti(getConnectedUser().getUuid() == sppoti.getUserSppoti().getUuid());

        //if user is member of a team, get admin of the tem and other informations.
        TeamMemberEntity teamAdmin = teamMembersRepository.findByUsersUuidAndTeamUuidAndAdminTrue(sppoti.getUserSppoti().getUuid(), sppoti.getTeamHost().getUuid());
        if (teamAdmin != null) {
            sppotiResponseDTO.setAdminTeamId(teamAdmin.getUuid());
            sppotiResponseDTO.setAdminUserId(sppoti.getUserSppoti().getUuid());
            sppotiResponseDTO.setConnectedUserId(getConnectedUser().getUuid());
        }

        return sppotiResponseDTO;
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

            //Convert team members to sppoters.
            Set<SppotiMember> sppotiMembers = adverseTeam.get(0).getTeamMembers().stream()
                    .map(
                            sm -> {
                                SppotiMember sppotiMember = new SppotiMember();
                                sppotiMember.setTeamMember(sm);
                                sppotiMember.setSppoti(sppoti);
                                return sppotiMember;
                            }

                    ).collect(Collectors.toSet());
            sppoti.setSppotiMembers(sppotiMembers);
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

        //update status as sppoti memeber.
        sppotiMembers.setStatus(GlobalAppStatus.CONFIRMED.name());
        SppotiMember updatedSppoter = sppotiMembersRepository.save(sppotiMembers);

        /**
         * Send notification to sppoti admin.
         */
        if (updatedSppoter != null) {
            addNotification(NotificationType.X_ACCEPTED_YOUR_SPPOTI_INVITATION, sppotiMembers.getTeamMember().getUsers(), sppotiMembers.getSppoti().getUserSppoti(), null);
        }

        //update status as team member.
        TeamMemberEntity teamMembers = sppotiMembers.getTeamMember();
        if (!GlobalAppStatus.valueOf(teamMembers.getStatus()).equals(GlobalAppStatus.CONFIRMED)) {
            teamMembers.setStatus(GlobalAppStatus.CONFIRMED.name());
        }

        TeamMemberEntity updatedTeamMember = teamMembersRepository.save(teamMembers);

        /**
         * Send notification to team admin.
         */
        if (updatedTeamMember != null && !GlobalAppStatus.valueOf(teamMembers.getStatus()).equals(GlobalAppStatus.CONFIRMED)) {

            UserEntity teamAdmin = teamMembersRepository.findByTeamUuidAndAdminTrue(teamMembers.getTeam().getUuid()).getUsers();

            addNotification(NotificationType.X_ACCEPTED_YOUR_TEAM_INVITATION, sppotiMembers.getTeamMember().getUsers(), teamAdmin, teamMembers.getTeam());
        }

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
        SppotiMember updatedSppoter = sppotiMembersRepository.save(sppotiMembers);

        /**
         * Send notification to sppoti admin.
         */
        if (updatedSppoter != null) {
            addNotification(NotificationType.X_REFUSED_YOUR_SPPOTI_INVITATION, sppotiMembers.getTeamMember().getUsers(), sppotiMembers.getSppoti().getUserSppoti(), null);
        }

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


    /**
     * {@inheritDoc}
     */
    @Override
    public List<SppotiResponseDTO> getAllUserSppoties(Integer id, int page) {

        Pageable pageable = new PageRequest(page, sppotiSize);

        List<SppotiEntity> sppoties = sppotiRepository.findByUserSppotiUuidAndTeamAdverseStatusNot(id, GlobalAppStatus.REFUSED.name(), pageable);

        return sppoties.stream()
                .map(this::getSppotiResponse)
                .collect(Collectors.toList());

    }

    @Override
    public List<SppotiResponseDTO> getAllJoinedSppoties(int userId, int page) {
        Pageable pageable = new PageRequest(page, sppotiSize);

        List<SppotiMember> sppotiMembers = sppotiMembersRepository.findByTeamMemberUsersUuid(userId, pageable);

        return sppotiMembers.stream()
                .map(s -> getSppotiResponse(s.getSppoti()))
                .collect(Collectors.toList());
    }


}