package com.fr.controllers.service;

import com.fr.entities.Comment;
import com.fr.entities.LikeContent;
import com.fr.entities.Post;
import com.fr.entities.Users;
import com.fr.dto.HeaderData;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by djenanewail on 12/24/16.
 */

@Service
public interface LikeControllerService{
    boolean likePost(LikeContent likeToSave);

    boolean unLikePost(Post post);

    boolean isPostAlreadyLikedByUser(int postId, Long userId);

    boolean unLikeComment(Comment commentToLike);

    boolean isCommentAlreadyLikedByUser(int id, Long userId);

    boolean likeComment(LikeContent likeToSave);

    Users getUserById(Long userId);

    List<HeaderData> getPostLikersList(int id, int page);

    List<HeaderData> getCommentLikersList(int id, int page);
}
