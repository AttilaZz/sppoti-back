package com.fr.models;

/**
 * Created by djenanewail on 2/11/17.
 */
public enum NotificationType {

    //FRIEND
    FRIEND_REQUEST_SENT(51),
    FRIEND_REQUEST_ACCEPTED(52),
    FRIEND_REQUEST_REFUSED(53),

    //COMMENT
    X_COMMENTED_ON_YOUR_POST(21),
    X_LIKED_YOUR_COMMENT(22),
    X_TAGGED_YOU_IN_A_COMMENT(22);

    private int notifType;

    NotificationType(int notifType) {
        this.notifType = notifType;
    }

    public int getNotifType() {
        return notifType;
    }
}
