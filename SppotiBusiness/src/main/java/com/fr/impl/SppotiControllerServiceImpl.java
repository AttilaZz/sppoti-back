package com.fr.impl;

import com.fr.commons.dto.SppotiRatingDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.NotificationTypeEnum;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.exception.NotAdminException;
import com.fr.entities.*;
import com.fr.mail.SppotiMailer;
import com.fr.service.SppotiControllerService;
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

    /**
     * Returned sppoti list size.
     */
    @Value("${key.sppotiesPerPage}")
    private int sppotiSize;

    /**
     * {@link SportEntity} transformer.
     */
    private final SportTransformer sportTransformer;

    /**
     * {@link SppotiEntity} transformer.
     */
    private final SppotiTransformer sppotiTransformer;

    /**
     * {@link TeamMemberEntity} transformer.
     */
    private final TeamMemberTransformer teamMemberTransformer;

    /**
     * {@link UserEntity} transformer
     */
    private final UserTransformer userTransformer;

    /**
     * Sppoti mailer.
     */
    private final SppotiMailer sppotiMailer;

    /**
     * Init services.
     */
    @Autowired
    public SppotiControllerServiceImpl(SportTransformer sportTransformer, SppotiTransformer sppotiTransformer, TeamMemberTransformer teamMemberTransformer, UserTransformer userTransformer, SppotiMailer sppotiMailer) {
        this.sportTransformer = sportTransformer;
        this.sppotiTransformer = sppotiTransformer;
        this.teamMemberTransformer = teamMemberTransformer;
        this.userTransformer = userTransformer;
        this.sppotiMailer = sppotiMailer;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public SppotiDTO saveSppoti(SppotiDTO newSppoti) {

        TeamEntity hostTeam = new TeamEntity();
        SppotiEntity sppoti = sppotiTransformer.dtoToModel(newSppoti);
        sppoti.setUserSppoti(getConnectedUser());
        Set<SppotiEntity> sppotiEntities = new HashSet<>();
        sppotiEntities.add(sppoti);
        hostTeam.setSppotiEntity(sppotiEntities);

        TeamDTO teamDTO = newSppoti.getTeamHost();
        if (teamDTO != null) {

            if (StringUtils.hasText(teamDTO.getName())) {
                hostTeam.setName(teamDTO.getName());
            }

            if (StringUtils.hasText(teamDTO.getName())) {
                hostTeam.setLogoPath(teamDTO.getLogoPath());
            }

            if (StringUtils.hasText(teamDTO.getName())) {
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

            if (!hostTeam.getSport().equals(sppoti.getSport())) {
                throw new BusinessGlobalException("Host team sport not as same as sppoti sport !");
            }

            //Convert team members to sppoters.
            Set<SppoterEntity> sppotiMembers = hostTeam.getTeamMembers().stream()
                    .map(
                            sm -> {
                                SppoterEntity sppotiMember = new SppoterEntity();
                                sppotiMember.setTeamMember(sm);
                                sppotiMember.setSppoti(sppoti);
                                if (getConnectedUser().getId().equals(sm.getUsers().getId())) {
                                    sppotiMember.setStatus(GlobalAppStatusEnum.CONFIRMED);
                                }
                                return sppotiMember;
                            }

                    ).collect(Collectors.toSet());
            sppoti.setSppotiMembers(sppotiMembers);

        } else {
            throw new EntityNotFoundException("Host team not found in the request");
        }

        sppoti.setTeamHostEntity(hostTeam);

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
        SppotiDTO sppotiDTO = sppotiTransformer.modelToDto(savedSppoti);
        //send email to sppoters.
        sppoti.getTeamHostEntity().getTeamMembers()
                .forEach(m -> {
                    //exclude sppoti admin from the email.
                    if (!m.getUsers().getId().equals(sppoti.getUserSppoti().getId())) {
                        new Thread(() -> this.sppotiMailer.sendJoinSppotiEmail(sppotiDTO,
                                userTransformer.modelToDto(m.getUsers()),
                                userTransformer.modelToDto(sppoti.getUserSppoti()))).start();
                    }
                });

        return sppotiDTO;

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
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Transactional
    @Override
    public SppotiDTO updateSppoti(SppotiDTO sppotiRequest, int id) {

        SppotiEntity sppoti = sppotiRepository.findByUuid(id);
        if (sppoti == null) throw new EntityNotFoundException("SppotiEntity not found with id: " + id);

        if (StringUtils.hasText(sppotiRequest.getTags())) {
            sppoti.setTags(sppotiRequest.getTags());
        }

        if (StringUtils.hasText(sppotiRequest.getDescription())) {
            sppoti.setDescription(sppotiRequest.getDescription());
        }

        if (sppotiRequest.getDateTimeStart() != null) {
            sppoti.setDateTimeStart(sppotiRequest.getDateTimeStart());
        }

        if (StringUtils.hasText(sppotiRequest.getName())) {
            sppoti.setName(sppotiRequest.getName());
        }

        if (StringUtils.hasText(sppotiRequest.getLocation())) {
            sppoti.setLocation(sppotiRequest.getLocation());
        }

        if (sppotiRequest.getMaxMembersCount() != null && sppotiRequest.getMaxTeamCount() != 0) {
            sppoti.setMaxTeamCount(sppotiRequest.getMaxTeamCount());
        }

        if (sppotiRequest.getVsTeam() != 0) {
            List<TeamEntity> adverseTeam = teamRepository.findByUuid(sppotiRequest.getVsTeam());

            //check if adverse team exist
            if (adverseTeam.isEmpty()) {
                throw new EntityNotFoundException("TeamEntity id not found: " + sppotiRequest.getVsTeam());
            }
            TeamEntity team = adverseTeam.get(0);

            if (!team.getSport().getId().equals(sppoti.getSport().getId())) {
                throw new BusinessGlobalException("Adverse team sport not as same as sppoti sport !");
            }

            //check if adverse team members are not in conflict with team host members
            sppoti.getTeamHostEntity().getTeamMembers().forEach(
                    hostMember -> team.getTeamMembers().forEach(adverseMember -> {
                        if (hostMember.getUsers().getId().equals(adverseMember.getUsers().getId())) {
                            throw new BusinessGlobalException("Conflict found between host team members and adverse team members");
                        }
                    })
            );

            // Add team in sppoti adverse teams list
            SppotiAdverseEntity adverse = new SppotiAdverseEntity();
            adverse.setSppoti(sppoti);
            adverse.setTeam(team);
            adverse.setFromSppotiAdmin(Boolean.TRUE);
            sppoti.getAdverseTeams().add(adverse);
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

        Optional<SppoterEntity> optional = Optional.ofNullable(sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiUuid(userId, sppotiId));

        optional.ifPresent(
                sm -> {
                    //update sppoter status.
                    sm.setStatus(GlobalAppStatusEnum.CONFIRMED);
                    SppoterEntity updatedSppoter = sppotiMembersRepository.save(sm);

                    //Send notification to sppoti admin.
                    if (updatedSppoter != null) {
                        addNotification(NotificationTypeEnum.X_ACCEPTED_YOUR_SPPOTI_INVITATION, sm.getTeamMember().getUsers(), sm.getSppoti().getUserSppoti(), null, null);
                    }

                    //update team member status.
                    TeamMemberEntity teamMembers = sm.getTeamMember();
                    if (!teamMembers.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)) {
                        teamMembers.setStatus(GlobalAppStatusEnum.CONFIRMED);
                    }

                    TeamMemberEntity updatedTeamMember = teamMembersRepository.save(teamMembers);

                    //Send notification to team admin.
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

        SppoterEntity sppotiMembers = sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiUuid(sppotiId, userId);

        if (sppotiMembers == null) {
            throw new EntityNotFoundException("Sppoter not found");
        }

        sppotiMembers.setStatus(GlobalAppStatusEnum.REFUSED);
        SppoterEntity updatedSppoter = sppotiMembersRepository.save(sppotiMembers);

        //Send notification to sppoti admin.
        if (updatedSppoter != null) {
            addNotification(NotificationTypeEnum.X_REFUSED_YOUR_SPPOTI_INVITATION, sppotiMembers.getTeamMember().getUsers(), sppotiMembers.getSppoti().getUserSppoti(), null, null);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SppotiDTO> getAllUserSppoties(Integer id, int page) {

        Pageable pageable = new PageRequest(page, sppotiSize);

        List<SppotiEntity> sppoties = sppotiRepository.findByUserSppotiUuid(id, pageable);

        return sppoties.stream()
                //.filter(s -> !s.getTeamAdverseStatusEnum().equals(GlobalAppStatusEnum.REFUSED))
                .map(this::getSppotiResponse)
                .sorted((t2, t1) -> t1.getDatetimeCreated().compareTo(t2.getDatetimeCreated()))
                .collect(Collectors.toList());

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

        //check if team sport is as same as sppoti sport.
        if (!challengeTeam.getSport().getId().equals(sppotiEntity.getSport().getId())) {
            throw new BusinessGlobalException("challenged team sport not as same as sppoti sport !");
        }

        //Check if sppoti has already a CONFIRMED adverse team in the adverse team list.
        if (sppotiEntity.getAdverseTeams() != null && sppotiEntity.getAdverseTeams().stream()
                .anyMatch(t -> t.getStatus().equals(GlobalAppStatusEnum.CONFIRMED))) {
            throw new BusinessGlobalException("This sppoti has already an adverse team");
        }

        //check if adverse team members are not in conflict with team host members
        sppotiEntity.getTeamHostEntity().getTeamMembers().forEach(
                hostMember -> challengeTeam.getTeamMembers().forEach(adverseMember -> {
                    if (hostMember.getUsers().getId().equals(adverseMember.getUsers().getId())) {
                        throw new BusinessGlobalException("Conflict found between host team members and adverse team members");
                    }
                })
        );

        SppotiAdverseEntity adverse = new SppotiAdverseEntity();
        adverse.setSppoti(sppotiEntity);
        adverse.setTeam(challengeTeam);
        sppotiEntity.getAdverseTeams().add(adverse);

        //update sppoti.
        SppotiEntity savedSppoti = sppotiRepository.save(sppotiEntity);

        return getSppotiResponse(savedSppoti);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void chooseOneAdverseTeamFromAllRequests(int sppotiId, TeamDTO teamDTO) {

        //Check if sppoti exist and has no confirmed adverse team yet.
        Optional<SppotiEntity> sppotiEntityOptional = Optional.ofNullable(sppotiRepository.findByUuid(sppotiId));
        sppotiEntityOptional.orElseThrow(() -> new EntityNotFoundException("Sppoti (" + sppotiId + ") not found !"));

        sppotiEntityOptional.ifPresent(sp -> {
            //check if sppoti has already an adverse team.
            if (sp.getAdverseTeams().stream().anyMatch(ad -> ad.getStatus().equals(GlobalAppStatusEnum.CONFIRMED))) {
                throw new BusinessGlobalException("This sppoti has already an adverse team");
            }

            //check if team exist, and it's already pending id sppoti adverse teams.
            List<TeamEntity> teamEntities = teamRepository.findByUuid(teamDTO.getId());
            if (teamEntities.isEmpty()) {
                throw new EntityNotFoundException("Team (" + teamDTO.getId() + ") not found");
            }
            teamEntities.stream().findFirst().ifPresent(tad -> {
                //check if connected user has rights to answer the challenge.
                if (!this.getConnectedUser().getId().equals(sp.getUserSppoti().getId())) {
                    throw new NotAdminException("Only sppoti admin can answer to this challenge!");
                }

                //get selected adverse team from all requests.
                sp.getAdverseTeams().stream().filter(a -> a.getTeam().getId().equals(tad.getId())).findFirst()
                        .ifPresent(teamAdverse -> {
                            //update team adverse status.
                            teamAdverse.setStatus(GlobalAppStatusEnum.valueOf(teamDTO.getTeamAdverseStatus()));
                            sppotiRepository.save(sp);

                            //get team adverse admin.
                            UserEntity teamAdverseAdmin = tad.getTeamMembers().stream()
                                    .filter(t -> t.getAdmin().equals(true) && t.getTeam().getUuid() == teamDTO.getId())
                                    .findFirst().get().getUsers();

                            //notify team adverse admin.
                            addNotification(NotificationTypeEnum.X_ACCEPTED_YOUR_SPPOTI_INVITATION, sp.getUserSppoti(),
                                    teamAdverseAdmin, null, sp);

                            //Convert team members to sppoters if status equals to confirmed
                            if (GlobalAppStatusEnum.valueOf(teamDTO.getTeamAdverseStatus())
                                    .equals(GlobalAppStatusEnum.CONFIRMED)) {
                                Set<SppoterEntity> sppotiMembers = convertAdverseTeamMembersToSppoters
                                        (teamAdverse.getTeam(), sp, true);
                                sp.setSppotiMembers(sppotiMembers);
                                sppotiRepository.save(sp);
                            }
                        });
            });
        });
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
                        Optional<SppoterEntity> ratedSppoter = Optional.ofNullable(sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiUuid(sppoter.getSppoterRatedId(), se.getUuid()));
                        ratedSppoter.orElseThrow(() -> new EntityNotFoundException("Sppoter (" + sppoter.getSppoterRatedId() + ") not found"));

                        SppoterEntity rs = ratedSppoter.get();
                        //Deny rating of a sppoter who hasn't accepted sppoti yet !
                        if (rs.getStatus().equals(GlobalAppStatusEnum.PENDING)) {
                            throw new BusinessGlobalException("Sppoter (" + sppoter.getId() + ") hasn't accepted sppoti yet");
                        }

                        UserEntity connectUser = getConnectedUser();

                        //FIXME: create transformer for rating
                        SppotiRatingEntity sppotiRatingEntity = new SppotiRatingEntity();
                        sppotiRatingEntity.setSppotiEntity(se);
                        sppotiRatingEntity.setRatedSppoter(rs.getTeamMember().getUsers());
                        sppotiRatingEntity.setRatingDate(new Date());
                        sppotiRatingEntity.setRaterSppoter(connectUser);
                        sppotiRatingEntity.setStarsCount(sppoter.getStars());
                        ratingRepository.save(sppotiRatingEntity);

                        //Flag rated sppoter to true.
                        //This means that sppoti admin has rate this sppoter
                        SppoterEntity sppoterRaterEntity = sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiUuid(connectUser.getUuid(), sppotiId);
                        sppoterRaterEntity.setHasRateOtherSppoter(Boolean.TRUE);
                        sppotiMembersRepository.save(sppoterRaterEntity);

                        //Get RATED SPPOTER.
                        SppoterEntity sppoterRatedEntity = sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiUuid(sppoter.getSppoterRatedId(), sppotiId);
                        return teamMemberTransformer.modelToDto(sppoterRatedEntity.getTeamMember(), se);

                    }).collect(Collectors.toList());
        }

        throw new EntityNotFoundException("Sppoti not found");
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
    @Override
    public List<SppotiDTO> getAllJoinedSppoties(int userId, int page) {

        CheckConnectedUserAccessPrivileges(userId);

        Pageable pageable = new PageRequest(page, sppotiSize);

        List<SppoterEntity> sppotiMembers = sppotiMembersRepository.findByTeamMemberUsersUuid(userId, pageable);

        return sppotiMembers.stream()
                .filter(s -> s.getSppoti().getUserSppoti().getUuid() != userId)
                .map(s -> getSppotiResponse(s.getSppoti()))
                .sorted((t2, t1) -> t1.getDatetimeCreated().compareTo(t2.getDatetimeCreated()))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public List<SppotiDTO> getAllConfirmedSppoties(int userId, int page) {

        CheckConnectedUserAccessPrivileges(userId);

        Pageable pageable = new PageRequest(page, sppotiSize);

        return sppotiMembersRepository.findByTeamMemberUsersUuid(userId, pageable)
                .stream()
                .filter(m -> m.getStatus().equals(GlobalAppStatusEnum.CONFIRMED))
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

        return sppotiMembersRepository.findByTeamMemberUsersUuid(userId, pageable)
                .stream()
                .filter(m -> m.getStatus().equals(GlobalAppStatusEnum.REFUSED))
                .map(s -> getSppotiResponse(s.getSppoti()))
                .sorted((t2, t1) -> t1.getDatetimeCreated().compareTo(t2.getDatetimeCreated()))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SppotiDTO> getAllUpcomingSppoties(int userId, int page) {

        CheckConnectedUserAccessPrivileges(userId);

        Pageable pageable = new PageRequest(page, sppotiSize);

        return sppotiRepository.findByUserSppotiUuid(userId, pageable)
                .stream()
                .filter(s -> s.getAdverseTeams().stream().anyMatch(t -> t.getStatus().name()
                        .equals(GlobalAppStatusEnum.CONFIRMED.name()) ||
                        (t.getStatus().name().equals(GlobalAppStatusEnum.CONFIRMED.name()) &&
                                t.getTeam().getTeamMembers().stream()
                                        .anyMatch(am -> Integer.compare(am.getUsers().getUuid(), userId) == 0))) ||
                        s.getTeamHostEntity().getTeamMembers().stream().anyMatch(t -> t.getUsers()
                                .getUuid() == userId))
                .map(sppotiTransformer::modelToDto)
                .sorted((t2, t1) -> t1.getDatetimeCreated().compareTo(t2.getDatetimeCreated()))
                .collect(Collectors.toList());
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
            SppoterEntity sppoter = new SppoterEntity();

            //team member.
            teamMembers.setTeam(userTeam);
            teamMembers.setUsers(userSppoter);

            //sppoter.
            sppoter.setTeamMember(teamMembers);
            sppoter.setSppoti(userSppoti);

            Set<SppoterEntity> sppotiMembers = new HashSet<>();
            sppotiMembers.add(sppoter);

            //Link sppoter with team member.
            teamMembers.setSppotiMembers(sppotiMembers);

            //add coordinate if set.
            if (StringUtils.isEmpty(user.getxPosition())) {
                teamMembers.setxPosition(user.getxPosition());
                sppoter.setxPosition(user.getxPosition());
            }

            if (StringUtils.isEmpty(user.getyPosition())) {
                teamMembers.setyPosition(user.getyPosition());
                sppoter.setyPosition(user.getyPosition());
            }

            //save new member and sppoter.
            TeamMemberEntity savedMember = teamMembersRepository.save(teamMembers);

            //Send email to the new team member.
            //sendJoinTeamEmail(team, sppoter, teamAdmin);

            //return new member.
            return teamMemberTransformer.modelToDto(savedMember, userSppoti);
        }

        throw new EntityNotFoundException("Sppoti (" + sppotiId + ") not found");

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

        sppoti.setConnectedUserId(getConnectedUser().getId());
        SppotiDTO sppotiDTO = sppotiTransformer.modelToDto(sppoti);

        if (StringUtils.hasText(sppoti.getDescription())) {
            sppotiDTO.setDescription(sppoti.getDescription());
        }

        if (StringUtils.hasText(sppoti.getTags())) {
            sppotiDTO.setTags(sppoti.getTags());
        }

        TeamDTO teamHostResponse = fillTeamResponse(sppoti.getTeamHostEntity(), sppoti);

        sppotiDTO.setTeamHost(teamHostResponse);
        sppotiDTO.setId(sppoti.getUuid());
        sppotiDTO.setRelatedSport(sportTransformer.modelToDto(sppoti.getSport()));

        List<SppoterEntity> sppotiMembers = sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiSportId(sppoti.getUserSppoti().getUuid(), sppoti.getSport().getId());

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
     * @param challengeTeam adverse team.
     * @param sppoti        sppoti id.
     * @return all adverse team as sppoters.
     */
    private Set<SppoterEntity> convertAdverseTeamMembersToSppoters(TeamEntity challengeTeam, SppotiEntity sppoti, boolean fromAdverseTeam) {
        return challengeTeam.getTeamMembers().stream()
                .map(
                        sm -> {
                            SppoterEntity sppotiMember = new SppoterEntity();
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
