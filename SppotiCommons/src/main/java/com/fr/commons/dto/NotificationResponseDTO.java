package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by wdjenane on 14/02/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponseDTO {

    private List<NotificationDTO> notifications;
    private Integer notifCounter;

    public List<NotificationDTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
    }

    public Integer getNotifCounter() {
        return notifCounter;
    }

    public void setNotifCounter(Integer notifCounter) {
        this.notifCounter = notifCounter;
    }
}
