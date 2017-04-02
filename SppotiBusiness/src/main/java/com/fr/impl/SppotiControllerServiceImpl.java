package com.fr.impl;

import com.fr.commons.dto.SppotiRatingDTO;
import com.fr.commons.dto.sppoti.SppotiRequestDTO;
import com.fr.commons.dto.sppoti.SppotiResponseDTO;
import com.fr.commons.dto.team.TeamResponseDTO;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.exception.NoRightToAcceptOrRefuseChallenge;
import com.fr.commons.exception.NotAdminException;
import com.fr.entities.*;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.NotificationTypeEnum;
import com.fr.service.SppotiControllerService;
import com.fr.transformers.SportTransformer;
import com.fr.transformers.SppotiTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Component
class SppotiControllerServiceImpl extends AbstractControllerServiceImpl implements SppotiControllerService {

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
            throw new EntityNotFoundException("SportEntity id is incorrect");
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
            Set<SppotiMemberEntity> sppotiMembers = hostTeam.getTeamMembers().stream()
                    .map(
                            sm -> {
                                SppotiMemberEntity sppotiMember = new SppotiMemberEntity();
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

        //If team has been saved with the sppoti, send email to its members.
        if (newSppoti.getMyTeam() != null) {
            TeamEntity team = savedSppoti.getTeamHost();
            team.getTeamMembers().forEach(m -> sendJoinTeamEmail(team, getUserById(m.getUsers().getId()),
                    teamMembersRepository.findByTeamUuidAndAdminTrue(team.getUuid())));
        }

        //TODO: Send email to the adverse team admin.

        //TODO: Send emails to sppoti members too.

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

        SppotiResponseDTO sppotiResponseDTO = SppotiTransformer.entityToDto(sppoti);

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
            sppotiResponseDTO.setTeamAdverseStatus(sppoti.getTeamAdverseStatus().getValue());
        } else {
            sppotiResponseDTO.setTeamAdverseStatus(GlobalAppStatusEnum.NO_CHALLENGE_YET.getValue());
        }

        sppotiResponseDTO.setTeamHost(teamHostResponse);
        sppotiResponseDTO.setId(sppoti.getUuid());
        sppotiResponseDTO.setRelatedSport(SportTransformer.modelToDto(sppoti.getSport()));

        List<SppotiMemberEntity> sppotiMembers = sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiSportId(sppoti.getUserSppoti().getUuid(), sppoti.getSport().getId());

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
            Set<SppotiMemberEntity> sppotiMembers = convertAdverseTeamMembersToSppoters(adverseTeam.get(0), sppoti, false);
            sppoti.setSppotiMembers(sppotiMembers);
            sppoti.setTeamAdverse(adverseTeam.get(0));
        }

        sppoti.setTeamAdverseStatus(GlobalAppStatusEnum.PENDING);
        SppotiEntity updatedSppoti = sppotiRepository.save(sppoti);

