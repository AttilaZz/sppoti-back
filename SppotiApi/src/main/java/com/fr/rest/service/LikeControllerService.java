package com.fr.rest.service;

import com.fr.commons.dto.HeaderDataDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.LikeContent;
import com.fr.entities.Post;
import com.fr.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by djenanewail on 12/24/16.
 */

@Service
public interface LikeControllerService extends AbstractControllerService{
    boolean likePost(LikeContent likeToSave);

    boolean unLikePost(Post post);

    boolean isPostAlreadyLikedByUser(int postId, Long userId);

    boolean unLikeComment(CommentEntity commentEntityToLike);

    boolean isCommentAlreadyLikedByUser(int id, Long userId);

    boolean likeComment(LikeContent likeToSave);

    UserEntity getUserById(Long userId);

    List<HeaderDataDTO> getPostLikersList(int id, int page);

    List<HeaderDataDTO> getCommentLikersList(int id, int page);
}
