package com.fr.core;

import com.fr.commons.dto.CommentModel;
import com.fr.commons.dto.ContentEditedResponse;
import com.fr.commons.dto.SportModel;
import com.fr.commons.dto.User;
import com.fr.controllers.AccountController;
import com.fr.controllers.service.AbstractControllerService;
import com.fr.entities.*;
import com.fr.mail.ApplicationMailer;
import com.fr.dto.*;
import com.fr.repositories.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
        String userName = null;
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
                FriendShip friendShip = friendShipRepository.getByFriendUuidAndUser(targetUser.getUuid(), connected_user.getUuid());

                if (friendShip == null) {
                    user.setFriendStatus(FriendStatus.PUBLIC_RELATION.getValue());
                } else {

                    //We are friend
                    if (friendShip.getStatus().equals(FriendStatus.CONFIRMED.name())) {
                        user.setFriendStatus(FriendStatus.CONFIRMED.getValue());

                        //Friend request waiting to be accepted by me
                    } else if (friendShip.getStatus().equals(FriendStatus.PENDING.name())) {
                        user.setFriendStatus(FriendStatus.PENDING.getValue());

                        //Friend request refused by me
                    } else if (friendShip.getStatus().equals(FriendStatus.REFUSED.name())) {
                        user.setFriendStatus(FriendStatus.REFUSED.getValue());

                    }

                }
                /*
                Manage request sent by me
                 */
                if (!friendShipRepository.getByUserAndFriendUuidAndStatus(targetUser.getUuid(), connected_user.getUuid(), FriendStatus.PENDING.name()).isEmpty()) {
                    user.setFriendStatus(FriendStatus.PENDING_SENT.getValue());
                }
            }

            user.setMyProfile(connected_user.getId().equals(targetUser.getId()));

        }

        /*
        Manage resources
         */
        Set<Resources> resources = targetUser.getRessources();

        List<Resources> resources_temp = new ArrayList<Resources>();
        resources_temp.addAll(resources);

        if (!resources_temp.isEmpty()) {
            if (resources_temp.size() == 2) {
                //cover and avatar found
                Resources resource1 = resources_temp.get(0);
                Resources resource2 = resources_temp.get(1);

                if (resource1.getType() == 1 && resource2.getType() == 2) {//acatar
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

}