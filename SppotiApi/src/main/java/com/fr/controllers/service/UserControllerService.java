package com.fr.controllers.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.models.HeaderData;
import com.fr.models.Notification;
import com.fr.models.PostResponse;
import com.fr.entities.FriendShip;
import com.fr.entities.Users;

@Service
public interface UserControllerService extends AbstractControllerService {

	public Users getAllUserData();

	public Long getConnectedUserId();

	public Users getUserWithAllDataById(Long userId);

	public List<FriendShip> getPendingFriendList(Long userId, int page);

	public List<FriendShip> getConfirmedFriendList(Long userId, int page);

	public HeaderData getHeaderData(Long userId);

	public boolean editProfilePicture(String newAvatar, Long oldAvatarId, Long userId);

	boolean editCoverPicture(String newCover, Long oldCoverId, int coverExtension, Long userId);

	public Users getUserByUsername(String username);

	public HeaderData getTopHeaderData(Long userId);

	public List<PostResponse> getPostsFromLastPage(Long loggedUserId, Long targetProfileId, int page);

	public List<HeaderData> getUserFromPrefix(String userPrefix, int page);

	public List<Notification> getUnseenNotifications(Long userId, int page);

	public List<HeaderData> getFriendFromPrefix(Long userId, String friendPrefix, int page);

}
