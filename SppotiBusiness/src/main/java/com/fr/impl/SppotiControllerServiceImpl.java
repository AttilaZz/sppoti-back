package com.fr.impl;

import com.fr.commons.dto.SppotiRatingDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.NotificationTypeEnum;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.exception.NoRightToAcceptOrRefuseChallenge;
import com.fr.commons.exception.NotAdminException;
import com.fr.entities.*;
import com.fr.service.SppotiControllerService;
import com.fr.transformers.ScoreTransformer;
import com.fr.transformers.SppotiTransformer;
import com.fr.transformers.UserTransformer;
import com.fr.transformers.impl.SportTransformer;
import com.fr.transformers.impl.TeamMemberTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Component
class SppotiControllerServiceImpl extends AbstractControllerServiceImpl implements SppotiControllerService {

    private Logger LOGGER = Logger.getLogger(SppotiControllerServiceImpl.class);

    @Value("${key.sppotiesPerPage}")
    private int sppotiSize;

    /**
     * Score transformer.
     */
    private final ScoreTransformer scoreTransformer;

    /**
     * Sppoti transformer.
     */
    private final SppotiTransformer sppotiTransformer;

    /**
     * Team member transformer.
     */
    private final TeamMemberTransformer teamMemberTransformer;

    /**
     * User transformer
     */
    private final UserTransformer userTransformer;

