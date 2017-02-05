package com.fr.core;

import com.fr.commons.dto.*;
import com.fr.rest.controllers.AccountController;
import com.fr.rest.service.AbstractControllerService;
import com.fr.entities.*;
import com.fr.mail.ApplicationMailer;
import com.fr.models.*;
import com.fr.repositories.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Component("abstractService")
public abstract class AbstractControllerServiceImpl implements AbstractControllerService {

    protected Long connectedUserId;

    protected UserRepository userRepository;
    protected SportRepository sportRepository;
    protected RoleRepository roleRepository;
    protected PostRepository postRepository;
    protected NotificationRepository notificationRepository;
    protected EditHistoryRepository editHistoryRepository;
    protected LikeRepository likeRepository;
    protected ResourceRepository resourceRepository;
    protected CommentRepository commentRepository;
    protected FriendShipRepository friendShipRepository;
    protected SppotiRepository sppotiRepository;
    protected TeamRepository teamRepository;
    protected TeamMembersRepository teamMembersRepository;

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

    protected Logger LOGGER = Logger.getLogger(AccountController.class);

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

    @Override
    public Users getUserFromUsernameType(String loginUser) {
        return null;
    }

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

    @Override
    public Users getUserById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public Users getUserByUuId(int id) {
        return userRepository.getByUuid(id);
    }

    protected Properties globalAddressConfigProperties() {
        Properties properties = new Properties();
        properties.put("rootAddress", environment.getRequiredProperty("rootAddress"));

        return properties;
    }

    protected List<ContentEditedResponse> fillEditContentResponse(List<EditHistory> dsHistoryList) {
        List<ContentEditedResponse> editHistoryResponse = new ArrayList<ContentEditedResponse>();
        editHistoryResponse.clear();
        for (EditHistory editContent : dsHistoryList) {

            ContentEditedResponse cer = new ContentEditedResponse();
            cer.setDateTime(editContent.getDatetimeEdited());
            cer.setId(editContent.getId());
            cer.setText(editContent.getText());

            editHistoryResponse.add(cer);
        }

        return editHistoryResponse;
    }

    protected List<CommentModel> fillCommentModelList(List<Comment> dbCommentList, Long userId) {
        List<CommentModel> myList = new ArrayList<CommentModel>();

        for (Comment comment : dbCommentList) {
            int commentId = comment.getUuid();
            CommentModel cm = new CommentModel();

//            if (!userDaoService.getLastAvatar(userId).isEmpty())
//                cm.setAuthorAvatar(userDaoService.getLastAvatar(userId).get(0).getUrl());

            cm.setAuthorFirstName(comment.getUser().getFirstName());
            cm.setAuthorLastName(comment.getUser().getLastName());
            cm.setCreationDate(comment.getDatetimeCreated());
            cm.setId(commentId);
            cm.setImageLink(comment.getImageLink());
            cm.setMyComment(comment.getUser().getId().equals(userId));

            boolean isCommentLikedByMe = isContentLikedByUser(comment, userId);
            cm.setLikedByUser(isCommentLikedByMe);
            cm.setLikeCount(comment.getLikes().size());

            List<EditHistory> editHistory = editHistoryRepository.getByCommentUuidOrderByDatetimeEditedDesc(commentId);
            if (!editHistory.isEmpty()) {
                cm.setEdited(true);

                EditHistory ec = editHistory.get(0);

                cm.setCreationDate(ec.getDatetimeEdited());
                cm.setText(ec.getText());
            } else {
                cm.setText(comment.getContent());
                cm.setCreationDate(comment.getDatetimeCreated());
            }

            cm.setLikeCount(comment.getLikes().size());

            myList.add(cm);
        }

        return myList;

    }

    // detect if post or comment has already been liked by user
    protected boolean isContentLikedByUser(Object o, Long userId) {

        List<LikeContent> lp = new ArrayList<LikeContent>();
        Post p;
        Comment c;

        if (o instanceof Post) {
            p = (Post) o;
            lp.addAll(p.getLikes());
        } else if (o instanceof Comment) {
            c = (Comment) o;
            lp.addAll(c.getLikes());
        }

        for (LikeContent likePost : lp) {
            if (likePost.getUser().getId().equals(userId)) {
                return true;
            }
        }

        return false;

    }

