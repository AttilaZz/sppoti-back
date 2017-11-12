package com.fr.impl;

import com.fr.BusinessTestConfiguration;
import com.fr.commons.enumeration.notification.NotificationObjectType;
import com.fr.commons.enumeration.notification.NotificationTypeEnum;
import com.fr.entities.*;
import com.fr.repositories.UserRepository;
import com.fr.transformers.NotificationTransformer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by djenanewail on 9/16/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BusinessTestConfiguration.class)
public class TestNotificationBusinessService
{
	@Mock
	NotificationTransformer notificationTransformer;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	SimpMessagingTemplate simpMessagingTemplate;
	
	private static final NotificationTypeEnum TEST_NOTIFICATION_TYPE = NotificationTypeEnum.FRIEND_REQUEST_ACCEPTED;
	
	@Spy
	@InjectMocks
	private NotificationBusinessServiceImpl notificationBusinessService;
	
	@Test
	public void testBuildNotificationEntityWith_FRIENDSHIP_As_NotificationObject_And_DataToSend_Empty() {
		
		final NotificationEntity notification = this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.FRIENDSHIP);
		
		Assert.assertNotNull(notification);
	}
	
	/**
	 * RATING
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBuildNotificationEntityWith_RATING_As_NotificationObject_And_DataToSend_Empty() {
		
		this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.RATING);
	}
	
	@Test
	public void shouldWorkFine_When_ArgumentPositionAreRespected_And_NotificationObject_Is_RATING() {
		
		final NotificationEntity notification = this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.RATING, new SppotiEntity(), new RatingEntity());
		
		Assert.assertNotNull(notification);
		Assert.assertNotNull(notification.getRating());
		Assert.assertNotNull(notification.getSppoti());
		
		Assert.assertNull(notification.getComment());
		Assert.assertNull(notification.getTeam());
		Assert.assertNull(notification.getPost());
		Assert.assertNull(notification.getScore());
	}
	
	/**
	 * COMMENT
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBuildNotificationEntityWith_COMMENT_As_NotificationObject_And_DataToSend_Empty() {
		
		this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.COMMENT);
	}
	
	@Test
	public void shouldWorkFine_When_ArgumentPositionAreRespected_And_NotificationObject_Is_COMMENT() {
		
		final NotificationEntity notification = this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.COMMENT, new PostEntity(), new CommentEntity());
		
		Assert.assertNotNull(notification);
		Assert.assertNotNull(notification.getPost());
		Assert.assertNotNull(notification.getComment());
		
		Assert.assertNull(notification.getSppoti());
		Assert.assertNull(notification.getTeam());
		Assert.assertNull(notification.getRating());
		Assert.assertNull(notification.getScore());
	}
	
	/**
	 * POST
	 */
	@Test(expected = IllegalArgumentException.class)
	public void shouldReturn_EntityNotFoundException_When_ArgumentPositionAreNotRespected_And_NotificationObject_Is_POST() {
		
		this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.POST, new PostEntity(), new TeamEntity());
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testBuildNotificationEntityWith_POST_As_NotificationObject_And_DataToSend_Empty() {
		
		this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.POST);
	}
	
	@Test(expected = ClassCastException.class)
	public void shouldThrow_ClassCastException_When_ArgumentPositionAreNotRespected_And_NotificationObject_Is_POST() {
		
		this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.POST, new TeamEntity());
		
	}
	
	@Test
	public void shouldWorkFine_When_ArgumentPositionAreRespected_And_NotificationObject_Is_POST() {
		
		final NotificationEntity notification = this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.POST, new PostEntity());
		
		Assert.assertNotNull(notification);
		Assert.assertNotNull(notification.getPost());
		
		Assert.assertNull(notification.getSppoti());
		Assert.assertNull(notification.getTeam());
		Assert.assertNull(notification.getRating());
		Assert.assertNull(notification.getComment());
		Assert.assertNull(notification.getScore());
	}
	
	/**
	 * TEAM
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBuildNotificationEntityWith_TEAM_As_NotificationObject_And_DataToSend_Empty() {
		
		this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.TEAM);
	}
	
	@Test(expected = ClassCastException.class)
	public void shouldThrow_ClassCastException_When_ArgumentPositionAreNotRespected_And_NotificationObject_Is_TEAM() {
		
		this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.TEAM, new PostEntity());
		
	}
	
	@Test
	public void shouldWorkFine_When_ArgumentPositionAreRespected_And_NotificationObject_Is_TEAM() {
		
		final NotificationEntity notification = this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.TEAM, new TeamEntity());
		
		Assert.assertNotNull(notification);
		Assert.assertNotNull(notification.getTeam());
		
		Assert.assertNull(notification.getSppoti());
		Assert.assertNull(notification.getPost());
		Assert.assertNull(notification.getRating());
		Assert.assertNull(notification.getComment());
		Assert.assertNull(notification.getScore());
	}
	
	/**
	 * SPPOTI
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBuildNotificationEntityWith_SPPOTI_As_NotificationObject_And_DataToSend_Empty() {
		
		this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.SPPOTI);
	}
	
	@Test(expected = ClassCastException.class)
	public void shouldThrow_ClassCastException_When_ArgumentPositionAreNotRespected_And_NotificationObject_Is_SPPOTI() {
		
		this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.SPPOTI, null, new UserEntity());
		
	}
	
	/**
	 * SCORE
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBuildNotificationEntityWith_SCORE_As_NotificationObject_And_DataToSend_Empty() {
		
		this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.SCORE);
	}
	
	@Test(expected = ClassCastException.class)
	public void shouldThrow_ClassCastException_When_ArgumentPositionAreNotRespected_And_NotificationObject_Is_SCORE() {
		
		this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.SCORE, new UserEntity(), new PostEntity(), new TeamEntity());
		
	}
	
	@Test
	public void shouldWorkFine_When_ArgumentPositionAreRespected_And_NotificationObject_Is_SCORE() {
		
		final NotificationEntity notification = this.notificationBusinessService
				.buildNotificationEntity(TEST_NOTIFICATION_TYPE, new UserEntity(), new UserEntity(),
						NotificationObjectType.SCORE, new TeamEntity(), new SppotiEntity(), new ScoreEntity());
		
		Assert.assertNotNull(notification);
		Assert.assertNotNull(notification.getScore());
		Assert.assertNotNull(notification.getSppoti());
		Assert.assertNotNull(notification.getTeam());
		
		Assert.assertNull(notification.getPost());
		Assert.assertNull(notification.getRating());
		Assert.assertNull(notification.getComment());
	}
}
