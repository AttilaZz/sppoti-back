package com.fr.api.notifications;

import com.fr.commons.dto.notification.NotificationListDTO;
import com.fr.security.AccountUserDetails;
import com.fr.service.NotificationControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Created by djenanewail on 2/9/17.
 */
@RestController
@RequestMapping("/notification/{userId}")
class NotificationController
{
	
	/** Notification service. */
	private NotificationControllerService notificationControllerService;
	
	/** Init notif service. */
	@Autowired
	void setNotificationControllerService(final NotificationControllerService notificationControllerService)
	{
		this.notificationControllerService = notificationControllerService;
	}
	
	/**
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		number.
	 *
	 * @return all unread user notifications.
	 */
	@GetMapping("/{page}")
	ResponseEntity<NotificationListDTO> getAllUserNotifications(@PathVariable final int userId,
																@PathVariable final int page)
	{
		
		final NotificationListDTO notificationListDTO = this.notificationControllerService
				.getAllReceivedNotifications(userId, page);
		
		return new ResponseEntity<>(notificationListDTO, HttpStatus.OK);
	}
	
	/**
	 * @param notifId
	 * 		notif id.
	 *
	 * @return 200 http status if notif were updated, 404 http status if notif not found, 500 http status otherwise.
	 */
	@PutMapping("/{notifId}")
	ResponseEntity<Void> openNotification(@PathVariable final int notifId, final Authentication authentication)
	{
		
		final Long connectedUserId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		
		this.notificationControllerService.openNotification(notifId, connectedUserId);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
