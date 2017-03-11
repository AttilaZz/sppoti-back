package com.fr.core;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.commons.dto.SportDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.team.TeamResponseDTO;
import com.fr.entities.*;
import com.fr.exceptions.TeamMemberNotFoundException;
import com.fr.mail.ApplicationMailer;
import com.fr.models.GlobalAppStatus;
import com.fr.models.NotificationType;
import com.fr.repositories.*;
import com.fr.rest.service.AbstractControllerService;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import transformers.EntityToDtoTransformer;
import transformers.TeamMemberTransformer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional(readOnly = true)
@Component("abstractService")
public abstract class AbstractControllerServiceImpl implements AbstractControllerService {

    protected Long connectedUserId;

    protected UserRepository userRepository;
    protected SportRepository sportRepository;
    protected RoleRepository roleRepository;
    protected PostRepository postRepository;
    protected EditHistoryRepository editHistoryRepository;
    protected LikeRepository likeRepository;
    protected ResourceRepository resourceRepository;
    protected CommentRepository commentRepository;
    protected FriendShipRepository friendShipRepository;
    protected SppotiRepository sppotiRepository;
    protected TeamRepository teamRepository;
    protected TeamMembersRepository teamMembersRepository;
    protected SppotiMembersRepository sppotiMembersRepository;
    protected NotificationRepository notificationRepository;


    @Autowired
    public void setSppotiMembersRepository(SppotiMembersRepository sppotiMembersRepository) {
        this.sppotiMembersRepository = sppotiMembersRepository;
    }

    @Autowired
    public void setTeamMembersRepository(TeamMembersRepository teamMembersRepository) {
        this.teamMembersRepository = teamMembersRepository;
    }

