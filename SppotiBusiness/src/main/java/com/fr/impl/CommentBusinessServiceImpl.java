package com.fr.impl;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.ContentEditedResponseDTO;
import com.fr.commons.enumeration.notification.NotificationTypeEnum;
import com.fr.entities.CommentEntity;
import com.fr.entities.EditHistoryEntity;
import com.fr.entities.PostEntity;
import com.fr.service.CommentBusinessService;
import com.fr.service.NotificationBusinessService;
import com.fr.transformers.CommentTransformer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.fr.commons.enumeration.notification.NotificationObjectType.COMMENT;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */
@Component
class CommentBusinessServiceImpl extends CommonControllerServiceImpl implements CommentBusinessService
{
	
	private final Logger LOGGER = LoggerFactory.getLogger(CommentBusinessServiceImpl.class);
	
	private final CommentTransformer commentTransformer;
	private final NotificationBusinessService notificationService;
	
	/** Comment list size. */
	@Value("${key.commentsPerPage}")
	private int commentSize;
	
	@Autowired
	CommentBusinessServiceImpl(final CommentTransformer commentTransformer,
							   final NotificationBusinessService notificationService)
	{
		this.commentTransformer = commentTransformer;
		this.notificationService = notificationService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public CommentDTO saveComment(final CommentEntity newCommentEntity, final Long userId, final String postId)
	{
		
		
		// get post postId to link the like
		final List<PostEntity> postEntity = this.postRepository.getByUuidAndDeletedFalse(postId);
		if (!postEntity.isEmpty()) {
			newCommentEntity.setPost(postEntity.get(0));
		} else {
			throw new EntityNotFoundException("Post not found (" + postId + ")");
		}
		
		newCommentEntity.setUser(this.userRepository.findOne(getConnectedUser().getId()));
		final Optional<CommentEntity> commentEntity = Optional
				.ofNullable(this.commentRepository.save(newCommentEntity));
		
		if (commentEntity.isPresent()) {
			final String targetUser = commentEntity.get().getPost().getTargetUserProfile().getUuid();
			if (!StringUtils.isBlank(targetUser) && !Objects.equals(targetUser, getConnectedUser().getUuid())) {
				
				//like on other posts not mine
				if (!Objects.equals(commentEntity.get().getUser().getUuid(),
						commentEntity.get().getPost().getTargetUserProfile().getUuid())) {
					this.notificationService.saveAndSendNotificationToUsers(commentEntity.get().getUser(),
							commentEntity.get().getPost().getTargetUserProfile(), COMMENT,
							NotificationTypeEnum.X_COMMENTED_ON_YOUR_POST, commentEntity.get().getPost(),
							commentEntity.get());
					
				}
				
				this.notificationService.addTagNotification(null, commentEntity.get());
				
			}
			
			return this.commentTransformer.modelToDto(commentEntity.get());
		}
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public boolean deleteComment(final CommentEntity commentEntity)
	{
		commentEntity.setDeleted(true);
		try {
			this.commentRepository.save(commentEntity);
			return true;
		} catch (final Exception e) {
			this.LOGGER.error("Error saving commentEntity: ", e);
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommentEntity findComment(final String id)
	{
		return this.commentRepository.getByUuidAndDeletedFalse(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public boolean updateComment(final EditHistoryEntity commentToEdit)
	{
		
		try {
			this.editHistoryRepository.save(commentToEdit);
			return true;
		} catch (final Exception e) {
			this.LOGGER.error("Error saving edited like: ", e);
			return false;
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CommentDTO> getPostCommentsFromLastId(final String postId, final int page, final Long userId)
	{
		
		final Pageable pageable = new PageRequest(page, this.commentSize);
		
		final List<CommentEntity> lCommentEntity = this.commentRepository
				.getByPostUuidAndDeletedFalseOrderByDatetimeCreatedDesc(postId, pageable);
		
		//userId used to distinguich connected user comments
		return fillCommentModelList(lCommentEntity, userId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ContentEditedResponseDTO> getAllCommentHistory(final String id, final int page)
	{
		
		final Pageable pageable = new PageRequest(page, this.commentSize);
		
		final List<EditHistoryEntity> dsHistoryList = this.editHistoryRepository
				.getByCommentUuidOrderByDatetimeEditedDesc(id, pageable);
		return fillEditContentResponse(dsHistoryList);
	}
	
}
