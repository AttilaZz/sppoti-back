/**
 *
 */
package com.fr.controllers.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fr.controllers.service.CommentControllerService;
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
@Component
public class CommentControllerServiceimpl extends AbstractControllerServiceImpl implements CommentControllerService {

    @Override
    public boolean saveComment(Comment newComment) {
        return commentDaoService.saveOrUpdate(newComment);
    }

    @Override
    public boolean deleteComment(Comment comment) {
        return commentDaoService.delete(comment);
    }

    @Override
    public Comment findComment(Long id) {
        return commentDaoService.getEntityByID(id);
    }

    @Override
    public Post findPostById(Long id) {
        return postDaoService.getEntityByID(id);
    }

    @Override
    public boolean updateComment(EditHistory commentToEdit) {
        return editContentDaoService.saveOrUpdate(commentToEdit);
    }

    @Override
    public List<CommentModel> getPostCommentsFromLastId(Long postId, int bottomMajId, Long userId) {
        List<Comment> lComment = commentDaoService.getCommentsFromLastMajId(postId, bottomMajId);

        return fillCommentModelList(lComment, userId);
    }

    @Override
    public List<ContentEditedResponse> getAllPostHistory(Long id, int page) {
        List<EditHistory> dsHistoryList = editContentDaoService.getAllComenttHistory(id, page);
        return fillEditContentResponse(dsHistoryList);
    }

}
