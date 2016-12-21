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

    Comment findComment(int id);

    Post findPostById(int id);

    boolean updateComment(EditHistory commentEditRow);

    List<CommentModel> getPostCommentsFromLastId(Long postId, int bottomMajId, Long userId);

    List<ContentEditedResponse> getAllPostHistory(int id, int page);
}
