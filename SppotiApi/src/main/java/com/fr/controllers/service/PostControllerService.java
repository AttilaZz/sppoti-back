/**
 *
 */
package com.fr.controllers.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.models.ContentEditedResponse;
import com.fr.models.HeaderData;
import com.fr.models.PostResponse;
import com.fr.entities.Address;
import com.fr.entities.EditHistory;
import com.fr.entities.LikeContent;
import com.fr.entities.Post;
import com.fr.entities.Sport;
import com.fr.entities.Sppoti;

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

@Service
public interface PostControllerService extends AbstractControllerService {

    Post savePost(Post post);

    boolean updatePost(EditHistory postEditRow, Address postEditAddress);

    boolean deletePost(Post p);

    Post findPost(Long id);

    Sport getSportToUse(Long id);

    Sppoti getGameById(Long id);

    boolean likePost(LikeContent likeToSave);

    boolean unLikePost(Post post);

    boolean isPostAlreadyLikedByUser(Long id, Long userId);

    List<PostResponse> getPhotoGallery(Long userId, int buttomMarker);

    List<PostResponse> getVideoGallery(Long userId, int buttomMarker);

    PostResponse fillPostToSend(Post post, Long userId);

    List<ContentEditedResponse> getAllPostHistory(Long id, int page);

    List<HeaderData> getLikersList(Long id, int buttomMarker);

    List<EditHistory> getLastModification(Long postId);

    Sport getSportById(Long sport_id);

    boolean editPostVisibility(Long id, int visibility);

    boolean addNotification(Long userId, Long postId, String content);

    List<Post> finAllPosts();
}