    @Autowired
    public SppotiControllerServiceImpl(ScoreTransformer scoreTransformer, SppotiTransformer sppotiTransformer, TeamMemberTransformer teamMemberTransformer, UserTransformer userTransformer) {
        this.scoreTransformer = scoreTransformer;
        this.sppotiTransformer = sppotiTransformer;
        this.teamMemberTransformer = teamMemberTransformer;
        this.userTransformer = userTransformer;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public SppotiDTO saveSppoti(SppotiDTO newSppoti) {

        TeamEntity hostTeam = new TeamEntity();
        SppotiEntity sppoti = sppotiTransformer.dtoToModel(newSppoti);

//        sppoti.setLocation(newSppoti.getAddress());
//        sppoti.setDateTimeStart(newSppoti.getDatetimeStart());
//        sppoti.setTitre(newSppoti.getTitre());
//        sppoti.setMaxTeamCount(newSppoti.getMaxTeamCount());
//        sppoti.setAltitude(newSppoti.getAltitude());
//        sppoti.setLongitude(newSppoti.getLongitude());

        TeamDTO teamDTO = newSppoti.getTeamHost();
        if (teamDTO != null) {

            if (teamDTO.getName() != null) {
                hostTeam.setName(teamDTO.getName());
            }

            if (teamDTO.getLogoPath() != null) {
                hostTeam.setLogoPath(teamDTO.getLogoPath());
            }

            if (teamDTO.getCoverPath() != null) {
                hostTeam.setCoverPath(teamDTO.getCoverPath());
            }

            hostTeam.setTeamMembers(getTeamMembersEntityFromDto(teamDTO.getMembers(), hostTeam, sppoti));
            hostTeam.setSport(sppoti.getSport());
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
        sppoti.setTeamHostEntity(hostTeam);

        Set<SppotiEntity> sppotiEntities = new HashSet<>();
        sppotiEntities.add(sppoti);
        hostTeam.setSppotiEntity(sppotiEntities);


        SppotiEntity savedSppoti = sppotiRepository.save(sppoti);

        //If team has been saved with the sppoti, send email to its members.
        if (teamDTO != null) {
            TeamEntity team = savedSppoti.getTeamHostEntity();
            team.getTeamMembers().forEach(m -> {
                if (!m.getAdmin().equals(Boolean.TRUE)) {
                    sendJoinTeamEmail(team, getUserById(m.getUsers().getId()),
                            teamMembersRepository.findByTeamUuidAndAdminTrue(team.getUuid()));
                }
            });
        }

        //TODO: Send email to the adverse team admin.

        //TODO: Send emails to sppoti members too.

        return sppotiTransformer.modelToDto(savedSppoti);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SppotiDTO getSppotiByUuid(Integer uuid) {

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
    private SppotiDTO getSppotiResponse(SppotiEntity sppoti) {

        if (sppoti == null) {
            throw new EntityNotFoundException("SppotiEntity not found");
        }

        SppotiDTO sppotiDTO = sppotiTransformer.modelToDto(sppoti);

        if (sppoti.getDescription() != null) {
            sppotiDTO.setDescription(sppoti.getDescription());
        }

        if (sppoti.getTags() != null) {
            sppotiDTO.setTags(sppoti.getTags());
        }

        TeamDTO teamHostResponse = fillTeamResponse(sppoti.getTeamHostEntity(), sppoti);
        if (sppoti.getTeamAdverseEntity() != null) {
            TeamDTO teamAdverseResponse = fillTeamResponse(sppoti.getTeamAdverseEntity(), sppoti);
            sppotiDTO.setTeamAdverse(teamAdverseResponse);
            sppotiDTO.setTeamAdverseStatus(sppoti.getTeamAdverseStatusEnum().getValue());
        } else {
            sppotiDTO.setTeamAdverseStatus(GlobalAppStatusEnum.NO_CHALLENGE_YET.getValue());
        }

        sppotiDTO.setTeamHost(teamHostResponse);
        sppotiDTO.setId(sppoti.getUuid());
        sppotiDTO.setRelatedSport(SportTransformer.modelToDto(sppoti.getSport()));

        List<SppotiMemberEntity> sppotiMembers = sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiSportId(sppoti.getUserSppoti().getUuid(), sppoti.getSport().getId());

        sppotiDTO.setSppotiCounter(sppotiMembers.size());
        sppotiDTO.setMySppoti(getConnectedUser().getUuid() == sppoti.getUserSppoti().getUuid());

        //if user is member of a team, get admin of the tem and other informations.
        TeamMemberEntity teamAdmin = teamMembersRepository.findByUsersUuidAndTeamUuidAndAdminTrue(sppoti.getUserSppoti().getUuid(), sppoti.getTeamHostEntity().getUuid());
        if (teamAdmin != null) {
            sppotiDTO.setAdminTeamId(teamAdmin.getUuid());
            sppotiDTO.setAdminUserId(sppoti.getUserSppoti().getUuid());
            sppotiDTO.setConnectedUserId(getConnectedUser().getUuid());
        }

        return sppotiDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deleteSppoti(int id) {

        Optional<SppotiEntity> sppoti = Optional.ofNullable(sppotiRepository.findByUuid(id));

        sppoti.ifPresent(s -> {
            s.setDeleted(true);
            sppotiRepository.save(s);
        });

        sppoti.orElseThrow(() -> new EntityNotFoundException("Trying to delete non existing sppoti"));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public SppotiDTO updateSppoti(SppotiDTO sppotiRequest, int id) {

        SppotiEntity sppoti = sppotiRepository.findByUuid(id);

        if (sppoti == null) {
            throw new EntityNotFoundException("SppotiEntity not found with id: " + id);
        }

        if (!StringUtils.isEmpty(sppotiRequest.getTags())) {
            sppoti.setTags(sppotiRequest.getTags());
        }

        if (!StringUtils.isEmpty(sppotiRequest.getDescription())) {
            sppoti.setDescription(sppotiRequest.getDescription());
        }

        if (sppotiRequest.getDateTimeStart() != null) {
            sppoti.setDateTimeStart(sppotiRequest.getDateTimeStart());
        }

        if (!StringUtils.isEmpty(sppotiRequest.getTitre())) {
            sppoti.setTitre(sppotiRequest.getTitre());
        }

        if (!StringUtils.isEmpty(sppotiRequest.getLocation())) {
            sppoti.setLocation(sppotiRequest.getLocation());
        }

        if (sppotiRequest.getMaxTeamCount() != 0) {
            sppoti.setMaxTeamCount(sppotiRequest.getMaxTeamCount());
        }

        if (sppotiRequest.getVsTeam() != 0) {
            List<TeamEntity> adverseTeam = teamRepository.findByUuid(sppotiRequest.getVsTeam());

            //check if adverse team exist
            if (adverseTeam.isEmpty()) {
                throw new EntityNotFoundException("TeamEntity id not found: " + sppotiRequest.getVsTeam());
            }

            //check if adverse team members are not in conflict with team host members
            sppoti.getTeamHostEntity().getTeamMembers().forEach(
                    hostMember -> adverseTeam.get(0).getTeamMembers().forEach(adverseMember -> {
                        if (hostMember.getUsers().getId().equals(adverseMember.getUsers().getId())) {
                            throw new BusinessGlobalException("Conflict found between host team members and adverse team members");
                        }
                    })
            );

            //Convert team members to sppoters.
            Set<SppotiMemberEntity> sppotiMembers = convertAdverseTeamMembersToSppoters(adverseTeam.get(0), sppoti, false);
            sppoti.setSppotiMembers(sppotiMembers);
            sppoti.setTeamAdverseEntity(adverseTeam.get(0));
        }

        sppoti.setTeamAdverseStatusEnum(GlobalAppStatusEnum.PENDING);
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
    public SppotiDTO updateTeamAdverseChallengeStatus(int sppotiId, int adverseTeamResponseStatus) {

        SppotiEntity sppotiEntity = sppotiRepository.findByUuid(sppotiId);

        Optional adverseTeamAdmin = sppotiEntity.getTeamAdverseEntity().getTeamMembers().stream()
                .filter(TeamMemberEntity::getAdmin)
                .filter(t -> t.getId().equals(getConnectedUser().getId()))
                .findFirst();

        if (!adverseTeamAdmin.isPresent()) {
            throw new NoRightToAcceptOrRefuseChallenge("User (" + getConnectedUser().toString() + " is not admin of team (" + sppotiEntity.getTeamAdverseEntity() + ")");
        }

        //set new status.
        for (GlobalAppStatusEnum status : GlobalAppStatusEnum.values()) {
            if (status.getValue() == adverseTeamResponseStatus) {
                sppotiEntity.setTeamAdverseStatusEnum(status);
            }
        }

        SppotiEntity savedSppoti = sppotiRepository.save(sppotiEntity);

        return getSppotiResponse(savedSppoti);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SppotiDTO> getAllUserSppoties(Integer id, int page) {

        Pageable pageable = new PageRequest(page, sppotiSize);

        List<SppotiEntity> sppoties = sppotiRepository.findByUserSppotiUuidAndTeamAdverseStatusEnumNot(id, GlobalAppStatusEnum.REFUSED, pageable);

        return sppoties.stream()
                .map(this::getSppotiResponse)
                .sorted((t2, t1) -> t1.getDatetimeCreated().compareTo(t2.getDatetimeCreated()))
                .collect(Collectors.toList());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SppotiDTO> getAllJoinedSppoties(int userId, int page) {
        Pageable pageable = new PageRequest(page, sppotiSize);

        List<SppotiMemberEntity> sppotiMembers = sppotiMembersRepository.findByTeamMemberUsersUuidAndStatusNot(userId, GlobalAppStatusEnum.REFUSED, pageable);

        return sppotiMembers.stream()
                .map(s -> getSppotiResponse(s.getSppoti()))
                .sorted((t2, t1) -> t1.getDatetimeCreated().compareTo(t2.getDatetimeCreated()))
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
    public SppotiDTO sendChallenge(int sppotiId, int teamId, Long connectedUserId) {

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
        if (sppotiEntity.getTeamAdverseEntity() != null) {
            throw new BusinessGlobalException("This sppoti is challenged by an other team");
        }

        //check if adverse team members are not in conflict with team host members
        sppotiEntity.getTeamHostEntity().getTeamMembers().forEach(
                hostMember -> challengeTeam.getTeamMembers().forEach(adverseMember -> {
                    if (hostMember.getUsers().getId().equals(adverseMember.getUsers().getId())) {
                        throw new BusinessGlobalException("Conflict found between host team members and adverse team members");
                    }
                })
        );

        //Convert team members to sppoters.
        Set<SppotiMemberEntity> sppotiMembers = convertAdverseTeamMembersToSppoters(challengeTeam, sppotiEntity, true);
        sppotiEntity.setSppotiMembers(sppotiMembers);
        sppotiEntity.setTeamAdverseEntity(challengeTeam);
        sppotiEntity.setTeamAdverseStatusEnum(GlobalAppStatusEnum.PENDING);

        //update sppoti.
        SppotiEntity savedSppoti = sppotiRepository.save(sppotiEntity);

        return getSppotiResponse(savedSppoti);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<UserDTO> rateSppoters(List<SppotiRatingDTO> sppotiRatingDTO, int sppotiId) {

        Optional<SppotiEntity> sppotiEntity = Optional.ofNullable(sppotiRepository.findByUuid(sppotiId));

        if (sppotiEntity.isPresent()) {
            SppotiEntity se = sppotiEntity.get();

            return sppotiRatingDTO.stream()
                    .map(sppoter ->
                    {
                        Optional<SppotiMemberEntity> ratedSppoter = Optional.ofNullable(sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiUuid(sppoter.getSppoterRatedId(), se.getUuid()));
                        ratedSppoter.orElseThrow(() -> new EntityNotFoundException("Sppoter (" + sppoter.getId() + ") not found"));

                        SppotiMemberEntity rs = ratedSppoter.get();
                        if (rs.getStatus().equals(GlobalAppStatusEnum.PENDING)) {
                            throw new BusinessGlobalException("Sppoter (" + sppoter.getId() + ") hasn't accepted sppoti yet");
                        }

                        UserEntity connectUser = getConnectedUser();

                        SppotiRatingEntity sppotiRatingEntity = new SppotiRatingEntity();
                        sppotiRatingEntity.setSppotiEntity(se);
                        sppotiRatingEntity.setRatedSppoter(rs.getTeamMember().getUsers());
                        sppotiRatingEntity.setRatingDate(new Date());
                        sppotiRatingEntity.setRaterSppoter(connectUser);
                        sppotiRatingEntity.setStarsCount(sppoter.getStars());

                        ratingRepository.save(sppotiRatingEntity);

                        //TODO: test this bloc
                        SppotiMemberEntity sppotiMemberEntity = sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiUuid(connectUser.getUuid(), sppotiId);
                        sppotiMemberEntity.setHasRateOtherSppoter(Boolean.TRUE);
                        sppotiMembersRepository.save(sppotiMemberEntity);

                        return teamMemberTransformer.modelToDto(sppotiMemberEntity.getTeamMember(), se);

                    }).collect(Collectors.toList());
        }

        throw new EntityNotFoundException("Sppoti not found");

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public List<SppotiDTO> getAllConfirmedSppoties(int userId, int page) {

        CheckConnectedUserAccessPrivileges(userId);

        Pageable pageable = new PageRequest(page, sppotiSize);

        return sppotiMembersRepository.findByTeamMemberUsersUuidAndStatus(userId, pageable, GlobalAppStatusEnum.CONFIRMED)
                .stream()
                .map(s -> getSppotiResponse(s.getSppoti()))
                .sorted((t2, t1) -> t1.getDatetimeCreated().compareTo(t2.getDatetimeCreated()))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public List<SppotiDTO> getAllRefusedSppoties(int userId, int page) {

        CheckConnectedUserAccessPrivileges(userId);

        Pageable pageable = new PageRequest(page, sppotiSize);

        return sppotiMembersRepository.findByTeamMemberUsersUuidAndStatus(userId, pageable, GlobalAppStatusEnum.REFUSED)
                .stream()
                .map(s -> getSppotiResponse(s.getSppoti()))
                .sorted((t2, t1) -> t1.getDatetimeCreated().compareTo(t2.getDatetimeCreated()))
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

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public UserDTO addSppoter(int sppotiId, UserDTO user) {

        Optional<SppotiEntity> sppotiEntity = Optional.ofNullable(sppotiRepository.findByUuid(sppotiId));

        if (sppotiEntity.isPresent()) {

            SppotiEntity userSppoti = sppotiEntity.get();
            TeamEntity userTeam = userSppoti.getTeamHostEntity();
            UserEntity userSppoter = getUserByUuId(user.getId());

            if (userSppoter == null) {
                throw new EntityNotFoundException("UserDTO with id (" + user.getId() + ") Not found");
            }

            if (!userSppoti.getUserSppoti().getId().equals(getConnectedUser().getId())) {
                throw new NotAdminException("You must be the sppoti admin to access this service");
            }

            TeamMemberEntity teamMembers = new TeamMemberEntity();
            teamMembers.setTeam(userTeam);
            teamMembers.setUsers(userSppoter);

             /* Convert team members to sppoters. */
            TeamMemberEntity teamMember = new TeamMemberEntity();
            SppotiMemberEntity sppoter = new SppotiMemberEntity();

            if (StringUtils.isEmpty(user.getxPosition())) {
                teamMembers.setxPosition(user.getxPosition());
                sppoter.setxPosition(user.getxPosition());
            }

            if (StringUtils.isEmpty(user.getyPosition())) {
                teamMembers.setyPosition(user.getyPosition());
                sppoter.setyPosition(user.getyPosition());
            }

            Set<SppotiMemberEntity> sppotiMembers = new HashSet<>();
            sppoter.setTeamMember(teamMember);
            sppoter.setSppoti(userSppoti);
            sppotiMembers.add(sppoter);

            teamMember.setSppotiMembers(sppotiMembers);
            userSppoti.setSppotiMembers(sppotiMembers);

            //save new member.
            teamMembersRepository.save(teamMembers);

            //Send email to the new team member.
            //sendJoinTeamEmail(team, sppoter, teamAdmin);

            //return new member.
            return teamMemberTransformer.modelToDto(teamMembers, userSppoti);
        }

        throw new EntityNotFoundException("Sppoti (" + sppotiId + ") not found");

    }
}
