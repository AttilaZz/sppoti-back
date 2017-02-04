package com.fr.models;

/**
 * Created by djenanewail on 12/26/16.
 */
public enum GlobalAppStatus {

    PUBLIC_RELATION(1), PENDING_SENT(2), PENDING(3), CONFIRMED(4), REFUSED(5);

    private int status;

    GlobalAppStatus(int status) {
        this.status = status;
    }

    public int getValue() {
        return this.status;
    }
}
