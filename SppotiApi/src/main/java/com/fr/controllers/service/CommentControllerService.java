/**
 *
 */
package com.fr.controllers.service;

import com.fr.commons.dto.CommentModel;
import com.fr.commons.dto.ContentEditedResponse;
import com.fr.entities.Comment;
import com.fr.entities.EditHistory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */
@Service
public interface CommentControllerService extends AbstractControllerService {

    Comment saveComment(Comment newComment);

    boolean deleteComment(Comment comment);

    Comment findComment(int id);

    boolean updateComment(EditHistory commentEditRow);

    List<CommentModel> getPostCommentsFromLastId(int postId, int page, Long userId);

    List<ContentEditedResponse> getAllCommentHistory(int id, int page);
}
