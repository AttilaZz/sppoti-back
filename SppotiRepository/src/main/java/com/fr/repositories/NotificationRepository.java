package com.fr.repositories;

import com.fr.commons.enumeration.notification.NotificationStatus;
import com.fr.entities.NotificationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by djenanewail on 12/13/16.
 */
@Service
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long>
{
	
	/**
	 * Get all suer notifications.
	 *
	 * @param userId
	 * 		user id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return all notifications.
	 */
	List<NotificationEntity> findByToUuid(String userId, Pageable pageable);
	
	/**
	 * Find notification by ut's id.
	 *
	 * @param notifId
	 * 		notif id.
	 *
	 * @return notification.
	 */
	NotificationEntity findByUuid(String notifId);
	
	/**
	 * Get count of all user notifications.
	 *
	 * @param userId
	 * 		user id.
	 *
	 * @return counter of all notifications.
	 */
	Integer countByToUuid(String userId);
	
	/**
	 * Get count of all notif by a status.
	 *
	 * @param userId
	 * 		user id.
	 * @param status
	 * 		notif status
	 *
	 * @return counter of all notifications by the specified status.
	 */
	Integer countByToUuidAndStatus(String userId, NotificationStatus status);
}
