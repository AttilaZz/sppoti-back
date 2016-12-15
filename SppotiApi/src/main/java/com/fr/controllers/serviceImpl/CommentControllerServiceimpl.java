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
    public boolean likeComment(LikeContent likeToSave) {
        return likeDaoService.saveOrUpdate(likeToSave);
    }

    @Override
    public boolean unLikeComment(Long id, Long userId) {
        return likeDaoService.unLikeComment(id, userId);
    }

    @Override
    public boolean isCommentAlreadyLikedByUser(Long commentId, Long userId) {
        return likeDaoService.isCommentAlreadyLiked(commentId, userId);
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

    @Override
    public List<HeaderData> getLikersList(Long id, int page) {
        List<LikeContent> likersData = likeDaoService.getCommentLikers(id, page);

        List<HeaderData> likers = new ArrayList<>();

        if (!likersData.isEmpty()) {
            for (LikeContent row : likersData) {
                // get liker data
                HeaderData u = new HeaderData();
                u.setAvatar(userDaoService.getLastAvatar(row.getUser().getId()).get(0).getUrl());
                u.setFirstName(row.getUser().getFirstName());
                u.setLastName(row.getUser().getLastName());
                // u.setCover(userDao.getLastCover(row.getUser().getId(),
                // coverType));
                u.setUsername(row.getUser().getUsername());

                likers.add(u);
            }
        }

        return likers;
    }

}
