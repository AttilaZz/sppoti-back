/**
 * 
 */
package com.fr.controllers.serviceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fr.controllers.service.PostControllerService;
import com.fr.models.ContentEditedResponse;
import com.fr.models.HeaderData;
import com.fr.models.PostResponse;
import com.fr.pojos.Address;
import com.fr.pojos.Comment;
import com.fr.pojos.EditHistory;
import com.fr.pojos.LikeContent;
import com.fr.pojos.Notifications;
import com.fr.pojos.Post;
import com.fr.pojos.Sport;
import com.fr.pojos.Sppoti;
import com.fr.pojos.Users;

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

@Component
public class PostControllerServiceImpl extends AbstractControllerServiceImpl implements PostControllerService {

	@Override
	public Serializable savePost(Post post) {
		return postDaoService.save(post);

	}

	private static Logger LOGGER = Logger.getLogger(PostControllerServiceImpl.class);

	/*
	 * The post update method is used to update the location (address) of the
	 * post and the content. This two jobs are separated, not at the same time.
	 * 
	 * That's why we check if the address is not null before persisting the new
	 * content
	 */
	@Override
	public boolean updatePost(EditHistory postEditRow, Address postEditAddress) {
		if (postEditAddress != null) {
			return addressDaoService.saveOrUpdate(postEditAddress);
		}

		return editContentDaoService.saveOrUpdate(postEditRow);

	}

	@Override
	public boolean deletePost(Post p) {
		return postDaoService.delete(p);
	}

	@Override
	public Post findPost(Long id) {

		return postDaoService.getEntityByID(id);
	}

	@Override
	public Sport getSportToUse(Long id) {

		return sportDaoService.getEntityByID(id);
	}

	@Override
	public Sppoti getGameById(Long id) {

		return sppotiDaoService.getEntityByID(id);
	}

	@Override
	public boolean likePost(LikeContent likeToSave) {
		return likeDaoService.saveOrUpdate(likeToSave);
	}

	@Override
	public boolean unLikePost(Long posttId, Long userId) {
		return likeDaoService.unLikePost(posttId, userId);
	}

	@Override
	public boolean isPostAlreadyLikedByUser(Long postId, Long userId) {
		return likeDaoService.isPostAlreadyLiked(postId, userId);
	}

	@Override
	public List<PostResponse> getPhotoGallery(Long userId, int buttomMarker) {
		return fillPostResponseFromDbPost(buttomMarker, userId, 1, null);

	}

	@Override
	public List<PostResponse> getVideoGallery(Long userId, int buttomMarker) {
		return fillPostResponseFromDbPost(buttomMarker, userId, 2, null);
	}

	private List<PostResponse> fillPostResponseFromDbPost(int bottomMajId, Long userId, int operationType,
			Long postId) {
		List<Post> dbContent = null;

		switch (operationType) {
		case 1:
			dbContent = postDaoService.getPhotoGalleryPostsFromLastMajId(userId, bottomMajId);

			break;
		case 2:
			dbContent = postDaoService.getVideoGalleryPostsFromLastMajId(userId, bottomMajId);

			break;
		case 3:
			Post p = new Post();
			p.setId(postId);
			dbContent = new ArrayList<Post>();
			dbContent.add(postDaoService.getEntityByID(postId));

			break;
		default:
			break;
		}

		List<PostResponse> mContentResponse = new ArrayList<PostResponse>();

		for (Post post : dbContent) {

			PostResponse pres = new PostResponse();

			if (post.getId() != null)
				pres.setId(post.getId());

			if (post.getContent() != null)
				pres.setContent(post.getContent());

			if (post.getDatetimeCreated() != null)
				pres.setDatetimeCreated(post.getDatetimeCreated());

			if (post.getAlbum() != null)
				pres.setImageLink(post.getAlbum());

			if (post.getVideoLink() != null)
				pres.setVideoLink(post.getVideoLink());

			if (post.getGame() != null)
				pres.setGame(post.getGame());

			if (post.getSport() != null && post.getSport().getId() != null) {
				pres.setSportId(post.getSport().getId());
			}

			// Access here if details message is requested - otherwise no need
			// to show comments
			if (operationType == 3) {
				List<Comment> commentsList = new ArrayList<>();
				if (post.getComments() != null) {
					commentsList.clear();
					commentsList = commentDaoService.getCommentsFromLastMajId(postId, 0);
					pres.setPostComments(fillCommentModelList(commentsList, userId));
				}
			}

			int nbLike = post.getLikes().size();
			if (post.getLikes() != null) {
				pres.setLikeCount(nbLike);
			}

			boolean isPostLikedByMe = isContentLikedByUser(post, userId);
			pres.setLikedByUser(isPostLikedByMe);

			pres.setCommentsCount(commentDaoService.getCommentCount(post.getId()));
			mContentResponse.add(pres);
		}

		return mContentResponse;

	}

