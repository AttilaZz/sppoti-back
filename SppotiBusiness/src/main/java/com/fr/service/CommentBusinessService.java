/**
 *
 */
package com.fr.service;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.EditHistoryEntity;

import java.util.List;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */
public interface CommentBusinessService extends AbstractBusinessService
{

    CommentDTO saveComment(CommentEntity newCommentEntity, Long userId, String postId);

    boolean deleteComment(CommentEntity commentEntity);

	CommentEntity findComment(String id);

	boolean updateComment(EditHistoryEntity commentEditRow);
	
	List<CommentDTO> getPostCommentsFromLastId(String postId, int page, Long userId);

	List<ContentEditedResponseDTO> getAllCommentHistory(String id, int page);
}
