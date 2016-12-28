/**
 *
 */
package com.fr.controllers.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fr.models.CommentModel;
import com.fr.models.ContentEditedResponse;
import com.fr.entities.Comment;
import com.fr.entities.EditHistory;
import com.fr.entities.Post;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */
@Service
public interface CommentControllerService extends AbstractControllerService {

    Comment saveComment(Comment newComment);

    boolean deleteComment(Comment comment);

    Comment findComment(int id);

    Post findPostById(int id);

    boolean updateComment(EditHistory commentEditRow);

    List<CommentModel> getPostCommentsFromLastId(int postId, int page, Long userId);

    List<ContentEditedResponse> getAllCommentHistory(int id, int page);
}
