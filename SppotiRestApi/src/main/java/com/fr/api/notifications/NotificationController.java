package com.fr.api.notifications;

import com.fr.commons.dto.notification.NotificationResponseDTO;
import com.fr.service.NotificationControllerService;
import com.fr.security.AccountUserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

/**
 * Created by djenanewail on 2/9/17.
 */
@RestController
@RequestMapping("/notification/{userId}")
public class NotificationController {

    private Logger LOGGER = Logger.getLogger(NotificationController.class);

    private NotificationControllerService notificationControllerService;

    @Autowired
    public void setNotificationControllerService(NotificationControllerService notificationControllerService) {
        this.notificationControllerService = notificationControllerService;
    }

    /**
     * @param userId user id.
     * @param page number.
     * @return all unread user notifications.
     */
    @GetMapping("/{page}")
    public ResponseEntity<NotificationResponseDTO> getAllUserNotifications(@PathVariable int userId, @PathVariable int page) {

        NotificationResponseDTO notificationResponseDTO = notificationControllerService.getAllReceivedNotifications(userId, page);

        if (notificationResponseDTO.getNotifCounter() == 0) {
            LOGGER.info("Empty notifications list for the user id: " + userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        LOGGER.info("All notifications has been returned to user id" + userId);
        return new ResponseEntity<>(notificationResponseDTO, HttpStatus.OK);
    }

    /**
     * @param notifId notif id.
     * @return 200 http status if notif were updated, 404 http status if notif not found, 500 http status otherwise.
     */
    @PutMapping("/{notifId}")
    public ResponseEntity<Void> openNotification(@PathVariable int notifId, Authentication authentication) {

        Long connectedUserId = ((AccountUserDetails) authentication.getPrincipal()).getId();

        try {
            notificationControllerService.openNotification(notifId, connectedUserId);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Can not update notif open status! (" + notifId + ")", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
