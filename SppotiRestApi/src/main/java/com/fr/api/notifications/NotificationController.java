package com.fr.api.notifications;

import com.fr.commons.dto.notification.NotificationListDTO;
import com.fr.service.NotificationControllerService;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
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
class NotificationController {

    private Logger LOGGER = Logger.getLogger(NotificationController.class);

    private NotificationControllerService notificationControllerService;

    @Autowired
    void setNotificationControllerService(NotificationControllerService notificationControllerService) {
        this.notificationControllerService = notificationControllerService;
    }

    /**
     * @param userId user id.
     * @param page   number.
     * @return all unread user notifications.
     */
    @GetMapping("/{page}")
    ResponseEntity<NotificationListDTO> getAllUserNotifications(@PathVariable int userId, @PathVariable int page) {

        NotificationListDTO notificationListDTO = notificationControllerService.getAllReceivedNotifications(userId, page);

        LOGGER.info("All notifications has been returned to user id" + userId);
        return new ResponseEntity<>(notificationListDTO, HttpStatus.OK);
    }

    /**
     * @param notifId notif id.
     * @return 200 http status if notif were updated, 404 http status if notif not found, 500 http status otherwise.
     */
    @PutMapping("/{notifId}")
    ResponseEntity<Void> openNotification(@PathVariable int notifId, Authentication authentication) {

        Long connectedUserId = ((AccountUserDetails) authentication.getPrincipal()).getId();

        notificationControllerService.openNotification(notifId, connectedUserId);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
