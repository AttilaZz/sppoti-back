package com.fr.controllers.serviceImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fr.controllers.service.UserControllerService;
import com.fr.enums.RessourceType;
import com.fr.models.HeaderData;
import com.fr.models.Notification;
import com.fr.models.PostResponse;
import com.fr.pojos.Address;
import com.fr.pojos.Comment;
import com.fr.pojos.EditHistory;
import com.fr.pojos.FriendShip;
import com.fr.pojos.Notifications;
import com.fr.pojos.Post;
import com.fr.pojos.Resources;
import com.fr.pojos.Sppoti;
import com.fr.pojos.Users;

@Component("userControllerService")
public class UserControllerServiceImpl extends AbstractControllerServiceImpl implements UserControllerService {

	@Override
	public Users getAllUserData() {

		String login = getAuthenticationUsername();
		String regex = "[0-9]+";

		Users userToFind = new Users();
		Long userIdToReturn;

		if (login.contains("@")) {// login with email
			userToFind.setEmail(login);
			userIdToReturn = userDaoService.getSimilarEntity(userToFind).get(0).getId();
		} else if (login.matches(regex)) {// login with phone
			userToFind.setTelephone(login);
			userIdToReturn = userDaoService.getSimilarEntity(userToFind).get(0).getId();
		} else {
			userToFind.setUsername(login);
			userIdToReturn = userDaoService.getSimilarEntity(userToFind).get(0).getId();
		}

		return userDaoService.getUserWithAllDataById(userIdToReturn);
	}

	@Override
	public Long getConnectedUserId() {
		return connectedUserId;
	}

	@Override
	public Users getUserWithAllDataById(Long userId) {
		return userDaoService.getUserWithAllDataById(userId);
	}

	@Override
	public List<PostResponse> getPostsFromLastPage(Long logedId, Long targetID, int bottomMajId) {

		List<Post> encodedContent = postDaoService.getPostsFromLastPage(targetID, bottomMajId);

		List<PostResponse> decodedPost = new ArrayList<PostResponse>();

		for (Post post : encodedContent) {
			// decode 64

			// String content = new
			// String(Base64Utils.decode(post.getContent()));
			// try {
			// content = new String(Base64Utils.decode(post.getContent()));
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

			PostResponse pres = new PostResponse();

			Long id = post.getId();
			if (id != null)
				pres.setId(id);

			String[] album = post.getAlbum();
			if (album != null)
				pres.setImageLink(album);

			String video = post.getVideoLink();
			if (video != null)
				pres.setVideoLink(video);

			Sppoti g = post.getGame();
			if (g != null)
				pres.setGame(g);

			Long userPostId = post.getUser().getId();
			if (userPostId == targetID) {
				pres.setMyPost(logedId == userPostId);
			}

			String lName = post.getUser().getLastName();
			if (lName != null) {
				pres.setLastName(lName);
			}

			String fName = post.getUser().getFirstName();
			if (fName != null) {
				pres.setFirstName(fName);
			}

			String uName = post.getUser().getUsername();
			if (uName != null) {
				pres.setUsername(uName);
			}

			// add last comment
			List<Comment> commentsList = new ArrayList<>();
			if (!post.getComments().isEmpty()) {
				commentsList.clear();
				List<Comment> dbCom = new ArrayList<>();
				dbCom = commentDaoService.getLastPostComment(post.getId());
				if (dbCom.size() > 0) {
					Comment c = dbCom.get(0);
					commentsList.add(c);
				}

				pres.setPostComments(fillCommentModelList(commentsList, targetID));
			}

			// check if content has been modified or not
			List<EditHistory> editHistory = editContentDaoService.getLastEditedPost(post.getId());

			if (!editHistory.isEmpty()) {
				// modification detected
				pres.setEdited(true);

				EditHistory ec = editHistory.get(0);

				pres.setDatetimeCreated(ec.getDatetimeEdited());
				if (ec.getText() != null) {
					pres.setContent(ec.getText());
				}

				if (ec.getSport() != null) {
					Long spId = ec.getSport().getId();
					pres.setSportId(spId);
				}
			} else {
				// post has not been edited - set initial params

				if (post.getContent() != null) {
					pres.setContent(post.getContent());
				}

				if (post.getSport() != null && post.getSport().getId() != null) {
					pres.setSportId(post.getSport().getId());
				}

				pres.setDatetimeCreated(post.getDatetimeCreated());
			}

			// get Last post address
			List<Address> postAddress = addressDaoService.getLastPostAddress(post.getId());
			if (!postAddress.isEmpty()) {

				pres.setAddress(postAddress.get(0));// get last inserted address

			}

			int nbLike = post.getLikes().size();
			if (post.getLikes() != null) {
				pres.setLikeCount(nbLike);
			}

			boolean isPostLikedByMe = isContentLikedByUser(post, logedId);
			pres.setLikedByUser(isPostLikedByMe);

			pres.setCommentsCount(commentDaoService.getCommentCount(post.getId()));
			decodedPost.add(pres);
		}

		return decodedPost;

	}

	@Override
	public List<FriendShip> getPendingFriendList(Long userId, int page) {
		return friendDaoService.getPendingFriendList(userId, page);
	}

	@Override
	public List<FriendShip> getConfirmedFriendList(Long userId, int page) {
		return friendDaoService.getConfirmedFriendList(userId, page);

	}

