package com.fr.models;

/**
 * Created by djenanewail on 12/26/16.
 */
public enum FriendStatus {

    PENDING(0L), CONFIRMED(1L), REFUSED(2L), PUBLICRELATION(-1L);

    private Long status;

    FriendStatus(Long status) {
        this.status = status;
    }

    public Long getValue() {
        return this.status;
    }
}
