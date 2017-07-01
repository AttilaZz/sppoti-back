package com.fr.api.notifications;

import com.fr.commons.dto.notification.NotificationDTO;
import com.fr.commons.dto.notification.NotificationListDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.commons.enumeration.NotificationStatus;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.service.NotificationControllerService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by djenanewail on 2/9/17.
 */
@RestController
@RequestMapping("/notification")
@ApiVersion("1")
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
	 * @param page
	 * 		number.
	 *
	 * @return all unread user notifications.
	 */
	@GetMapping("/{page}")
	ResponseEntity<NotificationListDTO> getAllUserNotifications(@PathVariable final int page,
																final Authentication authentication)
	{
		
		final String userId = ((AccountUserDetails) authentication.getPrincipal()).getUuid();
		
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
	ResponseEntity<Void> openNotification(@PathVariable final String notifId,
										  @RequestBody final NotificationDTO notificationDTO)
	{
		if (notificationDTO.getStatus() == null) {
			throw new BusinessGlobalException("Notification status missing");
		}
		
		if (!notificationDTO.getStatus().equals(NotificationStatus.UNREAD) &&
				!notificationDTO.getStatus().equals(NotificationStatus.READ)) {
			throw new BusinessGlobalException("Status not accepted.");
		}
		
		notificationDTO.setId(notifId);
		this.notificationControllerService.switchNotificationStatus(notificationDTO);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	/**
	 * Update status of many notifications in the same time.
	 *
	 * @param notifications
	 * 		list of notifications to update.
	 *
	 * @return 202 status if everything OK.
	 */
	@PutMapping("/update/all")
	ResponseEntity<Void> openAllNotification(@RequestBody final List<NotificationDTO> notifications)
	{
		notifications.forEach(n -> {
			if (n.getStatus().equals(NotificationStatus.UNREAD) || n.getStatus().equals(NotificationStatus.READ)) {
				this.notificationControllerService.switchNotificationStatus(n);
			}
		});
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
}
