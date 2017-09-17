package com.fr.impl;

import com.fr.commons.dto.notification.NotificationDTO;
import com.fr.commons.dto.notification.NotificationListDTO;
import com.fr.commons.enumeration.notification.NotificationObjectType;
import com.fr.commons.enumeration.notification.NotificationStatus;
import com.fr.commons.enumeration.notification.NotificationTypeEnum;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.exception.NotAdminException;
import com.fr.entities.*;
import com.fr.service.NotificationBusinessService;
import com.fr.transformers.impl.NotificationTransformerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 2/11/17.
 */
@Component
public class NotificationBusinessServiceImpl extends AbstractControllerServiceImpl implements
		NotificationBusinessService
{
	private final Logger LOGGER = LoggerFactory.getLogger(NotificationBusinessServiceImpl.class);
	
	/** Notification list size. */
	@Value("${key.notificationsPerPage}")
	private int notificationSize;
	
	/** Notification transformer. */
	@Autowired
	private NotificationTransformerImpl notificationTransformer;
	
	/** Socket messaging temples. */
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NotificationListDTO getAllReceivedNotifications(final String userId, final int page)
	{
		
		final Pageable pageable = new PageRequest(page, this.notificationSize, Sort.Direction.DESC, "creationDate");
		
		List<NotificationEntity> notifications = this.notificationRepository.findByToUuid(userId, pageable);
		
		notifications = notifications.stream().map(n -> {
			NotificationEntity entity = this
					.buildNotificationEntity(n.getNotificationType(), n.getFrom(), n.getTo(), n.getTeam(),
							n.getSppoti(), n.getPost(), n.getComment());
			
			entity.setId(n.getId());
			entity.setUuid(n.getUuid());
			entity.setVersion(n.getVersion());
			entity.setNotificationType(n.getNotificationType());
			entity.setStatus(n.getStatus());
			entity.setCreationDate(n.getCreationDate());
			return entity;
		}).collect(Collectors.toList());
		
		final NotificationListDTO notificationListDTO = new NotificationListDTO();
		
		notificationListDTO.setNotifications(
				notifications.stream().map(this.notificationTransformer::modelToDto).collect(Collectors.toList()));
		
		notificationListDTO.setNotifCounter(this.notificationRepository.countByToUuid(userId));
		
		notificationListDTO.setUnreadCounter(
				this.notificationRepository.countByToUuidAndStatus(userId, NotificationStatus.UNREAD));
		
		return notificationListDTO;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void switchNotificationStatus(final NotificationDTO notificationDTO)
	{
		final Optional<NotificationEntity> notification = Optional
				.ofNullable(this.notificationRepository.findByUuid(notificationDTO.getId()));
		
		notification.ifPresent(n -> {
			if (!n.getTo().getId().equals(getConnectedUser().getId())) {
				throw new NotAdminException("This is not your notification");
			}
			
			n.setStatus(notificationDTO.getStatus());
			this.notificationRepository.save(n);
		});
		
		notification.orElseThrow(() -> new EntityNotFoundException("Notification not Found"));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void saveAndSendNotificationToUsers(final UserEntity sender, final UserEntity userTo,
											   final NotificationObjectType objectType,
											   final NotificationTypeEnum notificationTypeEnum,
											   final Object... dataToSendInNotification)
	{
		if (dataToSendInNotification.length == 0 && objectType != NotificationObjectType.FRIENDSHIP) {
			throw new BusinessGlobalException("At least one object must be added to send a notification");
		}
		
		if (sender == null) {
			//			throw new EntityNotFoundException("Sender identity is required to send notification");
			this.LOGGER.warn("Sender identity is required to send notification, sender is {}", getConnectedUser());
		}
		if (userTo == null) {
			throw new BusinessGlobalException("Notification receivers must be added to notification");
		}
		if (objectType == null) {
			throw new BusinessGlobalException("Notification context must be specified, ex: SPPOTI, TEAM, ect..");
		}
		if (notificationTypeEnum == null) {
			throw new BusinessGlobalException("Notification type must be specified");
		}
		
		this.throwExceptionIfParameterMissingInNotificationBuilder(objectType, dataToSendInNotification);
		
		final NotificationEntity notification = buildNotificationEntity(notificationTypeEnum, sender, userTo,
				dataToSendInNotification);
		
		final NotificationDTO notificationDTO = this.notificationTransformer.modelToDto(notification);
		this.messagingTemplate.convertAndSendToUser(userTo.getEmail(), "/queue/notify", notificationDTO);
		this.notificationRepository.save(notification);
	}
	
	/**
	 * Init notif entity.
	 */
	public NotificationEntity buildNotificationEntity(final NotificationTypeEnum notificationType,
													  final UserEntity sender, final UserEntity userTo,
													  final Object... objectToSend)
	{
		final NotificationEntity notification = new NotificationEntity();
		notification.setNotificationType(notificationType);
		notification.setFrom(sender);
		notification.setTo(userTo);
		
		final Long connectedUser = sender.getId();
		
		//Initialize variables
		final TeamEntity team = (TeamEntity) objectToSend[0];
		final SppotiEntity sppoti = (SppotiEntity) objectToSend[1];
		final PostEntity post = (PostEntity) objectToSend[2];
		final CommentEntity comment = (CommentEntity) objectToSend[3];
		final ScoreEntity score = (ScoreEntity) objectToSend[4];
		final RatingEntity rating = (RatingEntity) objectToSend[5];
		
		if (team != null) {
			team.setConnectedUserId(connectedUser);
			notification.setTeam(team);
		}
		
		if (sppoti != null) {
			sppoti.setConnectedUserId(connectedUser);
			notification.setSppoti(sppoti);
		}
		
		if (post != null) {
			post.setConnectedUserId(connectedUser);
			notification.setPost(post);
		}
		
		if (comment != null) {
			comment.setConnectedUserId(connectedUser);
			notification.setComment(comment);
		}
		
		if (rating != null) {
			rating.setConnectedUserId(connectedUser);
			notification.setRating(rating);
		}
		
		if (score != null) {
			score.setConnectedUserId(connectedUser);
			notification.setScore(score);
		}
		
		return notification;
	}
	
	public void throwExceptionIfParameterMissingInNotificationBuilder(final NotificationObjectType objectType,
																	  final Object... objectToSend)
	{
		switch (objectType) {
			case POST:
				//post required(2
				if (objectToSend[2] == null || !(objectToSend[2] instanceof PostEntity)) {
					throw new EntityNotFoundException(
							"POST is missing OR the passed object is not an instance of POST-ENTITY");
				}
				break;
			case TEAM:
				//Team and sppoti needed, but only team is required(0, 1)
				if (objectToSend[0] == null || !(objectToSend[0] instanceof TeamEntity)) {
					throw new EntityNotFoundException(
							"TEAM is missing OR the passed object is not an instance of COMMENT-ENTITY");
				}
				break;
			case SPPOTI:
				//Sppoti required
				if (objectToSend[1] == null || !(objectToSend[1] instanceof SppotiEntity)) {
					throw new BusinessGlobalException(
							"SPPOTI is missing OR the passed object is not an instance of SPPOTI-ENTITY");
				}
				break;
			case SCORE:
				//Sppoti, Team and score are required(0, 1, 4)
				if (objectToSend[0] == null || !(objectToSend[0] instanceof TeamEntity)) {
					throw new EntityNotFoundException("TEAM is missing");
				}
				if (objectToSend[1] == null || !(objectToSend[1] instanceof SppotiEntity)) {
					throw new BusinessGlobalException(
							"SPPOTI is missing OR the passed object is not an instance of SPPOTI-ENTITY");
				}
				if (objectToSend[4] == null || !(objectToSend[4] instanceof ScoreEntity)) {
					throw new BusinessGlobalException(
							"SCORE is missing OR the passed object is not an instance of SCORE-ENTITY");
				}
				break;
			case COMMENT:
				//Sppoti and comment are required (1, 3)
				if (objectToSend[1] == null || !(objectToSend[1] instanceof SppotiEntity)) {
					throw new BusinessGlobalException(
							"SPPOTI is missing OR the passed object is not an instance of SPPOTI-ENTITY");
				}
				if (objectToSend[3] == null || !(objectToSend[3] instanceof ScoreEntity)) {
					throw new BusinessGlobalException(
							"COMMENT is missing OR the passed object is not an instance of COMMENT-ENTITY");
				}
				break;
			case RATING:
				//Sppoti and rating are required (1, 5)
				if (objectToSend[1] == null || !(objectToSend[1] instanceof SppotiEntity)) {
					throw new BusinessGlobalException(
							"SPPOTI is missing OR the passed object is not an instance of SPPOTI-ENTITY");
				}
				if (objectToSend[5] == null || !(objectToSend[5] instanceof ScoreEntity)) {
					throw new BusinessGlobalException(
							"RATING is missing OR the passed object is not an instance of RATING-ENTITY");
				}
				break;
			case FRIENDSHIP:
				//No param is required
				break;
			case LIKE:
				//POST or COMMENT are required
				boolean hasComment = true;
				boolean hasPost = true;
				
				if (objectToSend[2] == null || !(objectToSend[2] instanceof PostEntity)) {
					hasPost = false;
				}
				if (objectToSend[3] == null || !(objectToSend[3] instanceof ScoreEntity)) {
					hasComment = false;
				}
				if (!hasComment && !hasPost) {
					throw new BusinessGlobalException("At least COMMENT or POST are needed to send like notification");
				}
				break;
			default:
				break;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void addTagNotification(final PostEntity postEntity, final CommentEntity commentEntity)
	{
		
		String content = null;
		if (postEntity != null) {
			content = postEntity.getContent();
		} else if (commentEntity != null) {
			content = commentEntity.getContent();
		}
		
		if (content != null) {
			/**
			 * All words starting with @, followed by Letter or accented Letter.
			 * and finishing with Letter, Number or Accented letter.
			 */
			final String patternString1 = "(\\$+)([a-z|A-Z|\\p{javaLetter}][a-z\\d|A-Z\\d|\\p{javaLetter}]*)";
			
			final Pattern pattern = Pattern.compile(patternString1);
			final Matcher matcher = pattern.matcher(content);
			
			/**
			 *  clean tags from @.
			 */
			final List<String> tags = new ArrayList<>();
			while (matcher.find()) {
				String s = matcher.group().trim();
				s = s.replaceAll("[$]", "");
				tags.add(s);
			}
			
			/**
			 * Process each tag.
			 */
			for (final String username : tags) {
				final UserEntity userToNotify;
				
				userToNotify = this.userRepository.getByUsernameAndDeletedFalseAndConfirmedTrue(username);
				
				if (userToNotify != null) {
					if (commentEntity != null) {
						this.saveAndSendNotificationToUsers(commentEntity.getUser(), userToNotify,
								NotificationObjectType.COMMENT, NotificationTypeEnum.X_TAGGED_YOU_IN_A_COMMENT,
								commentEntity.getPost(), commentEntity);
					} else {
						this.saveAndSendNotificationToUsers(postEntity.getUser(), userToNotify,
								NotificationObjectType.POST, NotificationTypeEnum.X_TAGGED_YOU_IN_A_POST, postEntity);
					}
					
				}
			}
		}
		
	}
}
