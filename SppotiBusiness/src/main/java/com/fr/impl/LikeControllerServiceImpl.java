package com.fr.impl;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.NotificationTypeEnum;
import com.fr.entities.CommentEntity;
import com.fr.entities.LikeContentEntity;
import com.fr.entities.PostEntity;
import com.fr.entities.UserEntity;
import com.fr.service.LikeControllerService;
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

/**
 * Created by djenanewail on 12/24/16.
 */
@Component
class LikeControllerServiceImpl extends AbstractControllerServiceImpl implements LikeControllerService
{
	/** User transformer. */
	@Autowired
	private UserTransformer userTransformer;
	
	/** post likers list size. */
	@Value("${key.likesPerPage}")
	private int likeSize;
	
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
		final Long connectedUserId = getConnectedUser().getId();
		
		if (likeContent != null) {
			
			if (likeContent.getComment() != null &&
					!likeContent.getComment().getUser().getId().equals(getConnectedUser().getId())) {
				//Comment like
				addNotification(NotificationTypeEnum.X_LIKED_YOUR_COMMENT, likeContent.getUser(),
						likeContent.getComment().getUser(), null, null, likeContent.getComment().getPost(),
						likeContent.getComment(), null, null);
				
			} else if (likeContent.getPost() != null &&
					!likeContent.getPost().getUser().getId().equals(connectedUserId)) {
				
				if (!this.isPostAlreadyLikedByUser(likeContent.getPost().getUuid(), connectedUserId)) {
					//like post
					addNotification(NotificationTypeEnum.X_LIKED_YOUR_POST, likeContent.getUser(),
							likeContent.getPost().getUser(), null, null, likeContent.getPost(), null, null, null);
				}
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
