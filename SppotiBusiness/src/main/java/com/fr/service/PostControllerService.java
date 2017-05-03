package com.fr.service;

import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.entities.*;

import java.util.List;
import java.util.SortedSet;


/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

public interface PostControllerService extends AbstractControllerService
{
	
	/**
	 * @param post
	 * 		post content to save.
	 *
	 * @return saved post.
	 */
	PostEntity savePost(PostEntity post);

    /*
	 * The post update method is used to update the location (address) of the
     * post and the content. This two jobs are separated, not at the same time.
     *
     * That's why we check if the address is not null before persisting the new
     * content
     */
	
	/**
	 * @param postEditRow
	 * 		last edited post.
	 * @param postEditAddress
	 * 		post address.
	 * @param postId
	 * 		post id.
	 *
	 * @return true if post has been edited, false otherwise.
	 */
	boolean updatePost(EditHistoryEntity postEditRow, SortedSet<AddressEntity> postEditAddress, int postId);
	
	/**
	 * @param postId
	 * 		post id.
	 */
	void deletePost(int postId);
	
	/**
	 * @param id
	 * 		post id.
	 *
	 * @return post.
	 */
	PostEntity findPost(int id);
	
	/**
	 * @param id
	 * 		sport id.
	 *
	 * @return sport.
	 */
	SportEntity getSportToUse(Long id);
	
	/**
	 * @param id
	 * 		sppoti id.
	 *
	 * @return SppotiEntity.
	 */
	SppotiEntity getSppotiById(Long id);
	
	/**
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return all posted photos.
	 */
	List<PostDTO> getPhotoGallery(int userId, int page);
	
	/**
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return all posted videos.
	 */
	List<PostDTO> getVideoGallery(int userId, int page);
	
	/**
	 * @param postId
	 * 		post id.
	 * @param userId
	 * 		user id.
	 *
	 * @return prepare post to be sent via service.
	 */
	PostDTO fillPostToSend(int postId, Long userId);
	
	/**
	 * @param id
	 * 		post id.
	 * @param page
	 * 		page number.
	 *
	 * @return list of all edited posts.
	 */
	List<ContentEditedResponseDTO> getAllPostHistory(int id, int page);
	
	/**
	 * @param postId
	 * 		post id.
	 *
	 * @return last modification.
	 */
	List<EditHistoryEntity> getLastModification(int postId);
	
	/**
	 * @param sportId
	 * 		sport id.
	 *
	 * @return sport.
	 */
	SportEntity getSportById(Long sportId);
	
	/**
	 * @param id
	 * 		post id.
	 * @param visibility
	 * 		visibility type.
	 */
	void editPostVisibility(int id, int visibility);
	
	/**
	 * @param userLongId
	 * 		user technical id.
	 * @param userIntId
	 * 		user unique id.
	 * @param visibility
	 * 		visibility type.
	 * @param page
	 * 		page number.
	 *
	 * @return all user posts
	 */
	List<PostEntity> findAllPosts(Long userLongId, int userIntId, List visibility, int page);
	
	/**
	 * @param connectedUserUuid
	 * 		user uuid.
	 * @param friendId
	 * 		friend id.
	 *
	 * @return true if target user is friend, false otherwise
	 */
	boolean isTargetUserFriendOfMe(int connectedUserUuid, int friendId);
	
	/**
	 * Get all friend posts.
	 *
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 * @param accountUserId
	 *
	 * @return list of posts.
	 */
	List<PostDTO> getAllTimelinePosts(int userId, int page, Long accountUserId);
	
	/**
	 * Get all user posts.
	 *
	 * @param connectedUserId
	 * 		user id.
	 * @param connectedUserUuid
	 * 		connected user UUID.
	 * @param targetUserPost
	 * 		posts looking for.
	 * @param page
	 * 		page number.
	 *
	 * @return list of posts.
	 */
	List<PostDTO> getAllUserPosts(Long connectedUserId, int connectedUserUuid, int targetUserPost, int page);
}