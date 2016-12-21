/**
 *
 */
package com.fr.controllers.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.fr.controllers.service.CommentControllerService;
import com.fr.models.CommentModel;
import com.fr.models.ContentEditedResponse;
import com.fr.models.HeaderData;
import com.fr.entities.Comment;
import com.fr.entities.EditHistory;
import com.fr.entities.LikeContent;
import com.fr.entities.Post;

import javax.validation.Valid;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */
@Component
public class CommentControllerServiceimpl extends AbstractControllerServiceImpl implements CommentControllerService {

    @Value("${key.commentsPerPage}")
    private int comment_size;

    @Override
    public boolean saveComment(Comment newComment) {
        return commentDaoService.saveOrUpdate(newComment);
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
    public Post findPostById(int id) {
        return postRepository.getByUuid(id);
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
    public List<CommentModel> getPostCommentsFromLastId(Long postId, int bottomMajId, Long userId) {
        List<Comment> lComment = commentDaoService.getCommentsFromLastMajId(postId, bottomMajId);

        return fillCommentModelList(lComment, userId);
    }

    @Override
    public List<ContentEditedResponse> getAllPostHistory(int id, int page) {

        int debut = page * comment_size;

        Pageable pageable = new PageRequest(debut, comment_size);

        List<EditHistory> dsHistoryList = editHistoryRepository.getByCommentUuidOrderByDatetimeEditedDesc(id, pageable);
        return fillEditContentResponse(dsHistoryList);
    }

}
