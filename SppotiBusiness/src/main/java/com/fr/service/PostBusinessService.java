package com.fr.service;

import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.commons.dto.post.PostRequestDTO;
import com.fr.entities.*;

import java.util.List;
import java.util.SortedSet;


/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

public interface PostBusinessService extends AbstractBusinessService
{

	PostDTO savePost(PostRequestDTO postRequestDTO);

	boolean updatePost(EditHistoryEntity postEditRow, SortedSet<AddressEntity> postEditAddress,
					   String postId);

	void deletePost(String postId);

	PostEntity findPost(String id);

	SportEntity getSportToUse(Long id);

	SppotiEntity getSppotiById(Long id);

	List<PostDTO> getPhotoGallery(String userId, int page);

	List<PostDTO> getVideoGallery(String userId, int page);

	PostDTO fillPostToSend(String postId, Long userId);

	List<ContentEditedResponseDTO> getAllPostHistory(String id, int page);

	List<EditHistoryEntity> getLastModification(String postId);

	SportEntity getSportById(Long sportId);

	void editPostVisibility(String id, int visibility);

	List<PostEntity> findAllPosts(Long userLongId, String userIntId, List visibility,
								  int page);

	boolean isTargetUserFriendOfMe(String connectedUserUuid, String friendId);

	List<PostDTO> getAllUserPosts(Long connectedUserId, String connectedUserUuid, String userID,
								  int page);

	List<PostDTO> getAllTimelinePosts(String userId, int page, Long accountUserId);
}