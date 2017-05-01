package com.fr.service;

import com.fr.commons.dto.HeaderDataDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.LikeContentEntity;
import com.fr.entities.PostEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by djenanewail on 12/24/16.
 */

@Service
public interface LikeControllerService extends AbstractControllerService
{
	
	/**
	 * Like like or post
	 *
	 * @param likeToSave
	 */
	LikeContentEntity likePost(LikeContentEntity likeToSave);
	
	/**
	 * unlike post
	 *
	 * @param post
	 */
	void unLikePost(PostEntity post);
	
	/**
	 * @param postId
	 * @param userId
	 *
	 * @return true if post already liked, false otherwise
	 */
	boolean isPostAlreadyLikedByUser(int postId,Long userId);
	
	/**
	 * Unlike content, post or like
	 *
	 * @param commentEntityToLike
	 */
	void unLikeComment(CommentEntity commentEntityToLike);
	
	/**
	 * @param id
	 * @param userId
	 *
	 * @return true if like already liked, false otherwise
	 */
	boolean isCommentAlreadyLikedByUser(int id,Long userId);
	
	/**
	 * Like like
	 *
	 * @param likeToSave
	 */
	void likeComment(LikeContentEntity likeToSave);
	
	/**
	 * @param id
	 * @param page
	 *
	 * @return All persons who likes the post
	 */
	List<HeaderDataDTO> getPostLikersList(int id,int page);
	
	/**
	 * @param id
	 * @param page
	 *
	 * @return All persons who likes the like
	 */
	List<HeaderDataDTO> getCommentLikersList(int id,int page);
}