    @Autowired
    public void setTeamRepository(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Autowired
    public void setSppotiRepository(SppotiRepository sppotiRepository) {
        this.sppotiRepository = sppotiRepository;
    }

    @Autowired
    public void setFriendShipRepository(FriendShipRepository friendShipRepository) {
        this.friendShipRepository = friendShipRepository;
    }

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Autowired
    public void setResourceRepository(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Autowired
    public void setLikeRepository(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Autowired
    public void setEditHistoryRepository(EditHistoryRepository editHistoryRepository) {
        this.editHistoryRepository = editHistoryRepository;
    }

    @Autowired
    public void setNotificationRepository(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setSportRepository(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPostRepository(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Autowired
    protected ApplicationMailer mailer;

    @Autowired
    private Environment environment;

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
    public UserEntity getUserFromUsernameType(String loginUser) {
        return null;
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

        Optional<UserEntity> usersList = userRepository.getByUuid(id);

        if (usersList.isPresent()) {
            return usersList.get();
        }

        return null;

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
        AccountUserDetails accountUserDetails = (AccountUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findOne(accountUserDetails.getId());
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

            UserDTO userCoverAndAvatar = EntityToDtoTransformer.getUserCoverAndAvatar(
                    commentEntity.getUser());
            cm.setAuthorAvatar(userCoverAndAvatar.getAvatar() != null ? userCoverAndAvatar.getAvatar() : null);
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
        user.setBirthDate(targetUser.getDateBorn());
        user.setFriendStatus(GlobalAppStatus.PUBLIC_RELATION.getValue());

        if (connectedUser != null) {

            if (!connectedUser.getId().equals(targetUser.getId())) {
                /*
                manage requests sent to me
                 */
                FriendShipEntity friendShip;

                friendShip = friendShipRepository.findByFriendUuidAndUserUuidAndDeletedFalse(connectedUser.getUuid(), targetUser.getUuid());

                if (friendShip == null) {
                    friendShip = friendShipRepository.findByFriendUuidAndUserUuidAndDeletedFalse(targetUser.getUuid(), connectedUser.getUuid());
                }

                if (friendShip == null) {
                    user.setFriendStatus(GlobalAppStatus.PUBLIC_RELATION.getValue());
                } else {

                    //We are friend
                    if (friendShip.getStatus().equals(GlobalAppStatus.CONFIRMED.name())) {
                        user.setFriendStatus(GlobalAppStatus.CONFIRMED.getValue());

                        //Friend request waiting to be accepted by me
                    } else if (friendShip.getStatus().equals(GlobalAppStatus.PENDING.name())) {
                        user.setFriendStatus(GlobalAppStatus.PENDING.getValue());

                        //Friend request refused by me
                    } else if (friendShip.getStatus().equals(GlobalAppStatus.REFUSED.name())) {
                        user.setFriendStatus(GlobalAppStatus.REFUSED.getValue());

                    }

                }
                /*
                Manage request sent by me
                 */
                if (!friendShipRepository.findByUserUuidAndFriendUuidAndStatusAndDeletedFalse(targetUser.getUuid(), connectedUser.getUuid(), GlobalAppStatus.PENDING.name()).isEmpty()) {
                    user.setFriendStatus(GlobalAppStatus.PENDING_SENT.getValue());
                }
            }

            user.setMyProfile(connectedUser.getId().equals(targetUser.getId()));

        } else {
            user.setMyProfile(true);
        }

        /*
        Manage resources
         */
        UserDTO user_cover_avatar = EntityToDtoTransformer.getUserCoverAndAvatar(targetUser);
        user.setCover(user_cover_avatar.getCover());
        user.setAvatar(user_cover_avatar.getAvatar());
        user.setCoverType(user_cover_avatar.getCoverType());
        /*
        End resource manager
         */

        List<SportDTO> sportDTOs = new ArrayList<SportDTO>();

        for (SportEntity sportEntity : targetUser.getRelatedSports()) {
            SportDTO sportDTO = new SportDTO();
            sportDTO.setId(sportEntity.getId());
            sportDTO.setName(sportEntity.getName());

            sportDTOs.add(sportDTO);
        }

        user.setSportDTOs(sportDTOs);

        try {
            user.setAddress(targetUser.getAddresses().first().getAddress());
        } catch (Exception e) {
            LOGGER.warn("UserDTO has no address yet !");
        }

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Set<TeamMemberEntity> getTeamMembersEntityFromDto(List<UserDTO> users, TeamEntity team, SppotiEntity sppoti) {

        Set<TeamMemberEntity> teamUsers = new HashSet<TeamMemberEntity>();
        Set<NotificationEntity> notificationEntities = new HashSet<>();

        Long adminId = getConnectedUser().getId();

        for (UserDTO user : users) {

            UserEntity u = getUserByUuId(user.getId());

            TeamMemberEntity teamMember = new TeamMemberEntity();
            SppotiMember sppoter = new SppotiMember();

            if (u != null) {

                if (u.getId().equals(adminId)) {
                    teamMember.setAdmin(true);
                    teamMember.setTeamCaptain(true);
                    /** Admin is member of the team, status should be confirmed. */
                    teamMember.setStatus(GlobalAppStatus.CONFIRMED.name());
                }

                teamMember.setTeam(team);
                teamMember.setUsers(u);

                if (sppoti != null) {
                    TeamMemberEntity sppoterMember = teamMembersRepository.findByUsersUuidAndTeamUuid(user.getId(), team.getUuid());

                    /** if request comming from add sppoti, insert new coordinate in (team_sppoti) to define new sppoter. */
                    if (user.getxPosition() != null && !user.getxPosition().equals(0)) {
                        sppoter.setxPosition(user.getxPosition());
                    }

                    if (user.getyPosition() != null && !user.getyPosition().equals(0)) {
                        sppoter.setyPosition(user.getyPosition());
                    }

                    /** Admin is member of sppoti, status should be confirmed. */
                    if (teamMember.getAdmin() != null && teamMember.getAdmin()) {
                        sppoter.setStatus(GlobalAppStatus.CONFIRMED.name());
                    }

                    /** if the sppoter already exist - default coordinate doesn't change. */
                    if (sppoterMember == null) {
                        if (user.getxPosition() != null && !user.getxPosition().equals(0)) {
                            teamMember.setxPosition(user.getxPosition());
                        }

                        if (user.getyPosition() != null && !user.getyPosition().equals(0)) {
                            teamMember.setyPosition(user.getyPosition());
                        }
                    }

                    /** Convert team members to sppoters. */
                    Set<SppotiMember> sppotiMembers = new HashSet<>();
                    sppoter.setTeamMember(teamMember);
                    sppoter.setSppoti(sppoti);
                    sppotiMembers.add(sppoter);

                    teamMember.setSppotiMembers(sppotiMembers);
                    sppoti.setSppotiMembers(sppotiMembers);

                    /** send TEAM notification to the invited user. */
                    if (!u.getId().equals(adminId)) {
                        addNotification(NotificationType.X_INVITED_YOU_TO_JOIN_HIS_TEAM, getUserById(adminId), u, null);
                    }

                    /** send SPPOTI notification to all the team. */
                    if (!u.getId().equals(adminId)) {
                        addNotification(NotificationType.X_INVITED_YOU_TO_JOIN_HIS_SPPOTI, getUserById(adminId), u, null);
                    }

                } else {
                    /** if request coming from add team - add members only in (users_team). */
                    if (user.getxPosition() != null && !user.getxPosition().equals(0)) {
                        teamMember.setxPosition(user.getxPosition());
                    }

                    if (user.getyPosition() != null && !user.getyPosition().equals(0)) {
                        teamMember.setyPosition(user.getyPosition());
                    }

                    /** send TEAM notification to the invited user. */
                    if (!u.getId().equals(adminId)) {
                        notificationEntities.add(getNotificationEntity(NotificationType.X_INVITED_YOU_TO_JOIN_HIS_TEAM, getUserById(adminId), u, team));
                        if(team.getNotificationEntities() != null) {
                            team.getNotificationEntities().addAll(notificationEntities);
                        }else{
                            team.setNotificationEntities(notificationEntities);
                        }
                    }
                }


                teamUsers.add(teamMember);

            } else {
                throw new TeamMemberNotFoundException("team member (" + user.getId() + ") not found");
            }

        }

        return teamUsers;

    }


    /**
     * @param team team to map.
     * @return a teamResponse object from TeamEntity entity.
     */
    protected TeamResponseDTO fillTeamResponse(TeamEntity team, SppotiEntity sppoti) {

        TeamResponseDTO teamResponseDTO = new TeamResponseDTO();
        teamResponseDTO.setId(team.getUuid());

        List<UserDTO> teamUsers = new ArrayList<UserDTO>();

        for (TeamMemberEntity memberEntity : team.getTeamMembers()) {

            Integer sppoterStatus = null;

            if (sppoti != null) {
                //get status for the selected sppoti
                if (!StringUtils.isEmpty(memberEntity.getSppotiMembers())) {
                    for (SppotiMember sppoter : memberEntity.getSppotiMembers()) {
                        if (sppoter.getTeamMember().getId().equals(memberEntity.getId()) && sppoter.getSppoti().getId().equals(sppoti.getId())) {
                            sppoterStatus = GlobalAppStatus.valueOf(sppoter.getStatus()).getValue();
                        }
                    }
                }
            }

            teamUsers.add(TeamMemberTransformer.teamMemberEntityToDto(memberEntity, sppoti, sppoterStatus));
        }

        teamResponseDTO.setTeamMembers(teamUsers);
        teamResponseDTO.setCoverPath(team.getCoverPath());
        teamResponseDTO.setLogoPath(team.getLogoPath());
        teamResponseDTO.setName(team.getName());
        teamResponseDTO.setSportId(team.getSport().getId());

        return teamResponseDTO;

    }


    /**
     * Add notification.
     *
     * @param notificationType notif type.
     * @param userFrom         notif sender.
     * @param userTo           notif receiver.
     * @param teamEntity       team information.
     */
    @Transactional
    protected void addNotification(NotificationType notificationType, UserEntity userFrom, UserEntity userTo, TeamEntity teamEntity) {
        NotificationEntity notification = getNotificationEntity(notificationType, userFrom, userTo, teamEntity);

        notificationRepository.save(notification);
    }

    private NotificationEntity getNotificationEntity(NotificationType notificationType, UserEntity userFrom, UserEntity userTo, TeamEntity teamEntity) {
        NotificationEntity notification = new NotificationEntity();
        notification.setNotificationType(notificationType);
        notification.setFrom(userFrom);
        notification.setTo(userTo);
        notification.setTeam(teamEntity);
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

            userToNotify = userRepository.getByUsername(username);

            if (userToNotify != null) {
                if (commentEntity != null) {
                    addNotification(NotificationType.X_TAGGED_YOU_IN_A_COMMENT, commentEntity.getUser(), userToNotify, null);
                } else if (postEntity != null) {
                    addNotification(NotificationType.X_TAGGED_YOU_IN_A_POST, postEntity.getUser(), userToNotify, null);
                }

            }
        }

    }
}