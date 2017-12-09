package com.fr.impl;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.LikeContentEntity;
import com.fr.entities.PostEntity;
import com.fr.entities.UserEntity;
import com.fr.service.LikeBusinessService;
import com.fr.service.NotificationBusinessService;
import com.fr.transformers.CommentTransformer;
import com.fr.transformers.PostTransformer;
import com.fr.transformers.UserTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.fr.commons.enumeration.notification.NotificationObjectType.LIKE;
import static com.fr.commons.enumeration.notification.NotificationTypeEnum.X_LIKED_YOUR_COMMENT;
import static com.fr.commons.enumeration.notification.NotificationTypeEnum.X_LIKED_YOUR_POST;

/**
 * Created by djenanewail on 12/24/16.
 */
@Component
class LikeBusinessServiceImpl extends CommonControllerServiceImpl implements LikeBusinessService
{
	private final Logger LOGGER = LoggerFactory.getLogger(LikeBusinessServiceImpl.class);
	
	@Autowired
	private UserTransformer userTransformer;
	@Autowired
	private PostTransformer postTransformer;
	@Autowired
	private CommentTransformer commentTransformer;
	@Autowired
	private NotificationBusinessService notificationService;
	
	@Value("${key.likesPerPage}")
	private int likeSize;
	
	@Transactional
	@Override
	public LikeContentEntity likePost(final LikeContentEntity likeToSave)
	{
		final PostEntity entity = likeToSave.getPost();
		entity.setConnectedUserId(getConnectedUserId());
		
		this.LOGGER.info("Process triggered to like post: {}", this.postTransformer.modelToDto(entity));
		
		return likeContent(likeToSave);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void unLikePost(final String postId)
	{
		this.LOGGER.info("Process triggered to unlike post: {}", postId);
		
		final Optional<PostEntity> optional = this.postRepository.findTopByUuidAndDeletedFalse(postId);
		
		final UserEntity connectedUser = getConnectedUser();
		
		optional.orElseThrow(() -> new EntityNotFoundException("Post " + postId + " not found"));
		
		optional.ifPresent(p -> {
			if (this.isPostAlreadyLikedByUser(postId, connectedUser.getId())) {
				
				final LikeContentEntity likeContent = this.likeRepository
						.findTopByPostIdAndUserId(p.getId(), connectedUser.getId());
				this.likeRepository.delete(likeContent);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPostAlreadyLikedByUser(final String postId, final Long userId)
	{
		return this.likeRepository.findTopByUserIdAndPostUuid(userId, postId) != null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserDTO> getPostLikersList(final String postId, final int page)
	{
		this.LOGGER.info("Process triggered to get post <{}> likers list", postId);
		
		final Pageable pageable1 = new PageRequest(page, this.likeSize);
		
		final List<LikeContentEntity> likersData = this.likeRepository
				.getByPostUuidOrderByDatetimeCreatedDesc(postId, pageable1);
		
		return likersList(likersData);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserDTO> getCommentLikersList(final String commentId, final int page)
	{
		this.LOGGER.info("Process triggered to get comment <{}> likers list", commentId);
		
		final Pageable pageable1 = new PageRequest(page, this.likeSize);
		
		final List<LikeContentEntity> likersData = this.likeRepository
				.findByCommentUuidOrderByDatetimeCreatedDesc(commentId, pageable1);
		
		return likersList(likersData);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void unLikeComment(final CommentEntity commentEntityToUnlike)
	{
		this.LOGGER.info("Process triggered to unlike comment {}",
				this.commentTransformer.modelToDto(commentEntityToUnlike));
		
		final LikeContentEntity likeContent = this.likeRepository.findTopByCommentId(commentEntityToUnlike.getId());
		this.likeRepository.delete(likeContent);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCommentAlreadyLikedByUser(final String commentId, final Long userId)
	{
		return Objects.nonNull(this.likeRepository.findTopByUserIdAndCommentUuid(userId, commentId));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void likeComment(final LikeContentEntity likeToSave)
	{
		this.LOGGER.info("Process triggered to like comment {}",
				this.commentTransformer.modelToDto(likeToSave.getComment()));
		likeContent(likeToSave);
	}
	
	/**
	 * like content - PostEntity or CommentEntity
	 *
	 * @param likeContent
	 * 		content to like.
	 *
	 * @return liked content.
	 */
	@Transactional
	private LikeContentEntity likeContent(final LikeContentEntity likeContent)
	{
		
		if (likeContent != null) {
			
			if (likeContent.getComment() != null &&
					!likeContent.getComment().getUser().getId().equals(getConnectedUser().getId())) {
				//like comment
				this.notificationService
						.saveAndSendNotificationToUsers(likeContent.getUser(), likeContent.getComment().getUser(), LIKE,
								X_LIKED_YOUR_COMMENT, likeContent.getComment().getPost(), likeContent.getComment());
				
			} else if (likeContent.getPost() != null &&
					!likeContent.getPost().getUser().getId().equals(getConnectedUser().getId())) {
				//like post
				this.notificationService
						.saveAndSendNotificationToUsers(likeContent.getUser(), likeContent.getPost().getUser(), LIKE,
								X_LIKED_YOUR_POST, likeContent.getPost());
			}
			
		}
		
		return this.likeRepository.save(likeContent);
		
	}
	
	private List<UserDTO> likersList(final List<LikeContentEntity> likeContentEntityList)
	{
		return likeContentEntityList.stream().map(u -> this.userTransformer.modelToDto(u.getUser()))
				.collect(Collectors.toList());
	}
}
