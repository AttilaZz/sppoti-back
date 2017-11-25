package com.fr.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fr.commons.dto.FirebaseNotificationDTO;
import com.fr.commons.dto.notification.NotificationDTO;
import com.fr.commons.dto.notification.NotificationListDTO;
import com.fr.commons.enumeration.notification.NotificationObjectType;
import com.fr.commons.enumeration.notification.NotificationStatus;
import com.fr.commons.enumeration.notification.NotificationTypeEnum;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.commons.exception.NotAdminException;
import com.fr.entities.*;
import com.fr.impl.notification.AndroidPushNotificationsService;
import com.fr.repositories.FirebaseRegistrationRepository;
import com.fr.repositories.UserRepository;
import com.fr.service.NotificationBusinessService;
import com.fr.transformers.NotificationTransformer;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 2/11/17.
 */
@Component
public class NotificationBusinessServiceImpl extends CommonControllerServiceImpl implements NotificationBusinessService
{
	private final Logger LOGGER = LoggerFactory.getLogger(NotificationBusinessServiceImpl.class);
	
	@Value("${key.notificationsPerPage}")
	private int notificationSize;
	
	@Value("${spring.app.firebase.activated}")
	protected Boolean isFirebaseActivated;
	
	@Autowired
	private NotificationTransformer notificationTransformer;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private AndroidPushNotificationsService androidPushNotificationsService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FirebaseRegistrationRepository firebaseRegistrationRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NotificationListDTO getAllReceivedNotifications(final String userId, final int page)
	{
		
		final Pageable pageable = new PageRequest(page, this.notificationSize, Sort.Direction.DESC, "creationDate");
		
		final List<NotificationEntity> notifications = this.notificationRepository.findByToUuid(userId, pageable);
		
		final NotificationListDTO notificationListDTO = new NotificationListDTO();
		
		notificationListDTO.setNotifications(notifications.stream().map(n -> {
			n.setConnectedUserId(getConnectedUserId());
			return this.notificationTransformer.modelToDto(n);
		}).collect(Collectors.toList()));
		
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
											   final NotificationObjectType notificationObjectType,
											   final NotificationTypeEnum notificationTypeEnum,
											   final Object... dataToSendInNotification)
	{
		this.LOGGER.info("Received notification data {}", Arrays.toString(dataToSendInNotification));
		
		if (dataToSendInNotification.length == 0 && notificationObjectType != NotificationObjectType.FRIENDSHIP) {
			throw new BusinessGlobalException("At least one object must be added to send a notification");
		}
		
		if (sender == null) {
			//			throw new EntityNotFoundException("Sender identity is required to send notification");
			this.LOGGER.warn("Sender identity is required to send notification, sender is {}", getConnectedUser());
		}
		if (userTo == null) {
			throw new BusinessGlobalException("Notification receivers must be added to notification ");
		}
		if (notificationObjectType == null) {
			throw new BusinessGlobalException("Notification context must be specified, ex: SPPOTI, TEAM, ect..");
		}
		if (notificationTypeEnum == null) {
			throw new BusinessGlobalException("Notification type must be specified");
		}
		
		final NotificationEntity notification = buildNotificationEntity(notificationTypeEnum, sender, userTo,
				notificationObjectType, dataToSendInNotification);
		
		notification.setConnectedUserId(getConnectedUserId());
		final NotificationDTO notificationDTO = this.notificationTransformer.modelToDto(notification);
		sendNotificationToSubscribedUsers(userTo.getEmail(), notificationDTO);
		
		this.notificationRepository.saveAndFlush(notification);
	}
	
	private void sendNotificationToSubscribedUsers(final String email, final NotificationDTO notificationDTO)
	{
		
		if (this.isFirebaseActivated) {
			final UserEntity userToReceiveNotification = this.userRepository
					.getByEmailAndDeletedFalseAndConfirmedTrue(email);
			final List<FirebaseRegistrationEntity> firebaseRegistrationEntities = userToReceiveNotification
					.getFirebaseRegistrationKeys();
			this.LOGGER.info("Sending firebase notification to {}, having the following registration ids {}", email,
					userToReceiveNotification.toString());
			
			firebaseRegistrationEntities.forEach(r -> {
				final JSONObject data = buildJsonToSendViaFirebase(r.getRegistrationKey(), notificationDTO);
				
				final HttpEntity<String> request = new HttpEntity<>(data.toString());
				final CompletableFuture<String> pushNotification = this.androidPushNotificationsService.send(request);
				CompletableFuture.allOf(pushNotification).join();
				
				try {
					final String notificationKey = pushNotification.get();
					this.LOGGER.info("Notification {} has been fired successfully, under notification_key {}", data,
							notificationKey);
					updateFirebaseRegistration(email, notificationKey);
				} catch (final InterruptedException | ExecutionException e) {
					throw new BusinessGlobalException("Failed to send notification to firebase {}", e);
				}
			});
		}
	}
	
