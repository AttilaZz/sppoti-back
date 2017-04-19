package com.fr.impl;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.commons.dto.SportDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.exception.NotAdminException;
import com.fr.commons.exception.TeamMemberNotFoundException;
import com.fr.entities.*;
import com.fr.mail.TeamMailer;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.NotificationTypeEnum;
import com.fr.repositories.*;
import com.fr.service.AbstractControllerService;
import com.fr.transformers.impl.TeamMemberTransformer;
import com.fr.transformers.impl.TeamTransformerImpl;
import com.fr.transformers.impl.UserTransformerImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional(readOnly = true)
@Component("abstractService")
abstract class AbstractControllerServiceImpl implements AbstractControllerService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SportRepository sportRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    EditHistoryRepository editHistoryRepository;
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    FriendShipRepository friendShipRepository;
    @Autowired
    SppotiRepository sppotiRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TeamMembersRepository teamMembersRepository;
    @Autowired
    SppotiMembersRepository sppotiMembersRepository;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    TeamMailer teamMailer;

    @Autowired
    private Environment environment;

    @Autowired
    private TeamMemberTransformer teamMemberTransformer;

    @Autowired
    private UserTransformerImpl userTransformer;

    @Autowired
    private TeamTransformerImpl teamTransformer;

    private Logger LOGGER = Logger.getLogger(AbstractControllerServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getUserRole() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ArrayList<GrantedAuthority> roles;

        List<String> userRoles = new ArrayList<String>();

        if (principal instanceof UserDetails) {
            roles = (ArrayList<GrantedAuthority>) ((UserDetails) principal).getAuthorities();
            for (GrantedAuthority role : roles) {
                userRoles.add(role.getAuthority());
            }
        }
        return userRoles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthenticationUsername() {
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getUserLoginType(String username) {
        String numberRegex = "[0-9]+";
        int loginType;

        if (username.contains("@")) {
            loginType = 2;
        } else if (username.matches(numberRegex)) {
            loginType = 3;
        } else {
            loginType = 1;
        }

        return loginType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.getByIdAndDeletedFalse(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getUserByUuId(int id) {

        Optional<UserEntity> usersList = userRepository.getByUuidAndDeletedFalse(id);

        return usersList.orElse(null);

    }

    protected Properties globalAddressConfigProperties() {
        Properties properties = new Properties();
        properties.put("rootAddress", environment.getRequiredProperty("rootAddress"));

        return properties;
    }

    /**
     * @param dsHistoryList lost of edited content.
     * @return list of ContentEditedResponseDTO.
     */
    protected List<ContentEditedResponseDTO> fillEditContentResponse(List<EditHistoryEntity> dsHistoryList) {
        List<ContentEditedResponseDTO> editHistoryResponse = new ArrayList<ContentEditedResponseDTO>();
        editHistoryResponse.clear();
        for (EditHistoryEntity editContent : dsHistoryList) {

            ContentEditedResponseDTO cer = new ContentEditedResponseDTO();
            cer.setDateTime(editContent.getDatetimeEdited());
            cer.setId(editContent.getId());
            cer.setText(editContent.getText());

            editHistoryResponse.add(cer);
        }

        return editHistoryResponse;
    }

    /**
     * @return connected user entity
     */
    protected UserEntity getConnectedUser() {
        UserDetails accountUserDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.getUserByLogin(accountUserDetails.getUsername());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getUserByLogin(String username) {
        String numberRegex = "[0-9]+";

        if (username.contains("@")) {
            return this.userRepository.getByEmailAndDeletedFalse(username);
        } else if (username.matches(numberRegex)) {
            return this.userRepository.getByTelephoneAndDeletedFalse(username);
        } else {
            return this.userRepository.getByUsernameAndDeletedFalse(username);
        }
    }

    /**
     * @param dbCommentEntityList like entity.
     * @param userId              user id.
     * @return list of Comment DTO
     */
    protected List<CommentDTO> fillCommentModelList(List<CommentEntity> dbCommentEntityList, Long userId) {
        List<CommentDTO> myList = new ArrayList<CommentDTO>();

        for (CommentEntity commentEntity : dbCommentEntityList) {
            int commentId = commentEntity.getUuid();
            CommentDTO cm = new CommentDTO();

//            if (!userDaoService.getLastAvatar(userId).isEmpty())
//                cm.setAuthorAvatar(userDaoService.getLastAvatar(userId).get(0).getUrl());

            UserDTO userCoverAndAvatar = userTransformer.getUserCoverAndAvatar(
                    commentEntity.getUser());
            cm.setAuthorAvatar(userCoverAndAvatar.getAvatar());
            cm.setAuthorFirstName(commentEntity.getUser().getFirstName());
            cm.setAuthorLastName(commentEntity.getUser().getLastName());
            cm.setCreationDate(commentEntity.getDatetimeCreated());
            cm.setId(commentId);
            cm.setImageLink(commentEntity.getImageLink());
            cm.setMyComment(commentEntity.getUser().getId().equals(userId));

            boolean isCommentLikedByMe = isContentLikedByUser(commentEntity, userId);
            cm.setLikedByUser(isCommentLikedByMe);
            cm.setLikeCount(commentEntity.getLikes().size());

            List<EditHistoryEntity> editHistory = editHistoryRepository.getByCommentUuidOrderByDatetimeEditedDesc(commentId);

            if (!editHistory.isEmpty()) {
                cm.setEdited(true);

                EditHistoryEntity ec = editHistory.get(0);

                cm.setCreationDate(ec.getDatetimeEdited());
                cm.setText(ec.getText());
            } else {
                cm.setText(commentEntity.getContent());
                cm.setCreationDate(commentEntity.getDatetimeCreated());
            }

            cm.setLikeCount(commentEntity.getLikes().size());

            myList.add(cm);
        }

        return myList;
    }

    /**
     * @param o      post or like entity.
     * @param userId liker id.
     * @return true if content has been liked by me, false otherwise
     */
    // detect if post or like has already been liked by user
    protected boolean isContentLikedByUser(Object o, Long userId) {

        List<LikeContentEntity> lp = new ArrayList<LikeContentEntity>();
        PostEntity p;
        CommentEntity c;

        if (o instanceof PostEntity) {
            p = (PostEntity) o;
            lp.addAll(p.getLikes());
        } else if (o instanceof CommentEntity) {
            c = (CommentEntity) o;
            lp.addAll(c.getLikes());
        }

        for (LikeContentEntity likePost : lp) {
            if (likePost.getUser().getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param targetUser    user entity.
     * @param connectedUser connect user id.
     * @return user DTO.
     */
    protected UserDTO fillUserResponse(UserEntity targetUser, UserEntity connectedUser) {

        UserDTO user = new UserDTO();
        user.setLastName(targetUser.getLastName());
        user.setFirstName(targetUser.getFirstName());
        user.setUsername(targetUser.getUsername());
        user.setEmail(targetUser.getEmail());
        user.setPhone(targetUser.getTelephone());
        user.setId(targetUser.getUuid());
        user.setLanguage(targetUser.getLanguageEnum().name());
        user.setBirthDate(targetUser.getDateBorn());
        user.setFriendStatus(GlobalAppStatusEnum.PUBLIC_RELATION.getValue());
        user.setGender(targetUser.getGender().name());

        if (connectedUser != null) {
            if (!connectedUser.getId().equals(targetUser.getId())) {
                /* manage requests sent to me. */
                FriendShipEntity friendShip;

                friendShip = friendShipRepository.findByFriendUuidAndUserUuidAndDeletedFalse(connectedUser.getUuid(), targetUser.getUuid());

                if (friendShip == null) {
                    friendShip = friendShipRepository.findByFriendUuidAndUserUuidAndDeletedFalse(targetUser.getUuid(), connectedUser.getUuid());
                }

                if (friendShip == null) {
                    user.setFriendStatus(GlobalAppStatusEnum.PUBLIC_RELATION.getValue());
                } else {

                    //We are friend.
                    if (friendShip.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)) {
                        user.setFriendStatus(GlobalAppStatusEnum.CONFIRMED.getValue());

                        //Friend request waiting to be accepted by me.
                    } else if (friendShip.getStatus().equals(GlobalAppStatusEnum.PENDING)) {
                        user.setFriendStatus(GlobalAppStatusEnum.PENDING.getValue());

                        //Friend request refused by me.
                    } else if (friendShip.getStatus().equals(GlobalAppStatusEnum.REFUSED)) {
                        user.setFriendStatus(GlobalAppStatusEnum.REFUSED.getValue());

                    }
                }
                /*  Manage request sent by me. */
                if (!friendShipRepository.findByUserUuidAndFriendUuidAndStatusAndDeletedFalse(targetUser.getUuid(), connectedUser.getUuid(), GlobalAppStatusEnum.PENDING).isEmpty()) {
                    user.setFriendStatus(GlobalAppStatusEnum.PENDING_SENT.getValue());
                }
            }
            user.setMyProfile(connectedUser.getId().equals(targetUser.getId()));
        } else {
            user.setMyProfile(true);
        }

        /* Manage resources. */
        UserDTO userCoverAndAvatar = userTransformer.getUserCoverAndAvatar(targetUser);
        user.setCover(userCoverAndAvatar.getCover());
        user.setAvatar(userCoverAndAvatar.getAvatar());
        user.setCoverType(userCoverAndAvatar.getCoverType());
        /* End resource manager. */
        List<SportDTO> sportDTOs = new ArrayList<SportDTO>();

        for (SportEntity sportEntity : targetUser.getRelatedSports()) {
            SportDTO sportDTO = new SportDTO();
            sportDTO.setId(sportEntity.getId());
            sportDTO.setName(sportEntity.getName());
            sportDTOs.add(sportDTO);
        }
        user.setSportDTOs(sportDTOs);

        if (!targetUser.getAddresses().isEmpty()) {
            user.setAddress(targetUser.getAddresses().first().getAddress());
        }

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Set<TeamMemberEntity> getTeamMembersEntityFromDto(List<UserDTO> users, TeamEntity team, SppotiEntity sppoti) {

        Set<TeamMemberEntity> teamUsers = new HashSet<>();
        Set<NotificationEntity> notificationEntities = new HashSet<>();

        Long connectedUserId = getConnectedUser().getId();

        users.forEach(userDTO -> {

            Optional<UserEntity> userEntity = Optional.ofNullable(getUserByUuId(userDTO.getId()));

            TeamMemberEntity teamMember = new TeamMemberEntity();
            SppoterEntity sppoter = new SppoterEntity();

            userEntity.ifPresent(user -> {
                if (user.getId().equals(connectedUserId)) {
                    teamMember.setAdmin(true);
                    teamMember.setTeamCaptain(true);
                    /* Admin is member of the team, status should be confirmed. */
                    teamMember.setStatus(GlobalAppStatusEnum.CONFIRMED);
                }
                teamMember.setTeam(team);
                teamMember.setUsers(user);

                if (sppoti != null) {
                    TeamMemberEntity sppoterMember = teamMembersRepository.findByUsersUuidAndTeamUuid(userDTO.getId(), team.getUuid());

                    /* if request comming from add sppoti, insert new coordinate in (team_sppoti) to define new sppoter. */
                    if (userDTO.getxPosition() != null && !userDTO.getxPosition().equals(0)) {
                        sppoter.setxPosition(userDTO.getxPosition());
                    }

                    if (userDTO.getyPosition() != null && !userDTO.getyPosition().equals(0)) {
                        sppoter.setyPosition(userDTO.getyPosition());
                    }

                    /* Admin is member of sppoti, status should be confirmed. */
                    if (teamMember.getAdmin() != null && teamMember.getAdmin()) {
                        sppoter.setStatus(GlobalAppStatusEnum.CONFIRMED);
                    }

                    /* if the sppoter already exist - default coordinate doesn't change. */
                    if (sppoterMember == null) {
                        if (userDTO.getxPosition() != null && !userDTO.getxPosition().equals(0)) {
                            teamMember.setxPosition(userDTO.getxPosition());
                        }

                        if (userDTO.getyPosition() != null && !userDTO.getyPosition().equals(0)) {
                            teamMember.setyPosition(userDTO.getyPosition());
                        }
                    }

                    /* Convert team members to sppoters. */
                    Set<SppoterEntity> sppotiMembers = new HashSet<>();
                    sppoter.setTeamMember(teamMember);
                    sppoter.setSppoti(sppoti);
                    sppotiMembers.add(sppoter);

                    teamMember.setSppotiMembers(sppotiMembers);
                    sppoti.setSppotiMembers(sppotiMembers);

                    /* send TEAM && Sppoti notification And TEAM Email to the invited user. */
                    if (!user.getId().equals(connectedUserId)) {
                        notificationEntities.add(getNotificationEntity(NotificationTypeEnum.X_INVITED_YOU_TO_JOIN_HIS_SPPOTI,
                                getUserById(connectedUserId), user, null, sppoti));
                        teamNotificationAndEmail(team, notificationEntities, connectedUserId, user);
                    }

                } else {
                    /* if request coming from add team - add members only in (users_team). */
                    if (userDTO.getxPosition() != null && !userDTO.getxPosition().equals(0)) {
                        teamMember.setxPosition(userDTO.getxPosition());
                    }

                    if (userDTO.getyPosition() != null && !userDTO.getyPosition().equals(0)) {
                        teamMember.setyPosition(userDTO.getyPosition());
                    }

                    /* send TEAM notification And TEAM Email to the invited user. */
                    if (!user.getId().equals(connectedUserId)) {
                        teamNotificationAndEmail(team, notificationEntities, connectedUserId, user);
                    }
                }

                teamUsers.add(teamMember);
            });

            userEntity.orElseThrow(() -> new TeamMemberNotFoundException("team member (" + userDTO.getId() + ") not found"));

        });

        return teamUsers;

    }

    /**
     * Send team member notification and Email.
     */
    private void teamNotificationAndEmail(TeamEntity team, Set<NotificationEntity> notificationEntities, Long connectedUserId,
                                          UserEntity userEntity) {
        this.sendTeamNotification(team, notificationEntities, connectedUserId, userEntity);
        //this.sendJoinTeamEmail(team, userEntity, adminEntity);
    }

    /**
     * @param team team to map.
     * @return a teamResponse object from TeamEntity entity.
     */
    protected TeamDTO fillTeamResponse(TeamEntity team, SppotiEntity sppoti) {

        List<UserDTO> teamUsers = new ArrayList<>();

        for (TeamMemberEntity memberEntity : team.getTeamMembers()) {
            teamUsers.add(teamMemberTransformer.modelToDto(memberEntity, sppoti));
        }
        TeamDTO teamDTO = teamTransformer.modelToDto(team);
        teamDTO.setMembers(teamUsers);

        return teamDTO;

    }

    /**
     * @param team                 team info.
     * @param notificationEntities list of notif to send.
     * @param adminId              connected user id
     * @param u                    user to notify.
     */
    private void sendTeamNotification(TeamEntity team, Set<NotificationEntity> notificationEntities, Long adminId, UserEntity u) {

        notificationEntities.add(getNotificationEntity(NotificationTypeEnum.X_INVITED_YOU_TO_JOIN_HIS_TEAM, getUserById(adminId), u, team, null));
        if (team.getNotificationEntities() != null) {
            team.getNotificationEntities().addAll(notificationEntities);
        } else {
            team.setNotificationEntities(notificationEntities);
        }

    }

    /**
     * Add notification.
     *
     * @param notificationType notif type.
     * @param userFrom         notif sender.
     * @param userTo           notif receiver.
     * @param teamEntity       team information.
     * @param sppoti           sppoti info.
     */
    @Transactional
    protected void addNotification(NotificationTypeEnum notificationType, UserEntity userFrom, UserEntity userTo, TeamEntity teamEntity, SppotiEntity sppoti) {
        NotificationEntity notification = getNotificationEntity(notificationType, userFrom, userTo, teamEntity, sppoti);

        notificationRepository.save(notification);
    }

    /**
     * Init notif entity.
     */
    private NotificationEntity getNotificationEntity(NotificationTypeEnum notificationType, UserEntity userFrom, UserEntity userTo, TeamEntity teamEntity, SppotiEntity sppotiEntity) {
        NotificationEntity notification = new NotificationEntity();
        notification.setNotificationType(notificationType);
        notification.setFrom(userFrom);
        notification.setTo(userTo);
        notification.setTeam(teamEntity);
        notification.setSppoti(sppotiEntity);
        return notification;
    }

    /**
     * Find tags in content and add notifications.
     *
     * @param commentEntity like entity.
     * @param postEntity    post entity.
     */
    @Transactional
    public void addTagNotification(PostEntity postEntity, CommentEntity commentEntity) {

        String content = null;
        if (postEntity != null) {
            content = postEntity.getContent();
        } else if (commentEntity != null) {
            content = commentEntity.getContent();
        }

        /**
         * All words starting with @, followed by Letter or accented Letter.
         * and finishing with Letter, Number or Accented letter.
         */
        String patternString1 = "(\\$+)([a-z|A-Z|\\p{javaLetter}][a-z\\d|A-Z\\d|\\p{javaLetter}]*)";

        Pattern pattern = Pattern.compile(patternString1);
        Matcher matcher = pattern.matcher(content);

        /**
         *  clean tags from @.
         */
        List<String> tags = new ArrayList<>();
        while (matcher.find()) {
            LOGGER.debug(matcher.group());
            String s = matcher.group().trim();
            s = s.replaceAll("[$]", "");
            tags.add(s);
        }

        /**
         * Process each tag.
         */
        for (String username : tags) {
            UserEntity userToNotify;

            userToNotify = userRepository.getByUsernameAndDeletedFalse(username);

            if (userToNotify != null) {
                if (commentEntity != null) {
                    addNotification(NotificationTypeEnum.X_TAGGED_YOU_IN_A_COMMENT, commentEntity.getUser(), userToNotify, null, null);
                } else if (postEntity != null) {
                    addNotification(NotificationTypeEnum.X_TAGGED_YOU_IN_A_POST, postEntity.getUser(), userToNotify, null, null);
                }

            }
        }

    }

    /**
     * Check if the user id in parameter is same as connected user id.
     *
     * @param userId user id resource.
     */
    protected void CheckConnectedUserAccessPrivileges(int userId) {
        if (getConnectedUser().getUuid() != userId) {
            throw new NotAdminException("Unauthorized access");
        }
    }


    /**
     * Send Email to the invited member to join the team.
     *
     * @param team team to add member.
     * @param to   added member.
     * @param from team admin.
     */
    protected void sendJoinTeamEmail(TeamEntity team, UserEntity to, TeamMemberEntity from) {

        UserDTO member = userTransformer.modelToDto(to);
        UserDTO admin = teamMemberTransformer.modelToDto(from, null);
        TeamDTO teamDto = teamTransformer.modelToDto(team);

        Thread thread = new Thread(() -> {
            this.teamMailer.sendJoinTeamEmail(teamDto, member, admin);
            LOGGER.info("Join team email has been sent successfully !");
        });
        thread.start();
    }
}