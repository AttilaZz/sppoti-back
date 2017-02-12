package com.fr.rest.controllers.notifications;

import com.fr.commons.dto.NotificationDTO;
import com.fr.rest.service.NotificationControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/{page}")
    public ResponseEntity<List<NotificationDTO>> getAllUserNotifications(Authentication authentication, @PathVariable int userId, @PathVariable int page) {

        List<NotificationDTO> notifications = notificationControllerService.getAllReceivedNotifications(userId, page);

        if (notifications.isEmpty()) {
            LOGGER.info("Empty notifications list for the user id: " + userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        LOGGER.info("All notifications has been returned to user id" + userId);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

}
