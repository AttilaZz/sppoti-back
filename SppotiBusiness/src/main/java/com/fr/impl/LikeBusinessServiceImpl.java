package com.fr.impl;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.LikeContentEntity;
import com.fr.entities.PostEntity;
import com.fr.entities.UserEntity;
import com.fr.service.LikeBusinessService;
import com.fr.service.NotificationBusinessService;
import com.fr.transformers.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.fr.commons.enumeration.notification.NotificationObjectType.FRIENDSHIP;
import static com.fr.commons.enumeration.notification.NotificationObjectType.LIKE;
import static com.fr.commons.enumeration.notification.NotificationTypeEnum.X_LIKED_YOUR_COMMENT;
import static com.fr.commons.enumeration.notification.NotificationTypeEnum.X_LIKED_YOUR_POST;

/**
 * Created by djenanewail on 12/24/16.
 */
@Component
class LikeBusinessServiceImpl extends AbstractControllerServiceImpl implements LikeBusinessService
{
	private final UserTransformer userTransformer;
	private final NotificationBusinessService notificationService;
	
	/** post likers list size. */
	@Value("${key.likesPerPage}")
	private int likeSize;
	
	@Autowired
	LikeBusinessServiceImpl(final UserTransformer userTransformer,
							final NotificationBusinessService notificationService)
	{
		this.userTransformer = userTransformer;
		this.notificationService = notificationService;
	}
	
	@Transactional
	@Override
	public LikeContentEntity likePost(final LikeContentEntity likeToSave)
	{
		return likeContent(likeToSave);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void unLikePost(final String postId)
	{
		
		final List<PostEntity> postToUnlike = this.postRepository.getByUuidAndDeletedFalse(postId);
		
		final UserEntity connectedUser = getConnectedUser();
		
		if (postToUnlike == null) {
			// post not fount
			throw new EntityNotFoundException("Post not found");
		}
		
		//post must be liked before unlike
		if (this.isPostAlreadyLikedByUser(postId, connectedUser.getId())) {
			
			final LikeContentEntity likeContent = this.likeRepository
					.findByPostIdAndUserId(postToUnlike.get(0).getId(), connectedUser.getId());
			this.likeRepository.delete(likeContent);
			
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPostAlreadyLikedByUser(final String postId, final Long userId)
	{
		
		return this.likeRepository.getByUserIdAndPostUuid(userId, postId) != null;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserDTO> getPostLikersList(final String id, final int page)
	{
		
		final Pageable pageable1 = new PageRequest(page, this.likeSize);
		
		final List<LikeContentEntity> likersData = this.likeRepository
				.getByPostUuidOrderByDatetimeCreatedDesc(id, pageable1);
		
		return likersList(likersData);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserDTO> getCommentLikersList(final String id, final int page)
	{
		
		final Pageable pageable1 = new PageRequest(page, this.likeSize);
		
		final List<LikeContentEntity> likersData = this.likeRepository
				.getByCommentUuidOrderByDatetimeCreatedDesc(id, pageable1);
		
		return likersList(likersData);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void unLikeComment(final CommentEntity commentEntityToUnlike)
	{
		
		final LikeContentEntity likeContent = this.likeRepository.getByCommentId(commentEntityToUnlike.getId());
		this.likeRepository.delete(likeContent);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCommentAlreadyLikedByUser(final String commentId, final Long userId)
	{
		return this.likeRepository.getByUserIdAndCommentUuid(userId, commentId) != null;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void likeComment(final LikeContentEntity likeToSave)
	{
		
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
						.saveAndSendNotificationToUsers(likeContent.getUser(), likeContent.getComment().getUser(),
								FRIENDSHIP, X_LIKED_YOUR_COMMENT, likeContent.getComment().getPost(),
								likeContent.getComment());
				
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
	
	/**
	 * Transform {@link LikeContentEntity} to {@link UserEntity}.
	 *
	 * @param likeContentEntityList
	 * 		list of post or comment likers.
	 *
	 * @return list of {@link UserEntity}.
	 */
	private List<UserDTO> likersList(final List<LikeContentEntity> likeContentEntityList)
	{
		
		return likeContentEntityList.stream().map(u -> this.userTransformer.modelToDto(u.getUser()))
				.collect(Collectors.toList());
	}
}
