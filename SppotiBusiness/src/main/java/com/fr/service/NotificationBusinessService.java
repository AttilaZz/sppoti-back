package com.fr.service;

import com.fr.commons.dto.notification.NotificationDTO;
import com.fr.commons.dto.notification.NotificationListDTO;

import java.util.List;

/**
 * Created by djenanewail on 2/11/17.
 */
public interface NotificationBusinessService extends AbstractBusinessService
{
	
	/**
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return All received notifications for a given user id
	 */
    NotificationListDTO getAllReceivedNotifications(String userId, int page);

    /**
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return all sent notification by a given user id
	 */
	List<NotificationDTO> getAllSentNotifications(int userId, int page);
	
	/**
	 * Update open status to true
	 *
	 * @param notificationDTO
	 * 		notif status.
	 */
	void switchNotificationStatus(NotificationDTO notificationDTO);
}
