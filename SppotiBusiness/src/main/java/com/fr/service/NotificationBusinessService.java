package com.fr.service;

import com.fr.commons.dto.notification.NotificationDTO;
import com.fr.commons.dto.notification.NotificationListDTO;
import com.fr.commons.enumeration.notification.NotificationObjectType;
import com.fr.commons.enumeration.notification.NotificationTypeEnum;
import com.fr.entities.CommentEntity;
import com.fr.entities.PostEntity;
import com.fr.entities.UserEntity;

import javax.transaction.Transactional;

/**
 * Created by djenanewail on 2/11/17.
 */
public interface NotificationBusinessService extends AbstractBusinessService
{
	
	NotificationListDTO getAllReceivedNotifications(String userId, int page);
	
	void switchNotificationStatus(NotificationDTO notificationDTO);
	
	/**
	 * Send notification to a set of users.
	 *
	 * @param sender
	 * 		user who trigged the notification.
	 * @param userTo
	 * 		notification receiver.
	 * @param notificationObjectType
	 * 		notification context.
	 * @param notificationTypeEnum
	 * 		notification type.
	 * @param dataToSendInNotification
	 * 		data to add inside notification.
	 */
	void saveAndSendNotificationToUsers(UserEntity sender, UserEntity userTo,
										NotificationObjectType notificationObjectType,
										NotificationTypeEnum notificationTypeEnum, Object... dataToSendInNotification);
	
	@Transactional
	void checkForTagNotification(PostEntity postEntity, CommentEntity commentEntity);
}
