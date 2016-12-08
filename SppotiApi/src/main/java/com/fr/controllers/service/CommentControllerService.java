/**
 * 
 */
package com.fr.controllers.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.models.CommentModel;
import com.fr.models.ContentEditedResponse;
import com.fr.models.HeaderData;
import com.fr.entities.Comment;
import com.fr.entities.EditHistory;
import com.fr.entities.LikeContent;
import com.fr.entities.Post;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */
@Service
public interface CommentControllerService extends AbstractControllerService {

	boolean saveComment(Comment newComment);

	boolean deleteComment(Comment comment);

	public Comment findComment(Long id);

	public Post findPostById(Long id);

	boolean updateComment(EditHistory commentEditRow);

	boolean likeComment(LikeContent likeToSave);

	boolean unLikeComment(Long id, Long userId);

	boolean isCommentAlreadyLikedByUser(Long id, Long userId);

	public List<CommentModel> getPostCommentsFromLastId(Long postId, int bottomMajId, Long userId);

	public List<ContentEditedResponse> getAllPostHistory(Long id, int page);

	List<HeaderData> getLikersList(Long id, int page);

}
