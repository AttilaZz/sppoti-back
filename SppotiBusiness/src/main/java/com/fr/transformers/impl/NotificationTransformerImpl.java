package com.fr.transformers.impl;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.notification.NotificationDTO;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.*;
import com.fr.transformers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by djenanewail on 2/19/17.
 */
@Transactional(readOnly = true)
@Component
public class NotificationTransformerImpl extends AbstractTransformerImpl<NotificationDTO, NotificationEntity> implements
		NotificationTransformer
{
	/** Team transformer. */
	private final TeamTransformer teamTransformer;
	/** USer transformer. */
	private final UserTransformer userTransformer;
	/** Sppoti transformer. */
	private final SppotiTransformer sppotiTransformer;
	/** Post transformer. */
	private final PostTransformer postTransformer;
	/** Comment transformer. */
	private final CommentTransformer commentTransformer;
	/** Score transformer. */
	private final ScoreTransformer scoreTransformer;
	/** Rating transformer. */
	private final RatingTransformer ratingTransformer;
	
	@Autowired
	public NotificationTransformerImpl(final TeamTransformerImpl teamTransformer,
									   final UserTransformerImpl userTransformer,
									   final SppotiTransformerImpl sppotiTransformer,
									   final PostTransformer postTransformer,
									   final CommentTransformer commentTransformer,
									   final ScoreTransformer scoreTransformer,
									   final RatingTransformer ratingTransformer)
	{
		this.teamTransformer = teamTransformer;
		this.userTransformer = userTransformer;
		this.sppotiTransformer = sppotiTransformer;
		this.postTransformer = postTransformer;
		this.commentTransformer = commentTransformer;
		this.scoreTransformer = scoreTransformer;
		this.ratingTransformer = ratingTransformer;
	}
	
	/**
	 * @param notification
	 * 		notification entity to map.
	 *
	 * @return NotificationEntity DTO.
	 */
	@Override
	public NotificationDTO modelToDto(final NotificationEntity notification)
	{
		final NotificationDTO notificationDTO = new NotificationDTO();
		notificationDTO.setId(notification.getUuid());
		notificationDTO.setDatetime(SppotiUtils.dateWithTimeZone(notification.getCreationDate(), getTimeZone()));
		
		if (notification.getFrom() != null) {
			notificationDTO.setFrom(this.notificationUserEntityToDto(notification.getFrom()));
		}
		if (notification.getTo() != null) {
			notificationDTO.setTo(this.notificationUserEntityToDto(notification.getTo()));
		}
		notificationDTO.setNotificationType(notification.getNotificationType().getType());
		notificationDTO.setStatus(notification.getStatus());
		
		//POST
		final Optional<PostEntity> optionalPost = Optional.ofNullable(notification.getPost());
		optionalPost.ifPresent(t -> {
			t.setConnectedUserId(notification.getConnectedUserId());
			notificationDTO.setPost(this.postTransformer.modelToDto(t));
		});
		
		//COMMENT
		final Optional<CommentEntity> optionalComment = Optional.ofNullable(notification.getComment());
		optionalComment.ifPresent(t -> {
			t.setConnectedUserId(notification.getConnectedUserId());
			notificationDTO.setComment(this.commentTransformer.modelToDto(t));
		});
		
		//TEAM
		final Optional<TeamEntity> optionalTeam = Optional.ofNullable(notification.getTeam());
		optionalTeam.ifPresent(t -> {
			t.setConnectedUserId(notification.getConnectedUserId());
			notificationDTO.setTeam(this.teamTransformer.modelToDto(t));
		});
		
		//SPPOTI
		final Optional<SppotiEntity> optionalSppoti = Optional.ofNullable(notification.getSppoti());
		optionalSppoti.ifPresent(t -> {
			t.setConnectedUserId(notification.getConnectedUserId());
			notificationDTO.setSppoti(this.sppotiTransformer.modelToDto(t));
		});
		
		//RATING
		final Optional<RatingEntity> ratingEntity = Optional.ofNullable(notification.getRating());
		ratingEntity.ifPresent(t -> {
			t.setConnectedUserId(notification.getConnectedUserId());
			notificationDTO.setRating(this.ratingTransformer.modelToDto(t));
		});
		
		//SCORE
		final Optional<ScoreEntity> scoreEntity = Optional.ofNullable(notification.getScore());
		scoreEntity.ifPresent(t -> {
			t.setConnectedUserId(notification.getConnectedUserId());
			notificationDTO.setScore(this.scoreTransformer.modelToDto(t));
		});
		
		return notificationDTO;
	}
	
	/**
	 * @param model
	 * 		user entity to map.
	 *
	 * @return user DTO used in notifications.
	 */
	public UserDTO notificationUserEntityToDto(final UserEntity model)
	{
		final UserDTO dto = new UserDTO();
		final UserDTO resourceUserDto = this.userTransformer.getUserCoverAndAvatar(model);
		dto.setFirstName(model.getFirstName());
		dto.setLastName(model.getLastName());
		dto.setUsername(model.getUsername());
		dto.setGender(model.getGender());
		
		dto.setAvatar(resourceUserDto.getAvatar());
		dto.setCover(resourceUserDto.getCover());
		dto.setCoverType(resourceUserDto.getCover() != null ? resourceUserDto.getCoverType() : null);
		
		return dto;
	}
	
}