	@Override
	public PostResponse fillPostToSend(Long postId) {

		return fillPostResponseFromDbPost(0, null, 3, postId).get(0);

	}

	@Override
	public List<ContentEditedResponse> getAllPostHistory(Long id, int page) {
		List<EditHistory> dsHistoryList = editContentDaoService.getAllPostHistory(id, page);
		return fillEditContentResponse(dsHistoryList);
	}

	@Override
	public List<HeaderData> getLikersList(Long id, int page) {
		List<LikeContent> likersData = likeDaoService.getPostLikers(id, page);

		List<HeaderData> likers = new ArrayList<>();

		if (!likersData.isEmpty()) {
			for (LikeContent row : likersData) {
				// get liker data
				HeaderData u = new HeaderData();
				u.setAvatar(userDaoService.getLastAvatar(row.getUser().getId()).get(0).getUrl());
				u.setFirstName(row.getUser().getFirstName());
				u.setLastName(row.getUser().getLastName());
				// u.setCover(userDao.getLastCover(row.getUser().getId(),
				// coverType));
				u.setUsername(row.getUser().getUsername());

				likers.add(u);
			}
		}

		return likers;
	}

	@Override
	public List<EditHistory> getLastModification(Long postId) {
		return editContentDaoService.getLastEditedPost(postId);
	}

	@Override
	public Sport getSportById(Long sport_id) {
		return sportDaoService.getEntityByID(sport_id);
	}

	@Override
	public boolean editPostVisibility(Long id, int visibility) {

		Post p = postDaoService.getEntityByID(id);
		p.setVisibility(visibility);

		return postDaoService.update(p);
	}

	@Override
	public boolean addNotification(Long userId, Long postId, String content) {

		Users connectedUser = userDaoService.getEntityByID(userId);
		Post concernedePostTag = postDaoService.getEntityByID(postId);
		/**
		 * All words starting with DOLLAR, followed by Letter or accented Letter
		 * and finishing with Letter, Number or Accented letter
		 */
		String patternString1 = "(\\@+)([a-z|A-Z|\\p{javaLetter}][a-z\\d|A-Z\\d|\\p{javaLetter}]*)";

		Pattern pattern = Pattern.compile(patternString1);
		Matcher matcher = pattern.matcher(content);

		// clean tags from dollar
		List<String> tags = new ArrayList<>();
		while (matcher.find()) {
			System.out.println(matcher.group());
			String s = matcher.group().trim();
			s = s.replaceAll("[@]", "");
			tags.add(s);
		}

		/*
		 * process each tag
		 */

		for (String username : tags) {
			Users userToNotify;
			try {
				userToNotify = userDaoService.getUserFromloginUsername(username, 1);
			} catch (Exception e) {
				LOGGER.info("POST-ADD: Username tag" + username + " is not valid !");
				return false;
			}
			if (userToNotify != null) {

				Notifications notif = new Notifications();
				notif.setTag(true);
				notif.setContentShared(false);
				notif.setViewed(false);
				notif.setWhoSentNotification(connectedUser);
				notif.setNotifiedUserId(userToNotify.getId());
				notif.setPostTag(concernedePostTag);

				try {
					notificationDaoService.save(notif);
				} catch (Exception e) {
					return false;
				}

				LOGGER.info(notif.toString());
			} else
				continue;
		}

		return true;
	}

}
