/**
 *
 */
package com.fr.controllers.service;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import com.fr.entities.*;
import org.springframework.stereotype.Service;

import com.fr.models.ContentEditedResponse;
import com.fr.models.HeaderData;
import com.fr.models.PostResponse;

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

@Service
public interface PostControllerService extends AbstractControllerService {

    Post savePost(Post post);

    /*
     * The post update method is used to update the location (address) of the
     * post and the content. This two jobs are separated, not at the same time.
     *
     * That's why we check if the address is not null before persisting the new
     * content
     */
    boolean updatePost(EditHistory postEditRow, SortedSet<Address> postEditAddress, int postId);

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
