/**
 *
 */
package com.fr.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by: Wail DJENANE on Jul 3, 2016
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendResponse {

    private List<User> pendingList;
    private List<User> refusedList;
    private List<User> confirmedList;

    public List<User> getPendingList() {
        return pendingList;
    }

    public void setPendingList(List<User> pendingList) {
        this.pendingList = pendingList;
    }

    public List<User> getRefusedList() {
        return refusedList;
    }

    public void setRefusedList(List<User> refusedList) {
        this.refusedList = refusedList;
    }

    public List<User> getConfirmedList() {
        return confirmedList;
    }

    public void setConfirmedList(List<User> confirmedList) {
        this.confirmedList = confirmedList;
    }
}
