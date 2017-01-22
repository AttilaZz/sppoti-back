/**
 *
 */
package com.fr.core;

import com.fr.controllers.service.CommentControllerService;
import com.fr.entities.Comment;
import com.fr.entities.EditHistory;
import com.fr.commons.dto.CommentModel;
import com.fr.commons.dto.ContentEditedResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */
@Component
public class CommentControllerServiceimpl extends AbstractControllerServiceImpl implements CommentControllerService {

    @Value("${key.commentsPerPage}")
    private int comment_size;

    @Override
    public Comment saveComment(Comment newComment) {

        return commentRepository.save(newComment);

    }

    @Override
    public boolean deleteComment(Comment comment) {
        comment.setDeleted(true);
        try {
            commentRepository.save(comment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Comment findComment(int id) {
        return commentRepository.getByUuid(id);
    }

    @Override
    public boolean updateComment(EditHistory commentToEdit) {

        try {
            editHistoryRepository.save(commentToEdit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public List<CommentModel> getPostCommentsFromLastId(int postId, int page, Long userId) {

        Pageable pageable = new PageRequest(page, comment_size);

        List<Comment> lComment = commentRepository.getByPostUuidOrderByDatetimeCreatedDesc(postId, pageable);

        //userId used to distinguich connected user comments
        return fillCommentModelList(lComment, userId);
    }

    @Override
    public List<ContentEditedResponse> getAllCommentHistory(int id, int page) {

        Pageable pageable = new PageRequest(page, comment_size);

        List<EditHistory> dsHistoryList = editHistoryRepository.getByCommentUuidOrderByDatetimeEditedDesc(id, pageable);
        return fillEditContentResponse(dsHistoryList);
    }

}