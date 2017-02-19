package com.fr.rest.service;

import com.fr.commons.dto.NotificationDTO;
import com.fr.commons.dto.NotificationResponseDTO;

import java.util.List;

/**
 * Created by djenanewail on 2/11/17.
 */
public interface NotificationControllerService {

    /**
     *
     * @param userId user id.
     * @param page page number.
     * @return All received notifications for a given user id
     */
    NotificationResponseDTO getAllReceivedNotifications(int userId, int page);

    /**
     *
     * @param userId user id.
     * @param page page number.
     * @return all sent notification by a given user id
     */
    List<NotificationDTO> getAllSentNotifications(int userId, int page);

    /**
     * Update open status to true
     *
     * @param notifId notif id.
     */
    void openNotification(int notifId);
}
