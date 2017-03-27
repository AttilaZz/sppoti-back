package com.fr.impl;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.EditHistoryEntity;
import com.fr.entities.PostEntity;
import com.fr.models.NotificationType;
import com.fr.service.CommentControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.fr.transformers.CommentTransformer;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */
@Component
public class CommentControllerServiceImpl extends AbstractControllerServiceImpl implements CommentControllerService {

    private Logger LOGGER = Logger.getLogger(CommentControllerServiceImpl.class);

    @Value("${key.commentsPerPage}")
    private int commentSize;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public CommentDTO saveComment(CommentEntity newCommentEntity, Long userId, int postId) {


        // get post postId to link the like
        List<PostEntity> postEntity = postRepository.getByUuid(postId);
        if (!postEntity.isEmpty()) {
            newCommentEntity.setPost(postEntity.get(0));
        } else {
            throw new EntityNotFoundException("Post not found (" + postId + ")");
        }

        newCommentEntity.setUser(userRepository.findOne(getConnectedUser().getId()));
        Optional<CommentEntity> commentEntity = Optional.ofNullable(commentRepository.save(newCommentEntity));

        if (commentEntity.isPresent()) {
            int targetUser = commentEntity.get().getPost().getTargetUserProfileUuid();
            if (targetUser != 0 && targetUser != getConnectedUser().getUuid()) {

                //like on other posts not mine
                if (commentEntity.get().getUser().getUuid() != commentEntity.get().getPost().getTargetUserProfileUuid()) {
                    addNotification(NotificationType.X_COMMENTED_ON_YOUR_POST, commentEntity.get().getUser(), getUserByUuId(commentEntity.get().getPost().getTargetUserProfileUuid()), null, null);

                }

                addTagNotification(null, commentEntity.get());

            }

            return CommentTransformer.commentEntityToDto(commentEntity.get(), getUserById(userId));
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
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

    /**
     * {@inheritDoc}
     */
    @Override
    public CommentEntity findComment(int id) {
        return commentRepository.getByUuid(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public boolean updateComment(EditHistoryEntity commentToEdit) {

        try {
            editHistoryRepository.save(commentToEdit);
            return true;
        } catch (Exception e) {
            LOGGER.error("Error saving edited like: ", e);
            return false;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CommentDTO> getPostCommentsFromLastId(int postId, int page, Long userId) {

        Pageable pageable = new PageRequest(page, commentSize);

        List<CommentEntity> lCommentEntity = commentRepository.getByPostUuidOrderByDatetimeCreatedDesc(postId, pageable);

        //userId used to distinguich connected user comments
        return fillCommentModelList(lCommentEntity, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ContentEditedResponseDTO> getAllCommentHistory(int id, int page) {

        Pageable pageable = new PageRequest(page, commentSize);

        List<EditHistoryEntity> dsHistoryList = editHistoryRepository.getByCommentUuidOrderByDatetimeEditedDesc(id, pageable);
        return fillEditContentResponse(dsHistoryList);
    }

}