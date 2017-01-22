/**
 *
 */
package com.fr.controllers.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.dto.CommentModel;
import com.fr.dto.ContentEditedResponse;
import com.fr.entities.Comment;
import com.fr.entities.EditHistory;

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
