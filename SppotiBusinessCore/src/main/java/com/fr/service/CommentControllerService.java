/**
 *
 */
package com.fr.service;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.EditHistoryEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */
@Service
public interface CommentControllerService extends AbstractControllerService {

    CommentDTO saveComment(CommentEntity newCommentEntity, Long userId, int postId);

    boolean deleteComment(CommentEntity commentEntity);

    CommentEntity findComment(int commentId);

    boolean updateComment(EditHistoryEntity commentEditRow);

    List<CommentDTO> getPostCommentsFromLastId(int postId, int page, Long userId);

    List<ContentEditedResponseDTO> getAllCommentHistory(int id, int page);
}