    protected User fillUserResponse(Users targetUser, Users connected_user) {

        User user = new User();
        user.setLastName(targetUser.getLastName());
        user.setFirstName(targetUser.getFirstName());
        user.setUsername(targetUser.getUsername());
        user.setEmail(targetUser.getEmail());
        user.setPhone(targetUser.getTelephone());
        user.setId(targetUser.getUuid());

        if (connected_user != null) {

            if (!connected_user.getId().equals(targetUser.getId())) {
                /*
                manage requests sent to me
                 */
                FriendShip friendShip;

                friendShip = friendShipRepository.findByFriendUuidAndUserAndDeletedFalse(connected_user.getUuid(), targetUser.getUuid());

                if (friendShip == null) {
                    friendShip = friendShipRepository.findByFriendUuidAndUserAndDeletedFalse(targetUser.getUuid(), connected_user.getUuid());
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
                if (!friendShipRepository.findByUserAndFriendUuidAndStatusAndDeletedFalse(targetUser.getUuid(), connected_user.getUuid(), GlobalAppStatus.PENDING.name()).isEmpty()) {
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
        User user_cover_avatar = getUserCoverAndAvatar(targetUser);
        user.setCover(user_cover_avatar.getCover());
        user.setAvatar(user_cover_avatar.getAvatar());
        user.setCoverType(user_cover_avatar.getCoverType());
        /*
        End resource manager
         */

        List<SportModel> sportModels = new ArrayList<SportModel>();

        for (Sport sport : targetUser.getRelatedSports()) {
            SportModel sportModel = new SportModel();
            sportModel.setId(sport.getId());
            sportModel.setName(sport.getName());

            sportModels.add(sportModel);
        }

        user.setSportModels(sportModels);

        try {
            user.setAddress(targetUser.getAddresses().first().getAddress());
        } catch (Exception e) {
            LOGGER.warn("User has no address yet !");
        }

        return user;
    }

    @Override
    public User getUserCoverAndAvatar(Users targetUser) {

        User user = new User();
        Set<Resources> resources = targetUser.getRessources();

        List<Resources> resources_temp = new ArrayList<Resources>();
        resources_temp.addAll(resources);

        if (!resources_temp.isEmpty()) {
            if (resources_temp.size() == 2) {
                //cover and avatar found
                Resources resource1 = resources_temp.get(0);
                Resources resource2 = resources_temp.get(1);

                if (resource1.getType() == 1 && resource2.getType() == 2) {
                    user.setAvatar(resource1.getUrl());

                    user.setCover(resource2.getUrl());
                    user.setCoverType(resource2.getTypeExtension());
                } else if (resource1.getType() == 2 && resource2.getType() == 1) {
                    user.setAvatar(resource2.getUrl());

                    user.setCover(resource1.getUrl());
                    user.setCoverType(resource1.getTypeExtension());
                }

            } else {
                // size is = 1 -> cover or avatar
                Resources resource = resources_temp.get(0);
                if (resource.getType() == 1) {//acatar
                    user.setAvatar(resource.getUrl());
                } else {
                    user.setCover(resource.getUrl());
                    user.setCoverType(resource.getTypeExtension());
                }
            }
        }

        return user;
    }

    /**
     * @param users
     * @param team
     * @param sppoti
     * @return array of USERS_TEAM
     */
    @Override
    public Set<TeamMembers> getTeamMembersEntityFromDto(List<User> users, Team team, Sppoti sppoti) {

        Set<TeamMembers> teamUsers = new HashSet<TeamMembers>();
        Set<Team> teams = new HashSet<Team>();
        teams.add(team);

        for (User user : users) {

            Users u = userRepository.getByUuid(user.getId());
            TeamMembers teamMember = new TeamMembers();
            SppotiMembers sppoter = new SppotiMembers();

            if (u != null) {

                teamMember.setTeams(team);
                teamMember.setUsers(u);

                if (sppoti != null) {
                    TeamMembers sppoterMember = teamMembersRepository.findByUsersUuidAndTeamsUuid(user.getId(), team.getUuid());

                    //if request comming from add sppoti, insert new coordinate in (team_sppoti) to define new sppoter
                    if (user.getxPosition() != null && !user.getxPosition().equals(0)) {
                        sppoter.setxPosition(user.getxPosition());
                    }

                    if (user.getyPosition() != null && !user.getyPosition().equals(0)) {
                        sppoter.setyPosition(user.getyPosition());
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

                    Set<SppotiMembers> sppotiMembers = new HashSet<SppotiMembers>();
                    sppoter.setUsersTeam(teamMember);
                    sppoter.setSppotis(sppoti);
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
}