	@Override
	public HeaderData getTopHeaderData(Long userId) {
		List<?> headerInfo = userDaoService.getHeaderData(userId);
		Iterator<?> it = headerInfo.iterator();

		HeaderData header = new HeaderData();
		while (it.hasNext()) {
			Object ob[] = (Object[]) it.next();

			header.setFirstName((String) ob[0]);
			header.setLastName((String) ob[1]);
			header.setUsername((String) ob[2]);
		}

		return header;

	}

	@Override
	public HeaderData getHeaderData(Long userId) {
		List<?> headerInfo = userDaoService.getHeaderData(userId);

		Iterator<?> it = headerInfo.iterator();

		HeaderData header = new HeaderData();
		while (it.hasNext()) {
			Object ob[] = (Object[]) it.next();

			header.setFirstName((String) ob[0]);
			header.setLastName((String) ob[1]);
			header.setUsername((String) ob[2]);
		}

		List<Resources> lAvatar = userDaoService.getLastAvatar(userId);
		List<Resources> lCover = userDaoService.getLastCover(userId);

		if (!lAvatar.isEmpty()) {
			header.setAvatar(lAvatar.get(0).getUrl());
			header.setAvatarId(lAvatar.get(0).getId());
		}

		if (!lCover.isEmpty()) {
			header.setCover(lCover.get(0).getUrl());
			header.setCoverType(lCover.get(0).getType());
			header.setCoverId(lCover.get(0).getId());
		}

		return header;
	}

	@Override
	public boolean editProfilePicture(String newAvatar, Long oldAvatar, Long userId) {
		return editResource(newAvatar, oldAvatar, 0, userId, 1);
	}

	@Override
	public boolean editCoverPicture(String newCover, Long oldCoverId, int coverExtension, Long userId) {
		return editResource(newCover, oldCoverId, coverExtension, userId, 2);
	}

	private boolean editResource(String url, Long oldId, int extension, Long userId, int op) {
		Resources r = new Resources();
		r.setUrl(url);
		r.setSelected(true);
		r.setUserRessources(userDaoService.getEntityByID(userId));
		switch (op) {
		case 1: // avatar
			r.setType(RessourceType.AVATAR.getResourceType());
			break;
		case 2: // cover
			r.setType(RessourceType.COVER.getResourceType());
			r.setTypeExtension(extension);
			break;
		default:
			break;
		}

		if (resourceDaoService.saveOrUpdate(r) && unSelectPreviousAvatarPicture(oldId)) {
			return true;
		}

		return false;
	}

	private boolean unSelectPreviousAvatarPicture(Long oldResource) {

		Resources editR = resourceDaoService.getEntityByID(oldResource);
		editR.setSelected(false);

		if (resourceDaoService.update(editR)) {
			return true;
		}

		return false;
	}

	@Override
	public Users getUserByUsername(String username) {
		return userDaoService.getUserFromloginUsername(username, 1);

	}

	@Override
	public List<HeaderData> getFriendFromPrefix(Long userId, String friendPrefix, int page) {
		List<FriendShip> foundFriends = friendDaoService.getFriendsFromPrefix(userId, friendPrefix, page);

		List<HeaderData> returnList = new ArrayList<>();

		for (FriendShip fship : foundFriends) {

			HeaderData u = new HeaderData();
			u.setUsername(fship.getFriend().getUsername());
			u.setFirstName(fship.getFriend().getFirstName());
			u.setLastName(fship.getFriend().getLastName());

			List<Resources> lAvatar = userDaoService.getLastAvatar(fship.getId());
			List<Resources> lCover = userDaoService.getLastCover(fship.getId());

			if (!lAvatar.isEmpty()) {
				u.setAvatar(lAvatar.get(0).getUrl());
				u.setAvatarId(lAvatar.get(0).getId());
			}

			if (!lCover.isEmpty()) {
				u.setCover(lCover.get(0).getUrl());
				u.setCoverType(lCover.get(0).getType());
				u.setCoverId(lCover.get(0).getId());
			}

			returnList.add(u);

		}

		return returnList;
	}

	@Override
	public List<HeaderData> getUserFromPrefix(String userPrefix, int page) {
		List<Users> foundUsers = userDaoService.getUsersFromPrefix(userPrefix, page);

		List<HeaderData> returnList = new ArrayList<>();

		for (Users users : foundUsers) {

			HeaderData u = new HeaderData();
			u.setUsername(users.getUsername());
			u.setFirstName(users.getFirstName());
			u.setLastName(users.getLastName());

			List<Resources> lAvatar = userDaoService.getLastAvatar(users.getId());
			List<Resources> lCover = userDaoService.getLastCover(users.getId());

			if (!lAvatar.isEmpty()) {
				u.setAvatar(lAvatar.get(0).getUrl());
				u.setAvatarId(lAvatar.get(0).getId());
			}

			if (!lCover.isEmpty()) {
				u.setCover(lCover.get(0).getUrl());
				u.setCoverType(lCover.get(0).getType());
				u.setCoverId(lCover.get(0).getId());
			}

			returnList.add(u);

		}

		return returnList;

	}

	@Override
	public List<Notification> getUnseenNotifications(Long userId, int page) {

		List<Notifications> dbListNotif = notificationDaoService.getUnseenNotifications(userId, page);
		List<Notification> listNotifToSend = new ArrayList<>();

		for (Notifications notifications : dbListNotif) {

			Notification n = new Notification();
			n.setDateTime(notifications.getDatetimeCreated());

			n.setNotifFromFirstName(notifications.getWhoSentNotification().getFirstName());
			n.setNotifFromLastName(notifications.getWhoSentNotification().getLastName());

			n.setTag(notifications.isTag());
			n.setShare(notifications.isContentShared());

			listNotifToSend.add(n);
		}

		return listNotifToSend;
	}

}
