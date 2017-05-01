package com.fr.transformers.impl;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.notification.NotificationDTO;
import com.fr.entities.NotificationEntity;
import com.fr.entities.SppotiEntity;
import com.fr.entities.TeamEntity;
import com.fr.entities.UserEntity;
import com.fr.transformers.SppotiTransformer;
import com.fr.transformers.TeamTransformer;
import com.fr.transformers.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by djenanewail on 2/19/17.
 */
@Transactional(readOnly = true)
@Component
public class NotificationTransformer
{
	/** Team transformer. */
	private final TeamTransformer teamTransformer;
	/** USer transformer. */
	private final UserTransformer userTransformer;
	/** Sppoti transformer. */
	private final SppotiTransformer sppotiTransformer;
	
	/**
	 * Init class transformers.
	 */
	@Autowired
	public NotificationTransformer(TeamTransformerImpl teamTransformer,UserTransformerImpl userTransformer,
								   SppotiTransformerImpl sppotiTransformer)
	{
		this.teamTransformer = teamTransformer;
		this.userTransformer = userTransformer;
		this.sppotiTransformer = sppotiTransformer;
	}
	
	/**
	 * @param notification
	 * 		notification entity to map.
	 *
	 * @return NotificationEntity DTO.
	 */
	public NotificationDTO notificationEntityToDto(NotificationEntity notification)
	{
		NotificationDTO notificationDTO = new NotificationDTO();
		notificationDTO.setId(notification.getUuid());
		notificationDTO.setDatetime(notification.getCreationDate());
		notificationDTO.setFrom(notificationUserEntityToDto(notification.getFrom()));
		notificationDTO.setTo(notificationUserEntityToDto(notification.getTo()));
		notificationDTO.setNotificationType(notification.getNotificationType().getNotifType());
		notificationDTO.setOpened(notification.isOpened());
		
		Optional<TeamEntity> optionalTeam = Optional.ofNullable(notification.getTeam());
		optionalTeam.ifPresent(t -> notificationDTO.setTeam(teamTransformer.modelToDto(notification.getTeam())));
		
		Optional<SppotiEntity> optionalSppoti = Optional.ofNullable(notification.getSppoti());
		optionalSppoti
				.ifPresent(t -> notificationDTO.setSppoti(sppotiTransformer.modelToDto(notification.getSppoti())));
		
		return notificationDTO;
	}
	
	/**
	 * @param userEntity
	 * 		user entity to map.
	 *
	 * @return user DTO used in notifications.
	 */
	public UserDTO notificationUserEntityToDto(UserEntity userEntity)
	{
		UserDTO userDTO = new UserDTO(), resourceUserDto = userTransformer.getUserCoverAndAvatar(userEntity);
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