	private JSONObject buildJsonToSendViaFirebase(final String tokenID, final NotificationDTO dto)
	{
		final ObjectMapper mapper = new ObjectMapper();
		
		final JSONObject data = new JSONObject();
		try {
			final JSONObject body = new JSONObject();
			body.put("to", tokenID);
			body.put("notification", mapper.writeValueAsString(new FirebaseNotificationDTO(dto)));
			body.put("data", mapper.writeValueAsString(dto));
			return data;
		} catch (final JSONException | JsonProcessingException e) {
			throw new BusinessGlobalException("Cannot serialize firebase notification object: {}", e);
		}
	}
	
	@Transactional
	private void updateFirebaseRegistration(final String email, final String notificationKey) {
		final FirebaseRegistrationEntity entityToUpdate = this.firebaseRegistrationRepository
				.findByRegistrationKey(email);
		if (Objects.nonNull(entityToUpdate)) {
			entityToUpdate.setRegistrationKey(notificationKey);
			this.firebaseRegistrationRepository.save(entityToUpdate);
		}
	}
	
	NotificationEntity buildNotificationEntity(final NotificationTypeEnum notificationType, final UserEntity sender,
											   final UserEntity userTo, final NotificationObjectType objectType,
											   final Object... objectToSend)
	{
		final NotificationEntity notification = new NotificationEntity();
		notification.setNotificationType(notificationType);
		notification.setFrom(sender);
		notification.setTo(userTo);
		
		final Long connectedUser = sender.getId();
		final SppotiEntity sppoti;
		final TeamEntity team;
		final CommentEntity comment;
		final PostEntity post;
		
		switch (objectType) {
			case POST:
				//post required(2)
				if (objectToSend.length != 1) {
					throw new IllegalArgumentException("Post Object is missing");
				}
				
				if (objectToSend[0] == null) {
					throw new EntityNotFoundException("POST is missing");
				}
				if (!(objectToSend[0] instanceof PostEntity)) {
					throw new ClassCastException("POST-ENTITY is expected");
				}
				
				post = (PostEntity) objectToSend[0];
				post.setConnectedUserId(connectedUser);
				notification.setPost(post);
				
				break;
			case TEAM:
				//Team and sppoti needed, but only team is required(0, 1)
				if (objectToSend.length < 1) {
					throw new IllegalArgumentException("TEAM Object is missing");
				}
				
				if (objectToSend[0] == null) {
					throw new EntityNotFoundException("TEAM is missing");
				}
				if (!(objectToSend[0] instanceof TeamEntity)) {
					throw new ClassCastException("TEAM-ENTITY is expected");
				}
				
				team = (TeamEntity) objectToSend[0];
				team.setConnectedUserId(connectedUser);
				notification.setTeam(team);
				
				if (objectToSend.length == 2) {
					if (!(objectToSend[1] instanceof SppotiEntity)) {
						throw new ClassCastException("SPPOTI-ENTITY is expected");
					}
					sppoti = (SppotiEntity) objectToSend[1];
					sppoti.setConnectedUserId(connectedUser);
					notification.setSppoti(sppoti);
				}
				
				break;
			case SPPOTI:
				//Sppoti required
				if (objectToSend.length < 1) {
					throw new IllegalArgumentException("SPPOTI Object is missing");
				}
				
				if (objectToSend[0] == null) {
					throw new BusinessGlobalException("SPPOTI is missing");
				}
				
				if (!(objectToSend[0] instanceof SppotiEntity)) {
					throw new ClassCastException("SPPOTI-ENTITY is expected");
				}
				
				sppoti = (SppotiEntity) objectToSend[0];
				sppoti.setConnectedUserId(connectedUser);
				notification.setSppoti(sppoti);
				
				break;
			case SCORE:
				//Sppoti, Team and score are required(0, 1, 4)
				if (objectToSend.length < 3) {
					throw new IllegalArgumentException("(TEAM or SPPOTI or SCORE) Object is missing");
				}
				
				//				team
				if (objectToSend[0] == null) {
					throw new EntityNotFoundException("TEAM is missing");
				}
				if (!(objectToSend[0] instanceof TeamEntity)) {
					throw new ClassCastException("TEAM-ENTITY is expected");
				}
				team = (TeamEntity) objectToSend[0];
				team.setConnectedUserId(connectedUser);
				notification.setTeam(team);
				
				//				sppoti
				if (objectToSend[1] == null) {
					throw new BusinessGlobalException("SPPOTI is missing");
				}
				if (!(objectToSend[1] instanceof SppotiEntity)) {
					throw new ClassCastException("SPPOTI-ENTITY is expected");
				}
				sppoti = (SppotiEntity) objectToSend[1];
				sppoti.setConnectedUserId(connectedUser);
				notification.setSppoti(sppoti);
				
				//				score
				if (objectToSend[2] == null) {
					throw new BusinessGlobalException(
							"SCORE is missing OR the passed object is not an instance of SCORE-ENTITY");
				}
				if (!(objectToSend[2] instanceof ScoreEntity)) {
					throw new ClassCastException("SCORE-ENTITY is expected");
				}
				final ScoreEntity score = (ScoreEntity) objectToSend[2];
				score.setConnectedUserId(connectedUser);
				notification.setScore(score);
				
				break;
			case COMMENT:
				//POSt and COMMENT are required (1, 3)
				if (objectToSend.length < 2) {
					throw new IllegalArgumentException("(COMMENT or POST) Object is missing");
				}
				
				if (objectToSend[0] == null || !(objectToSend[0] instanceof PostEntity)) {
					throw new BusinessGlobalException(
							"POST is missing OR the passed object is not an instance of SPPOTI-ENTITY");
				}
				post = (PostEntity) objectToSend[0];
				post.setConnectedUserId(connectedUser);
				notification.setPost(post);
				
				if (objectToSend[1] == null || !(objectToSend[1] instanceof CommentEntity)) {
					throw new BusinessGlobalException(
							"COMMENT is missing OR the passed object is not an instance of COMMENT-ENTITY");
				}
				comment = (CommentEntity) objectToSend[1];
				comment.setConnectedUserId(connectedUser);
				notification.setComment(comment);
				
				break;
			case RATING:
				//Sppoti and rating are required (1, 5)
				if (objectToSend.length < 2) {
					throw new IllegalArgumentException("(SPPOTI or RATING) Object is missing");
				}
				
				if (objectToSend[0] == null || !(objectToSend[0] instanceof SppotiEntity)) {
					throw new BusinessGlobalException(
							"SPPOTI is missing OR the passed object is not an instance of SPPOTI-ENTITY");
				}
				sppoti = (SppotiEntity) objectToSend[0];
				sppoti.setConnectedUserId(connectedUser);
				notification.setSppoti(sppoti);
				
				if (objectToSend[1] == null || !(objectToSend[1] instanceof RatingEntity)) {
					throw new BusinessGlobalException(
							"RATING is missing OR the passed object is not an instance of RATING-ENTITY");
				}
				final RatingEntity rating = (RatingEntity) objectToSend[1];
				rating.setConnectedUserId(connectedUser);
				notification.setRating(rating);
				
				break;
			case FRIENDSHIP:
				//No param is required
				break;
			case LIKE:
				//POST or COMMENT are required
				if (objectToSend.length < 4) {
					throw new IllegalArgumentException("(COMMENT or POST) Object is missing");
				}
				
				boolean hasComment = true;
				boolean hasPost = true;
				
				if (objectToSend[2] == null || !(objectToSend[2] instanceof PostEntity)) {
					hasPost = false;
				}
				if (objectToSend[3] == null || !(objectToSend[3] instanceof CommentEntity)) {
					hasComment = false;
				}
				if (!hasComment && !hasPost) {
					throw new BusinessGlobalException("At least COMMENT or POST are needed to send like notification");
				}
				
				if (hasComment) {
					comment = (CommentEntity) objectToSend[3];
					comment.setConnectedUserId(connectedUser);
					notification.setComment(comment);
				} else {
					post = (PostEntity) objectToSend[2];
					post.setConnectedUserId(connectedUser);
					notification.setPost(post);
				}
				
				break;
			default:
				break;
		}
		
		return notification;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void checkForTagNotification(final PostEntity postEntity, final CommentEntity commentEntity)
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
