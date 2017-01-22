package com.fr.dto;

/**
 * Created by djenanewail on 12/26/16.
 */
public enum FriendStatus {

    PUBLIC_RELATION(1), PENDING_SENT(2), PENDING(3), CONFIRMED(4), REFUSED(5);

    private int status;

    FriendStatus(int status) {
        this.status = status;
    }

    public int getValue() {
        return this.status;
    }
}
