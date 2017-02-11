package com.fr.models;

/**
 * Created by djenanewail on 2/11/17.
 */
public enum NotificationType {

    //FRIEND
    FRIEND_REQUEST_SENT(51),
    FRIEND_REQUEST_ACCEPTED(52),
    FRIEND_REQUEST_REFUSED(53);

    private int notifType;

    NotificationType(int notifType) {
        this.notifType = notifType;
    }

}
