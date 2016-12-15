package com.fr.controllers.service;

import com.fr.entities.Users;
import com.fr.models.HeaderData;
import com.fr.models.Notification;
import com.fr.models.PostResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserControllerService extends AbstractControllerService {

    Users getAllUserData();

    Long getConnectedUserId();

    Users getUserWithAllDataById(Long userId);

    HeaderData getHeaderData(Long userId);

    boolean editProfilePicture(String newAvatar, Long oldAvatarId, Long userId);

    boolean editCoverPicture(String newCover, Long oldCoverId, int coverExtension, Long userId);

    Users getUserByUsername(String username);

    HeaderData getTopHeaderData(Long userId);

    List<PostResponse> getPostsFromLastPage(Long loggedUserId, Long targetProfileId, int page);

    List<HeaderData> getUserFromPrefix(String userPrefix, int page);

    List<Notification> getUnseenNotifications(Long userId, int page);

}
