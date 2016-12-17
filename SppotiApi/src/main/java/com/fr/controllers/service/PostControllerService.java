/**
 *
 */
package com.fr.controllers.service;

import java.util.List;
import java.util.UUID;

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

    Post findPost(int id);

    Sport getSportToUse(Long id);

    Sppoti getGameById(Long id);

    boolean likePost(LikeContent likeToSave);

    boolean unLikePost(Post post);

    boolean isPostAlreadyLikedByUser(int postId, Long userId);

    List<PostResponse> getPhotoGallery(Long userId, int buttomMarker);

    List<PostResponse> getVideoGallery(Long userId, int buttomMarker);

    PostResponse fillPostToSend(Post post, Long userId);

    List<ContentEditedResponse> getAllPostHistory(int id, int page);

    List<HeaderData> getLikersList(int id, int page);

    List<EditHistory> getLastModification(int postId);

    Sport getSportById(Long sport_id);

    boolean editPostVisibility(int id, int visibility);

    boolean addNotification(Long userId, int postId, String content);

    List<Post> finAllPosts(int uuid, int page);
}
