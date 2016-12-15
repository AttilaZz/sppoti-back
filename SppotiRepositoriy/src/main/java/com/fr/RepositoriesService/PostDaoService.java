package com.fr.RepositoriesService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.entities.Post;

/**
 * Created by: Wail DJENANE on Jun 4, 2016
 */
@Service
public interface PostDaoService extends GenericDaoService<Post, Integer> {
    /**
     * @param userSportsId
     * @param pageId
     */
    List<Post> getPostsFromSubscribedUserSports(Long[] userSportsId, int pageId);

    /**
     * @param userId
     * @param buttomMarker
     * @return
     */
    List<Post> getPhotoGalleryPostsFromLastMajId(Long userId, int buttomMarker);

    /**
     * @param userId
     * @param buttomMarker
     * @return
     */
    List<Post> getVideoGalleryPostsFromLastMajId(Long userId, int buttomMarker);

    /**
     * @param userId
     * @param buttomMarker
     * @return
     */
    List<Post> getPostsFromLastPage(Long userId, int buttomMarker);

}
