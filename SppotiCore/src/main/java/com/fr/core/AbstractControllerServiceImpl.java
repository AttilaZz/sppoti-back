package com.fr.core;

import com.fr.commons.dto.*;
import com.fr.entities.*;
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
import org.springframework.util.StringUtils;
import utils.EntitytoDtoTransformer;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * @return current authentication username
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
     * @param loginUser
     * @return user entity from username
     */
    @Override
    public UserEntity getUserFromUsernameType(String loginUser) {
        return null;
    }

    /**
     * @param username
     * @return login type
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
     * @param id
     * @return found userEntity
     */
    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.getByIdAndDeletedFalse(id);
    }

    /**
     * @param id
     * @return found user entity
     */
    @Override
    public UserEntity getUserByUuId(int id) {

        List<UserEntity> usersList = userRepository.getByUuid(id);

        if (usersList == null && usersList.isEmpty()) {
            return null;
        }

        return usersList.get(0);

    }

    protected Properties globalAddressConfigProperties() {
        Properties properties = new Properties();
        properties.put("rootAddress", environment.getRequiredProperty("rootAddress"));

        return properties;
    }

    /**
     * @param dsHistoryList
     * @return list of ContentEditedResponseDTO
     */
    protected List<ContentEditedResponseDTO> fillEditContentResponse(List<EditHistory> dsHistoryList) {
        List<ContentEditedResponseDTO> editHistoryResponse = new ArrayList<ContentEditedResponseDTO>();
        editHistoryResponse.clear();
        for (EditHistory editContent : dsHistoryList) {

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
        return accountUserDetails.getConnectedUserDetails();
    }

    /**
     * @param dbCommentEntityList
     * @param userId
     * @return list of Comment DTO
     */
    protected List<CommentDTO> fillCommentModelList(List<CommentEntity> dbCommentEntityList, Long userId) {
        List<CommentDTO> myList = new ArrayList<CommentDTO>();

        for (CommentEntity commentEntity : dbCommentEntityList) {
            int commentId = commentEntity.getUuid();
            CommentDTO cm = new CommentDTO();

//            if (!userDaoService.getLastAvatar(userId).isEmpty())
//                cm.setAuthorAvatar(userDaoService.getLastAvatar(userId).get(0).getUrl());

            UserDTO userCoverAndAvatar = EntitytoDtoTransformer.getUserCoverAndAvatar(
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

            List<EditHistory> editHistory = editHistoryRepository.getByCommentUuidOrderByDatetimeEditedDesc(commentId);

            if (!editHistory.isEmpty()) {
                cm.setEdited(true);

                EditHistory ec = editHistory.get(0);

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
     * @param o
     * @param userId
     * @return true if content has been liked by me, false otherwise
     */
    // detect if post or comment has already been liked by user
    protected boolean isContentLikedByUser(Object o, Long userId) {

        List<LikeContent> lp = new ArrayList<LikeContent>();
        PostEntity p;
        CommentEntity c;

        if (o instanceof PostEntity) {
            p = (PostEntity) o;
            lp.addAll(p.getLikes());
        } else if (o instanceof CommentEntity) {
            c = (CommentEntity) o;
            lp.addAll(c.getLikes());
        }

        for (LikeContent likePost : lp) {
            if (likePost.getUser().getId().equals(userId)) {
                return true;
            }
        }

        return false;

    }

    /**
     * @param targetUser
     * @param connected_user
     * @return user DTO
     */
    protected UserDTO fillUserResponse(UserEntity targetUser, UserEntity connected_user) {

        UserDTO user = new UserDTO();
        user.setLastName(targetUser.getLastName());
        user.setFirstName(targetUser.getFirstName());
        user.setUsername(targetUser.getUsername());
        user.setEmail(targetUser.getEmail());
        user.setPhone(targetUser.getTelephone());
        user.setId(targetUser.getUuid());
        user.setBirthDate(targetUser.getDateBorn());
        user.setFriendStatus(GlobalAppStatus.PUBLIC_RELATION.getValue());

        if (connected_user != null) {

            if (!connected_user.getId().equals(targetUser.getId())) {
                /*
                manage requests sent to me
                 */
                FriendShip friendShip;

                friendShip = friendShipRepository.findByFriendUuidAndUserUuidAndDeletedFalse(connected_user.getUuid(), targetUser.getUuid());

                if (friendShip == null) {
                    friendShip = friendShipRepository.findByFriendUuidAndUserUuidAndDeletedFalse(targetUser.getUuid(), connected_user.getUuid());
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
                if (!friendShipRepository.findByUserUuidAndFriendUuidAndStatusAndDeletedFalse(targetUser.getUuid(), connected_user.getUuid(), GlobalAppStatus.PENDING.name()).isEmpty()) {
                    user.setFriendStatus(GlobalAppStatus.PENDING_SENT.getValue());
                }
            }

            user.setMyProfile(connected_user.getId().equals(targetUser.getId()));

        } else {
            user.setMyProfile(true);
        }

        /*
        Manage resources
         */
        UserDTO user_cover_avatar = EntitytoDtoTransformer.getUserCoverAndAvatar(targetUser);
        user.setCover(user_cover_avatar.getCover());
        user.setAvatar(user_cover_avatar.getAvatar());
        user.setCoverType(user_cover_avatar.getCoverType());
        /*
        End resource manager
         */

        List<SportModelDTO> sportModelDTOs = new ArrayList<SportModelDTO>();

        for (Sport sport : targetUser.getRelatedSports()) {
            SportModelDTO sportModelDTO = new SportModelDTO();
            sportModelDTO.setId(sport.getId());
            sportModelDTO.setName(sport.getName());

            sportModelDTOs.add(sportModelDTO);
        }

        user.setSportModelDTOs(sportModelDTOs);

        try {
            user.setAddress(targetUser.getAddresses().first().getAddress());
        } catch (Exception e) {
            LOGGER.warn("UserDTO has no address yet !");
        }

        return user;
    }

    /**
     * @param users
     * @param team
     * @param adminId
     * @param sppoti  @return array of USERS_TEAM
     */
    @Override
    public Set<TeamMembers> getTeamMembersEntityFromDto(List<UserDTO> users, Team team, Long adminId, Sppoti sppoti) {

        Set<TeamMembers> teamUsers = new HashSet<TeamMembers>();
        Set<Team> teams = new HashSet<Team>();
        teams.add(team);

        for (UserDTO user : users) {

            List<UserEntity> u = userRepository.getByUuid(user.getId());


            TeamMembers teamMember = new TeamMembers();
            SppotiMember sppoter = new SppotiMember();

            if (u != null && !u.isEmpty()) {

                if (u.get(0).getId().equals(adminId)) {
                    teamMember.setAdmin(true);
                    teamMember.setStatus(GlobalAppStatus.CONFIRMED.name());
                }

                teamMember.setTeams(team);
                teamMember.setUsers(u.get(0));

                if (sppoti != null) {
                    TeamMembers sppoterMember = teamMembersRepository.findByUsersUuidAndTeamsUuid(user.getId(), team.getUuid());

                    //if request comming from add sppoti, insert new coordinate in (team_sppoti) to define new sppoter
                    if (user.getxPosition() != null && !user.getxPosition().equals(0)) {
                        sppoter.setxPosition(user.getxPosition());
                    }

                    if (user.getyPosition() != null && !user.getyPosition().equals(0)) {
                        sppoter.setyPosition(user.getyPosition());
                    }

                    if (teamMember.getAdmin() != null && teamMember.getAdmin()) {
                        sppoter.setStatus(GlobalAppStatus.CONFIRMED.name());
                    }

                    //if the sppoter already exist - default coordinate doesn't change
                    if (sppoterMember == null) {

                        if (user.getxPosition() != null && !user.getxPosition().equals(0)) {
                            teamMember.setxPosition(user.getxPosition());
                        }

                        if (user.getyPosition() != null && !user.getyPosition().equals(0)) {
                            teamMember.setyPosition(user.getyPosition());
                        }

                    }

                    Set<SppotiMember> sppotiMembers = new HashSet<SppotiMember>();
                    sppoter.setUsersTeam(teamMember);
                    sppoter.setSppoti(sppoti);
                    sppotiMembers.add(sppoter);

                    teamMember.setSppotiMembers(sppotiMembers);
                    sppoti.setSppotiMembers(sppotiMembers);

                } else {
                    //if request comming from add team - add members only in (users_team)
                    if (user.getxPosition() != null && !user.getxPosition().equals(0)) {
                        teamMember.setxPosition(user.getxPosition());
                    }

                    if (user.getyPosition() != null && !user.getyPosition().equals(0)) {
                        teamMember.setyPosition(user.getyPosition());
                    }
                }


                teamUsers.add(teamMember);

            } else {
                throw new EntityNotFoundException();
            }

        }

        return teamUsers;

    }


    /**
     * @param team
     * @return a teamResponse object from Team entity
     */
    protected TeamResponseDTO fillTeamResponse(Team team, Long sppotiAdmin) {

        TeamResponseDTO teamResponseDTO = new TeamResponseDTO();
        teamResponseDTO.setId(team.getUuid());

        List<UserDTO> teamUsers = new ArrayList<UserDTO>();

        for (TeamMembers user : team.getTeamMemberss()) {

            Integer sppoterStatus = null;

            //get status for the selected sppoti
            if (!StringUtils.isEmpty(user.getSppotiMemberss())) {
                for (SppotiMember sppoter : user.getSppotiMemberss()) {
                    if (sppoter.getUsersTeam().getId().equals(user.getId())) {
                        sppoterStatus = GlobalAppStatus.valueOf(sppoter.getStatus()).getValue();
                    }
                }
            }

            //get avatar and cover
            UserDTO userCoverAndAvatar = EntitytoDtoTransformer.getUserCoverAndAvatar(user.getUsers());

            //fill sppoter data
            teamUsers.add(new UserDTO(user.getUuid(), user.getUsers().getFirstName(), user.getUsers().getLastName(), user.getUsers().getUsername(),
                    userCoverAndAvatar.getCover() != null ? userCoverAndAvatar.getCover() : null,
                    userCoverAndAvatar.getAvatar() != null ? userCoverAndAvatar.getAvatar() : null,
                    userCoverAndAvatar.getCoverType() != null ? userCoverAndAvatar.getCoverType() : null,
                    user.getAdmin(), sppotiAdmin != null && user.getUsers().getId().equals(sppotiAdmin) ? true : null,
                    GlobalAppStatus.valueOf(user.getStatus()).getValue(),
                    sppotiAdmin != null ? sppoterStatus : null, user.getUsers().getUuid()));
        }

        teamResponseDTO.setTeamMembers(teamUsers);

        teamResponseDTO.setCoverPath(team.getCoverPath());
        teamResponseDTO.setLogoPath(team.getLogoPath());
        teamResponseDTO.setName(team.getName());

        teamResponseDTO.setSportId(team.getSport().getId());

        return teamResponseDTO;

    }


    /**
     * Add notification
     *
     * @param friendRequestRefused
     * @param userFrom
     * @param userTo
     */
    protected void addNotification(NotificationType friendRequestRefused, UserEntity userFrom, UserEntity userTo) {
        NotificationEntity notification = new NotificationEntity();
        notification.setNotificationType(friendRequestRefused);
        notification.setFrom(userFrom);
        notification.setTo(userTo);
        notificationRepository.save(notification);
    }

    /**
     * Find tags in content and add notifications
     *
     * @param commentEntity
     * @param postEntity
     */
    public void addTagNotification(PostEntity postEntity, CommentEntity commentEntity) {

        String content = null;
        if (postEntity != null) {
            content = postEntity.getContent();
        } else if (commentEntity != null) {
            content = commentEntity.getContent();
        }

        /**
         * All words starting with @, followed by Letter or accented Letter
         * and finishing with Letter, Number or Accented letter
         */
        String patternString1 = "(\\$+)([a-z|A-Z|\\p{javaLetter}][a-z\\d|A-Z\\d|\\p{javaLetter}]*)";

        Pattern pattern = Pattern.compile(patternString1);
        Matcher matcher = pattern.matcher(content);

        /**
         *  clean tags from @
         */
        List<String> tags = new ArrayList<>();
        while (matcher.find()) {
            LOGGER.debug(matcher.group());
            String s = matcher.group().trim();
            s = s.replaceAll("[$]", "");
            tags.add(s);
        }

        /**
         * Process each tag
         */
        for (String username : tags) {
            UserEntity userToNotify;

            userToNotify = userRepository.getByUsername(username);

            if (userToNotify != null) {
                if (commentEntity != null) {
                    addNotification(NotificationType.X_TAGGED_YOU_IN_A_COMMENT, commentEntity.getUser(), userToNotify);
                } else if (postEntity != null) {
                    addNotification(NotificationType.X_TAGGED_YOU_IN_A_POST, postEntity.getUser(), userToNotify);
                }

            }
        }

    }
}