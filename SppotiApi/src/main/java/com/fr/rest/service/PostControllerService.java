package com.fr.rest.service;

import java.util.List;
import java.util.SortedSet;

import com.fr.commons.dto.ContentEditedResponse;
import com.fr.commons.dto.PostResponse;
import com.fr.entities.*;
import org.springframework.stereotype.Service;


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

    List<PostResponse> getPhotoGallery(Long userId, int buttomMarker);

    List<PostResponse> getVideoGallery(Long userId, int buttomMarker);

    PostResponse fillPostToSend(Post post, Long userId);

    List<ContentEditedResponse> getAllPostHistory(int id, int page);

    List<EditHistory> getLastModification(int postId);

    Sport getSportById(Long sport_id);

    boolean editPostVisibility(int id, int visibility);

    boolean addNotification(Long userId, int postId, String content);

    List<Post> findAllPosts(Long userLongId, int userIntId, List visibility, int page);

    boolean isTargetUserFriendOfMe(int connected_user_uuid, int friend_id);
}