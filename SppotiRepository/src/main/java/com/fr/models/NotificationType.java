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
    X_TAGGED_YOU_IN_A_COMMENT(22),

    //POST
    X_POSTED_ON_YOUR_PROFILE(11),
    X_LIKED_YOUR_POST(12),
    X_TAGGED_YOU_IN_A_POST(13),

    //TEAM
    X_INVITED_YOU_TO_JOIN_HIS_TEAM(31),
    X_ACCEPTED_YOUR_TEAM_INVITATION(32),
    X_REFUSED_YOUR_TEAM_INVITATION(33),

    //SPPOTI
    X_INVITED_YOU_TO_JOIN_HIS_SPPOTI(31),
    X_ACCEPTED_YOUR_SPPOTI_INVITATION(32),
    X_REFUSED_YOUR_SPPOTI_INVITATION(33);

    private int notifType;

    NotificationType(int notifType) {
        this.notifType = notifType;
    }

    public int getNotifType() {
        return notifType;
    }
}