        return getSppotiResponse(updatedSppoti);

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void acceptSppoti(int sppotiId, int userId) {

        Optional<SppotiMemberEntity> optional = Optional.ofNullable(sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiUuid(userId, sppotiId));

        optional.ifPresent(
                sm -> {
                    //update status as sppoti member.
                    sm.setStatus(GlobalAppStatusEnum.CONFIRMED);
                    SppotiMemberEntity updatedSppoter = sppotiMembersRepository.save(sm);

                    /**
                     * Send notification to sppoti admin.
                     */
                    if (updatedSppoter != null) {
                        addNotification(NotificationTypeEnum.X_ACCEPTED_YOUR_SPPOTI_INVITATION, sm.getTeamMember().getUsers(), sm.getSppoti().getUserSppoti(), null, null);
                    }

                    //update status as team member.
                    TeamMemberEntity teamMembers = sm.getTeamMember();
                    if (!teamMembers.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)) {
                        teamMembers.setStatus(GlobalAppStatusEnum.CONFIRMED);
                    }

                    TeamMemberEntity updatedTeamMember = teamMembersRepository.save(teamMembers);

                    /**
                     * Send notification to team admin.
                     */
                    if (updatedTeamMember != null && !teamMembers.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)) {

                        UserEntity teamAdmin = teamMembersRepository.findByTeamUuidAndAdminTrue(teamMembers.getTeam().getUuid()).getUsers();

                        addNotification(NotificationTypeEnum.X_ACCEPTED_YOUR_TEAM_INVITATION, sm.getTeamMember().getUsers(), teamAdmin, teamMembers.getTeam(), null);
                    }
                }
        );

        optional.orElseThrow(() -> new EntityNotFoundException("Sppoter not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void refuseSppoti(int sppotiId, int userId) {

        SppotiMemberEntity sppotiMembers = sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiUuid(sppotiId, userId);

        if (sppotiMembers == null) {
            throw new EntityNotFoundException("Sppoter not found");
        }

        sppotiMembers.setStatus(GlobalAppStatusEnum.REFUSED);
        SppotiMemberEntity updatedSppoter = sppotiMembersRepository.save(sppotiMembers);

        /**
         * Send notification to sppoti admin.
         */
        if (updatedSppoter != null) {
            addNotification(NotificationTypeEnum.X_REFUSED_YOUR_SPPOTI_INVITATION, sppotiMembers.getTeamMember().getUsers(), sppotiMembers.getSppoti().getUserSppoti(), null, null);
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
            throw new NoRightToAcceptOrRefuseChallenge("User (" + getConnectedUser().toString() + " is not admin of team (" + sppotiEntity.getTeamAdverse() + ")");
        }

        //set new status.
        for (GlobalAppStatusEnum status : GlobalAppStatusEnum.values()) {
            if (status.getValue() == adverseTeamResponseStatus) {
                sppotiEntity.setTeamAdverseStatus(status);
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

        List<SppotiEntity> sppoties = sppotiRepository.findByUserSppotiUuidAndTeamAdverseStatusNot(id, GlobalAppStatusEnum.REFUSED, pageable);

        return sppoties.stream()
                .map(this::getSppotiResponse)
                .collect(Collectors.toList());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SppotiResponseDTO> getAllJoinedSppoties(int userId, int page) {
        Pageable pageable = new PageRequest(page, sppotiSize);

        List<SppotiMemberEntity> sppotiMembers = sppotiMembersRepository.findByTeamMemberUsersUuidAndStatusNot(userId, GlobalAppStatusEnum.REFUSED, pageable);

        return sppotiMembers.stream()
                .map(s -> getSppotiResponse(s.getSppoti()))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void isSppotiAdmin(int sppotiId, Long userId) {
        SppotiEntity sppotiEntity = sppotiRepository.findByUuid(sppotiId);
        if (sppotiEntity == null) {
            throw new EntityNotFoundException("Sppoti (" + sppotiId + ") not found !");
        }

        if (!sppotiEntity.getUserSppoti().getId().equals(userId)) {
            throw new NotAdminException("You must be the sppoti admin to continue");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public SppotiResponseDTO sendChallenge(int sppotiId, int teamId, Long connectedUserId) {

        //Check if team exist.
        List<TeamEntity> teamEntities = teamRepository.findByUuid(teamId);
        if (teamEntities.isEmpty()) {
            throw new EntityNotFoundException("Team not found (" + teamId + ")");
        }
        TeamEntity challengeTeam = teamEntities.get(0);

        //check if user has rights to send challenge.
        if (!connectedUserId.equals(teamMembersRepository.findByTeamUuidAndAdminTrue(teamId).getUsers().getId())) {
            throw new NotAdminException("You don't have privileges to send challenge");
        }

        //check if sppoti exists.
        SppotiEntity sppotiEntity = sppotiRepository.findByUuid(sppotiId);
        if (sppotiEntity == null) {
            throw new EntityNotFoundException("Sppoti not found (" + sppotiId + ")");
        }

        //Check if sppoti has already an adverse team.
        if (sppotiEntity.getTeamAdverse() != null) {
            throw new BusinessGlobalException("This sppoti is challenged by an other team");
        }

        //Convert team members to sppoters.
        Set<SppotiMemberEntity> sppotiMembers = convertAdverseTeamMembersToSppoters(challengeTeam, sppotiEntity, true);
        sppotiEntity.setSppotiMembers(sppotiMembers);
        sppotiEntity.setTeamAdverse(challengeTeam);
        sppotiEntity.setTeamAdverseStatus(GlobalAppStatusEnum.PENDING);

        //update sppoti.
        SppotiEntity savedSppoti = sppotiRepository.save(sppotiEntity);

        return getSppotiResponse(savedSppoti);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void rateSppoters(List<SppotiRatingDTO> sppotiRatingDTO, int sppotiId) {

        Optional<SppotiEntity> sppotiEntity = Optional.ofNullable(sppotiRepository.findByUuid(sppotiId));

        sppotiEntity.ifPresent(
                se -> sppotiRatingDTO.forEach(sppoter ->
                {
                    Optional<SppotiMemberEntity> ratedSppoter = Optional.of(sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiUuid(sppoter.getSppoterRatedId(), se.getUuid()));
                    ratedSppoter.ifPresent(
                            rs -> {
                                if (rs.getStatus().equals(GlobalAppStatusEnum.PENDING)) {
                                    throw new BusinessGlobalException("Sppoter hasn't accepted sppoti yet");
                                }

                                UserEntity connectUser = getConnectedUser();

                                SppotiRatingEntity sppotiRatingEntity = new SppotiRatingEntity();
                                sppotiRatingEntity.setSppotiEntity(se);
                                sppotiRatingEntity.setRatedSppoter(rs.getTeamMember().getUsers());
                                sppotiRatingEntity.setRatingDate(new Date());
                                sppotiRatingEntity.setRaterSppoter(connectUser);
                                sppotiRatingEntity.setStarsCount(sppoter.getStars());

                                ratingRepository.save(sppotiRatingEntity);

                                SppotiMemberEntity sppotiMemberEntity = sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiUuid(connectUser.getUuid(), sppotiId);
                                sppotiMemberEntity.setHasRateOtherSppoter(Boolean.TRUE);
                                sppotiMembersRepository.save(sppotiMemberEntity);
                            }
                    );
                    ratedSppoter.orElseThrow(() -> new EntityNotFoundException("Sppoter not found"));
                })
        );

        sppotiEntity.orElseThrow(() -> new EntityNotFoundException("Sppoti not found"));


    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public List<SppotiResponseDTO> getAllConfirmedSppoties(int userId, int page) {

        CheckConnectedUserAccessPrivileges(userId);

        Pageable pageable = new PageRequest(page, sppotiSize);

        return sppotiMembersRepository.findByTeamMemberUsersUuidAndStatus(userId, pageable, GlobalAppStatusEnum.CONFIRMED)
                .stream()
                .map(s -> getSppotiResponse(s.getSppoti()))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public List<SppotiResponseDTO> getAllRefusedSppoties(int userId, int page) {

        CheckConnectedUserAccessPrivileges(userId);

        Pageable pageable = new PageRequest(page, sppotiSize);

        return sppotiMembersRepository.findByTeamMemberUsersUuidAndStatus(userId, pageable, GlobalAppStatusEnum.REFUSED)
                .stream()
                .map(s -> getSppotiResponse(s.getSppoti()))
                .collect(Collectors.toList());
    }

    /**
     * @param challengeTeam adverse team.
     * @param sppoti        sppoti id.
     * @return all adverse team as sppoters.
     */
    private Set<SppotiMemberEntity> convertAdverseTeamMembersToSppoters(TeamEntity challengeTeam, SppotiEntity sppoti, boolean fromAdverseTeam) {
        return challengeTeam.getTeamMembers().stream()
                .map(
                        sm -> {
                            SppotiMemberEntity sppotiMember = new SppotiMemberEntity();
                            sppotiMember.setTeamMember(sm);
                            sppotiMember.setSppoti(sppoti);
                            if (fromAdverseTeam) {
                                if (sm.getAdmin()) sppotiMember.setStatus(GlobalAppStatusEnum.CONFIRMED);
                            } else {
                                sppotiMember.setStatus(GlobalAppStatusEnum.PENDING);
                            }
                            return sppotiMember;
                        }

                ).collect(Collectors.toSet());
    }

}