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
	
	/**
	 * Init class transformers.
	 */
	@Autowired
	public NotificationTransformerImpl(final TeamTransformerImpl teamTransformer,
									   final UserTransformerImpl userTransformer,
									   final SppotiTransformerImpl sppotiTransformer,
									   final PostTransformer postTransformer,
									   final CommentTransformer commentTransformer)
	{
		this.teamTransformer = teamTransformer;
		this.userTransformer = userTransformer;
		this.sppotiTransformer = sppotiTransformer;
		this.postTransformer = postTransformer;
		this.commentTransformer = commentTransformer;
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
			notificationDTO.setFrom(notificationUserEntityToDto(notification.getFrom()));
		}
		if (notification.getTo() != null) {
			notificationDTO.setTo(notificationUserEntityToDto(notification.getTo()));
		}
		notificationDTO.setNotificationType(notification.getNotificationType().getNotifType());
		notificationDTO.setStatus(notification.getStatus());
		
		//POST
		final Optional<PostEntity> optionalPost = Optional.ofNullable(notification.getPost());
		optionalPost.ifPresent(t -> notificationDTO.setPost(this.postTransformer.modelToDto(notification.getPost())));
		
		//COMMENT
		final Optional<CommentEntity> optionalComment = Optional.ofNullable(notification.getComment());
		optionalComment.ifPresent(
				t -> notificationDTO.setComment(this.commentTransformer.modelToDto(notification.getComment())));
		
		//TEAM
		final Optional<TeamEntity> optionalTeam = Optional.ofNullable(notification.getTeam());
		optionalTeam.ifPresent(t -> notificationDTO.setTeam(this.teamTransformer.modelToDto(notification.getTeam())));
		
		//SPPOTI
		final Optional<SppotiEntity> optionalSppoti = Optional.ofNullable(notification.getSppoti());
		optionalSppoti
				.ifPresent(t -> notificationDTO.setSppoti(this.sppotiTransformer.modelToDto(notification.getSppoti())));
		
		
		return notificationDTO;
	}
	
	/**
	 * @param userEntity
	 * 		user entity to map.
	 *
	 * @return user DTO used in notifications.
	 */
	public UserDTO notificationUserEntityToDto(final UserEntity userEntity)
	{
		final UserDTO userDTO = new UserDTO();
		final UserDTO resourceUserDto = this.userTransformer.getUserCoverAndAvatar(userEntity);
		userDTO.setFirstName(userEntity.getFirstName());
		userDTO.setLastName(userEntity.getLastName());
		userDTO.setEmail(userDTO.getEmail());
		userDTO.setUsername(userEntity.getUsername());
		
		userDTO.setAvatar(resourceUserDto.getAvatar());
		userDTO.setCover(resourceUserDto.getCover());
		userDTO.setCoverType(resourceUserDto.getCover() != null ? resourceUserDto.getCoverType() : null);
		
		//        userDTO.setBirthDate(userEntity.getDateBorn());
		
		return userDTO;
	}
	
}
