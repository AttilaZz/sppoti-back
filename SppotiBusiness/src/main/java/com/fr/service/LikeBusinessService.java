package com.fr.service;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.LikeContentEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by djenanewail on 12/24/16.
 */

@Service
public interface LikeBusinessService extends AbstractBusinessService
{
	
	LikeContentEntity likePost(LikeContentEntity likeToSave);

	void unLikePost(String postId);

	List<UserDTO> getPostLikersList(String id, int page);

	List<UserDTO> getCommentLikersList(String id, int page);

    void unLikeComment(CommentEntity commentEntityToLike);

    boolean isCommentAlreadyLikedByUser(String commentId, Long userId);

    void likeComment(LikeContentEntity likeToSave);

    boolean isPostAlreadyLikedByUser(String postId, Long userId);
}
