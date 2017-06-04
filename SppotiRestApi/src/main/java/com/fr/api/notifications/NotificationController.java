package com.fr.api.notifications;

import com.fr.commons.dto.notification.NotificationDTO;
import com.fr.commons.dto.notification.NotificationListDTO;
import com.fr.commons.dto.security.AccountUserDetails;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.service.NotificationControllerService;
import com.fr.versionning.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
		
		final int userId = ((AccountUserDetails) authentication.getPrincipal()).getUuid();
		
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
	ResponseEntity<Void> openNotification(@PathVariable final int notifId,
										  @RequestBody final NotificationDTO notificationDTO,
										  final Authentication authentication)
	{
		
		final Long connectedUserId = ((AccountUserDetails) authentication.getPrincipal()).getId();
		
		if (notificationDTO.getStatus() == null) {
			throw new BusinessGlobalException("Notification status missing");
		}
		
		if (!notificationDTO.getStatus().equals(GlobalAppStatusEnum.UNREAD) &&
				!notificationDTO.getStatus().equals(GlobalAppStatusEnum.READ)) {
			throw new BusinessGlobalException("Status not accepted.");
		}
		
		this.notificationControllerService.switchNotificationStatus(notifId, connectedUserId, notificationDTO);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
