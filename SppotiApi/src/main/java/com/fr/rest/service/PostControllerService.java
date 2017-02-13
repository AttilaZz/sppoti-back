package com.fr.rest.service;

import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.commons.dto.PostResponseDTO;
import com.fr.entities.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.SortedSet;


/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */

@Service
public interface PostControllerService extends AbstractControllerService {

    /**
     * @param post
     * @return saved post
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
     * @param postEditAddress
     * @param postId
     * @return true if post has been edited, false otherwise
     */
    boolean updatePost(EditHistory postEditRow, SortedSet<Address> postEditAddress, int postId);

    /**
     * @param p
     * @return true if post has been deleted, false otherwise
     */
    boolean deletePost(PostEntity p);

    /**
     * @param id
     * @return post
     */
    PostEntity findPost(int id);

    /**
     * @param id
     * @return sport
     */
    Sport getSportToUse(Long id);

    /**
     * @param id
     * @return Sppoti
     */
    Sppoti getSppotiById(Long id);

    /**
     * @param userId
     * @param buttomMarker
     * @return all posted photos
     */
    List<PostResponseDTO> getPhotoGallery(Long userId, int buttomMarker);

    /**
     * @param userId
     * @param buttomMarker
     * @return all posted videos
     */
    List<PostResponseDTO> getVideoGallery(Long userId, int buttomMarker);

    /**
     * @param post
     * @param userId
     * @return prepare post to be sent via service
     */
    PostResponseDTO fillPostToSend(PostEntity post, Long userId);

    /**
     * @param id
     * @param page
     * @return list of all edited content
     */
    List<ContentEditedResponseDTO> getAllPostHistory(int id, int page);

    /**
     * @param postId
     * @return las modification
     */
    List<EditHistory> getLastModification(int postId);

    /**
     * @param sport_id
     * @return sport
     */
    Sport getSportById(Long sport_id);

    /**
     * @param id
     * @param visibility
     * @return true if visibility has been edited, false otherwise
     */
    boolean editPostVisibility(int id, int visibility);

    /**
     * @param userLongId
     * @param userIntId
     * @param visibility
     * @param page
     * @return all user posts
     */
    List<PostEntity> findAllPosts(Long userLongId, int userIntId, List visibility, int page);

    /**
     * @param connected_user_uuid
     * @param friend_id
     * @return true if target user is friend, false otherwise
     */
    boolean isTargetUserFriendOfMe(int connected_user_uuid, int friend_id);
}