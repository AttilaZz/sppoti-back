/**
 * 
 */
package com.fr.controllers.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.models.ContentEditedResponse;
import com.fr.models.HeaderData;
import com.fr.models.PostResponse;
import com.fr.pojos.Address;
import com.fr.pojos.EditHistory;
import com.fr.pojos.LikeContent;
import com.fr.pojos.Post;
import com.fr.pojos.Sport;
import com.fr.pojos.Sppoti;

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

@Service
public interface PostControllerService extends AbstractControllerService {

	public Serializable savePost(Post post);

	public boolean updatePost(EditHistory postEditRow, Address postEditAddress);

	public boolean deletePost(Post p);

	public Post findPost(Long id);

	public Sport getSportToUse(Long id);

	public Sppoti getGameById(Long id);

	public boolean likePost(LikeContent likeToSave);

	public boolean unLikePost(Long id, Long userId);

	public boolean isPostAlreadyLikedByUser(Long id, Long userId);

	public List<PostResponse> getPhotoGallery(Long userId, int buttomMarker);

	public List<PostResponse> getVideoGallery(Long userId, int buttomMarker);

	public PostResponse fillPostToSend(Long userId);

	public List<ContentEditedResponse> getAllPostHistory(Long id, int page);

	public List<HeaderData> getLikersList(Long id, int buttomMarker);

	public List<EditHistory> getLastModification(Long postId);

	public Sport getSportById(Long sport_id);

	public boolean editPostVisibility(Long id, int visibility);

	boolean addNotification(Long userId, Long postId, String content);
}
