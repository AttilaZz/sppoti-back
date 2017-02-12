/**
 *
 */
package com.fr.core;

import com.fr.entities.UserEntity;
import com.fr.rest.service.CommentControllerService;
import com.fr.entities.CommentEntity;
import com.fr.entities.EditHistory;
import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.ContentEditedResponseDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import utils.EntitytoDtoTransformer;

import java.util.List;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */
@Component
public class CommentControllerServiceImpl extends AbstractControllerServiceImpl implements CommentControllerService {

    private Logger LOGGER = Logger.getLogger(CommentControllerServiceImpl.class);

    @Value("${key.commentsPerPage}")
    private int commentSize;

    @Override
    public CommentDTO saveComment(CommentEntity newCommentEntity, Long userId) {

        return EntitytoDtoTransformer.commentEntityToDto(commentRepository.save(newCommentEntity), getUserById(userId));

    }

    @Override
    public boolean deleteComment(CommentEntity commentEntity) {
        commentEntity.setDeleted(true);
        try {
            commentRepository.save(commentEntity);
            return true;
        } catch (Exception e) {
            LOGGER.error("Error saving commentEntity: ", e);
            return false;
        }
    }

    @Override
    public CommentEntity findComment(int id) {
        return commentRepository.getByUuid(id);
    }

    @Override
    public boolean updateComment(EditHistory commentToEdit) {

        try {
            editHistoryRepository.save(commentToEdit);
            return true;
        } catch (Exception e) {
            LOGGER.error("Error saving edited comment: ", e);
            return false;
        }

    }

    @Override
    public List<CommentDTO> getPostCommentsFromLastId(int postId, int page, Long userId) {

        Pageable pageable = new PageRequest(page, commentSize);

        List<CommentEntity> lCommentEntity = commentRepository.getByPostUuidOrderByDatetimeCreatedDesc(postId, pageable);

        //userId used to distinguich connected user comments
        return fillCommentModelList(lCommentEntity, userId);
    }

    @Override
    public List<ContentEditedResponseDTO> getAllCommentHistory(int id, int page) {

        Pageable pageable = new PageRequest(page, commentSize);

        List<EditHistory> dsHistoryList = editHistoryRepository.getByCommentUuidOrderByDatetimeEditedDesc(id, pageable);
        return fillEditContentResponse(dsHistoryList);
    }

}
