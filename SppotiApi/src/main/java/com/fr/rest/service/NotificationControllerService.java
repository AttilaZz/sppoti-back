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
     * @param userId
     * @param page
     * @return All received notifications for a given user id
     */
    NotificationResponseDTO getAllReceivedNotifications(int userId, int page);

    /**
     *
     * @param userId
     * @param page
     * @return all sent notification by a given user id
     */
    List<NotificationDTO> getAllSentNotifications(int userId, int page);

    /**
     * Update open status to true
     *
     * @param notifId
     */
    void openNotification(int notifId);
}
