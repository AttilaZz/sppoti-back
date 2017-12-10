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
import com.fr.service.FirebaseAdminService;
import com.fr.service.NotificationBusinessService;
import com.fr.transformers.NotificationTransformer;
import com.fr.transformers.UserTransformer;
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
	private AndroidPushNotificationsService androidPushNotificationsService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserTransformer userTransformer;
	@Autowired
	private FirebaseRegistrationRepository firebaseRegistrationRepository;
	@Autowired
	private FirebaseAdminService firebaseAdminService;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	private final String TOPIC = "notify";
	
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
		if (dataToSendInNotification.length == 0 && notificationObjectType != NotificationObjectType.FRIENDSHIP) {
			throw new BusinessGlobalException("At least one object must be added to send a notification");
		}
		
		if (sender == null) {
			this.LOGGER.warn("Sender identity is required to send notification, sender is {}", getConnectedUser());
			return;
		}
		if (userTo == null) {
			this.LOGGER.warn("Notification receivers must be added to notification, receiver is NULL");
			return;
		}
		if (notificationObjectType == null) {
			this.LOGGER.warn("Notification context must be specified, ex: SPPOTI, TEAM, ect..");
			return;
		}
		if (notificationTypeEnum == null) {
			throw new BusinessGlobalException("Notification type must be specified");
		}
		
		final NotificationEntity notification = buildNotificationEntity(notificationTypeEnum, sender, userTo,
				notificationObjectType, dataToSendInNotification);
		final NotificationEntity savedNotification = this.notificationRepository.save(notification);
		this.notificationRepository.flush();
		savedNotification.setConnectedUserId(getConnectedUserId());
		final NotificationDTO notificationDTO = this.notificationTransformer.modelToDto(savedNotification);
		sendNotificationToSubscribedUsers(sender, userTo.getEmail(), notificationDTO, notificationTypeEnum);
	}
	
	private void sendNotificationToSubscribedUsers(final UserEntity sender, final String email,
												   final NotificationDTO notificationDTO,
												   final NotificationTypeEnum notificationTypeEnum)
	{
		this.messagingTemplate.convertAndSendToUser(email, "/queue/" + this.TOPIC, notificationDTO);
		
		if (this.isFirebaseActivated) {
			final UserEntity userToReceiveNotification = this.userRepository
					.getByEmailAndDeletedFalseAndConfirmedTrue(email);
			final List<FirebaseRegistrationEntity> firebaseRegistrationEntities = userToReceiveNotification
					.getFirebaseRegistrationKeys();
			
			if (firebaseRegistrationEntities.stream().noneMatch(r -> Boolean.TRUE.equals(r.getDeviceConnected()))) {
				this.LOGGER.info("All devices are offline - notifications are not sent");
			}
			
			firebaseRegistrationEntities.stream().filter(r -> Boolean.TRUE.equals(r.getDeviceConnected()))
					.forEach(r -> {
						
						this.LOGGER
								.info("Sending firebase notification to {}, having the following registration ids {}",
										email, userToReceiveNotification.toString());
						
						final FirebaseNotificationDTO firebaseNotificationDTO = new FirebaseNotificationDTO(
								notificationTypeEnum, this.userTransformer.modelToDto(sender));
						
						final JSONObject data = buildJsonToSendViaFirebase(r.getRegistrationKey(), notificationDTO,
								firebaseNotificationDTO);
						
						final HttpEntity<String> request = new HttpEntity<>(data.toString());
						
						//						final String deviceUid = this.firebaseAdminService
						//								.verifyUserFirebaseToken(r.getRegistrationKey());
						
						final CompletableFuture<String> pushNotification = this.androidPushNotificationsService
								.send(request);
						CompletableFuture.allOf(pushNotification).join();
						
						try {
							final String notificationKey = pushNotification.get();
							this.LOGGER.info("Notification {} has been fired successfully, under notification_key {}",
									data, notificationKey);
							updateFirebaseRegistration(email, notificationKey);
						} catch (final InterruptedException | ExecutionException e) {
							throw new BusinessGlobalException("Failed to send notification to firebase {}", e);
						}
					});
		}
	}
	
	private JSONObject buildJsonToSendViaFirebase(final String tokenID, final NotificationDTO notificationDTO,
												  final FirebaseNotificationDTO firebaseNotificationDTO)
	{
		this.LOGGER.info("firebaseNotificationDTO to send is: {}", firebaseNotificationDTO);
		final ObjectMapper mapper = new ObjectMapper();
		try {
			final JSONObject body = new JSONObject();
			body.put("to", tokenID);
			final JSONObject notification = new JSONObject();
			notification.put("title", firebaseNotificationDTO.getTitle());
			notification.put("body", firebaseNotificationDTO.getBody());
			notification.put("sound", firebaseNotificationDTO.getSound());
			notification.put("click_action", firebaseNotificationDTO.getPluginActivity());
			body.put("notification", notification);
			final JSONObject data = new JSONObject();
			data.put("title", "");
			data.put("body", "");
			data.put("notification", mapper.writeValueAsString(notificationDTO));
			body.put("data", data);
			this.LOGGER.info("Json to send via firebase: {}", body.toString());
			return body;
		} catch (final JSONException | JsonProcessingException e) {
			throw new BusinessGlobalException("Cannot serialize firebase notification object: {}", e);
		}
	}
	
	@Transactional
	private void updateFirebaseRegistration(final String email, final String notificationKey) {
		this.firebaseRegistrationRepository.findByRegistrationKey(email).ifPresent(e -> {
			this.LOGGER.info("Update firebase registration key for user {}, new token is: {}", email, notificationKey);
			e.setRegistrationKey(notificationKey);
			this.firebaseRegistrationRepository.save(e);
		});
	}
	
	NotificationEntity buildNotificationEntity(final NotificationTypeEnum notificationType, final UserEntity sender,
											   final UserEntity userTo, final NotificationObjectType objectType,
											   final Object... objectToSend)
	{
		final NotificationEntity notification = new NotificationEntity();
		notification.setNotificationType(notificationType);
		notification.setFrom(new UserEntity(sender.getId()));
		notification.setTo(new UserEntity(userTo.getId()));
		
		final Long connectedUser = sender.getId();
		final SppotiEntity sppoti;
		final TeamEntity team;
		final CommentEntity comment;
		final PostEntity post;
		
		switch (objectType) {
			case POST:
				this.LOGGER.info("Building POST notification, with data: {}", Arrays.toString(objectToSend));
				
				if (objectToSend.length != 1) {
					throw new IllegalArgumentException("Post Object is missing");
				}
				
				if (Objects.isNull(objectToSend[0])) {
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
				this.LOGGER.info("Building TEAM notification, with data: {}", Arrays.toString(objectToSend));
				
				if (objectToSend.length < 1) {
					throw new IllegalArgumentException("TEAM Object is missing");
				}
				
				if (Objects.isNull(objectToSend[0])) {
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
				this.LOGGER.info("Building SPPOTI notification, with data: {}", Arrays.toString(objectToSend));
				
				if (objectToSend.length < 1) {
					throw new IllegalArgumentException("SPPOTI Object is missing");
				}
				
				if (Objects.isNull(objectToSend[0])) {
					throw new BusinessGlobalException("SPPOTI is missing");
				}
				
				if (!(objectToSend[0] instanceof SppotiEntity)) {
					throw new ClassCastException("SPPOTI-ENTITY is expected");
				}
				
				sppoti = (SppotiEntity) objectToSend[0];
				sppoti.setConnectedUserId(connectedUser);
				notification.setSppoti(sppoti);
				
				break;
			case CHALLENGE:
				this.LOGGER.info("Building CHALLENGE notification, with data: {}", Arrays.toString(objectToSend));
				
				if (objectToSend.length < 2) {
					throw new IllegalArgumentException("SPPOTI Object is missing");
				}
				
				if (Objects.isNull(objectToSend[0])) {
					throw new BusinessGlobalException("TEAM is missing");
				}
				
				if (!(objectToSend[0] instanceof TeamEntity)) {
					throw new ClassCastException("TEAM-ENTITY is expected");
				}
				
				team = (TeamEntity) objectToSend[0];
				team.setConnectedUserId(connectedUser);
				notification.setTeam(team);
				
				if (Objects.isNull(objectToSend[1])) {
					throw new BusinessGlobalException("SPPOTI is missing");
				}
				
				if (!(objectToSend[1] instanceof SppotiEntity)) {
					throw new ClassCastException("SPPOTI-ENTITY is expected");
				}
				
				sppoti = (SppotiEntity) objectToSend[1];
				sppoti.setConnectedUserId(connectedUser);
				notification.setSppoti(sppoti);
				
				break;
			case SCORE:
				this.LOGGER.info("Building SCORE notification, with data: {}", Arrays.toString(objectToSend));
				
				//Sppoti, Team and score are required(0, 1, 4)
				if (objectToSend.length < 3) {
					throw new IllegalArgumentException("(TEAM or SPPOTI or SCORE) Object is missing");
				}
				
				//				team
				if (Objects.isNull(objectToSend[0])) {
					throw new EntityNotFoundException("TEAM is missing");
				}
				if (!(objectToSend[0] instanceof TeamEntity)) {
					throw new ClassCastException("TEAM-ENTITY is expected");
				}
				team = (TeamEntity) objectToSend[0];
				team.setConnectedUserId(connectedUser);
				notification.setTeam(team);
				
				//				sppoti
				if (Objects.isNull(objectToSend[1])) {
					throw new BusinessGlobalException("SPPOTI is missing");
				}
				if (!(objectToSend[1] instanceof SppotiEntity)) {
					throw new ClassCastException("SPPOTI-ENTITY is expected");
				}
				sppoti = (SppotiEntity) objectToSend[1];
				sppoti.setConnectedUserId(connectedUser);
				notification.setSppoti(sppoti);
				
				//				score
				if (Objects.isNull(objectToSend[2])) {
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
				this.LOGGER.info("Building COMMENT notification, with data: {}", Arrays.toString(objectToSend));
				
				if (objectToSend.length < 2) {
					this.LOGGER.info("Sending comment notification");
					throw new IllegalArgumentException("(COMMENT or POST) Object is missing");
				}
				
				if (Objects.isNull(objectToSend[0]) || !(objectToSend[0] instanceof PostEntity)) {
					throw new BusinessGlobalException(
							"POST is missing OR the passed object is not an instance of SPPOTI-ENTITY");
				}
				post = (PostEntity) objectToSend[0];
				post.setConnectedUserId(connectedUser);
				notification.setPost(post);
				
				if (Objects.isNull(objectToSend[1]) || !(objectToSend[1] instanceof CommentEntity)) {
					throw new BusinessGlobalException(
							"COMMENT is missing OR the passed object is not an instance of COMMENT-ENTITY");
				}
				comment = (CommentEntity) objectToSend[1];
				comment.setConnectedUserId(connectedUser);
				notification.setComment(comment);
				
				break;
			case RATING:
				this.LOGGER.info("Building RATING notification, with data: {}", Arrays.toString(objectToSend));
				
				if (objectToSend.length < 2) {
					throw new IllegalArgumentException("(SPPOTI or RATING) Object is missing");
				}
				
				if (Objects.isNull(objectToSend[0]) || !(objectToSend[0] instanceof SppotiEntity)) {
					throw new BusinessGlobalException(
							"SPPOTI is missing OR the passed object is not an instance of SPPOTI-ENTITY");
				}
				sppoti = (SppotiEntity) objectToSend[0];
				sppoti.setConnectedUserId(connectedUser);
				notification.setSppoti(sppoti);
				
				if (Objects.isNull(objectToSend[1]) || !(objectToSend[1] instanceof RatingEntity)) {
					throw new BusinessGlobalException(
							"RATING is missing OR the passed object is not an instance of RATING-ENTITY");
				}
				final RatingEntity rating = (RatingEntity) objectToSend[1];
				rating.setConnectedUserId(connectedUser);
				notification.setRating(rating);
				
				break;
			case FRIENDSHIP:
				this.LOGGER.info("Building FRIENDSHIP notification, with data: {}", Arrays.toString(objectToSend));
				
				break;
			case LIKE:
				this.LOGGER.info("Building LIKE notification");
				
				if (objectToSend.length == 1 && Objects.isNull(objectToSend[0])) {
					throw new BusinessGlobalException("POSt object is needed to send like notification");
				}
				
				if (objectToSend.length == 2 && Objects.isNull(objectToSend[0]) && Objects.isNull(objectToSend[1])) {
					throw new BusinessGlobalException("Comment object is needed to send like notification");
				}
				
				if (objectToSend.length == 1 && objectToSend[0] instanceof PostEntity) {
					post = (PostEntity) objectToSend[0];
					post.setConnectedUserId(connectedUser);
					notification.setPost(post);
					this.LOGGER.info("Sending like notification for the post: {}", post.getUuid());
					
				} else if (objectToSend.length == 2 && objectToSend[0] instanceof PostEntity &&
						objectToSend[1] instanceof CommentEntity) {
					post = (PostEntity) objectToSend[0];
					post.setConnectedUserId(connectedUser);
					notification.setPost(post);
					
					comment = (CommentEntity) objectToSend[1];
					comment.setConnectedUserId(connectedUser);
					notification.setComment(comment);
					this.LOGGER.info("Sending like notification for the comment: {}", comment.getUuid());
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
		this.LOGGER.info("Check for tags in the comment text has been started ...");
		
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