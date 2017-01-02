package com.fr.models;

/**
 * Created by djenanewail on 12/26/16.
 */
public enum FriendStatus {

    PENDING(0), CONFIRMED(1), REFUSED(2), PUBLICRELATION(-1);

    private int status;

    FriendStatus(int status) {
        this.status = status;
    }

    public int getValue() {
        return this.status;
    }
}
