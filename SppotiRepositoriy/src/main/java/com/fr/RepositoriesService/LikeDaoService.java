package com.fr.RepositoriesService;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.fr.entities.LikeContent;

/**
 * Created by: Wail DJENANE on Aug 15, 2016
 */
@Service
public interface LikeDaoService extends GenericDaoService<LikeContent, Integer> {

    /**
     * @param id
     * @param userId
     * @return
     */
    boolean unLikeComment(Long id, Long userId);

    /**
     * @param posttId
     * @param userId
     * @return
     */
    boolean unLikePost(Long posttId, Long userId);

    /**
     * @param posttId
     * @param userId
     * @return
     */
    boolean isPostAlreadyLiked(Long postId, Long userId);

    /**
     * @param commentId
     * @param userId
     * @return
     */
    boolean isCommentAlreadyLiked(Long commentId, Long userId);

    /**
     * @param id
     * @param page
     * @return
     */
    List<LikeContent> getPostLikers(Long id, int page);

    /**
     * @param id
     * @param page
     * @return
     */
    List<LikeContent> getCommentLikers(Long id, int page);
}
