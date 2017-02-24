package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by: Wail DJENANE on Jul 3, 2016
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendResponseDTO extends AbstractCommonDTO{

    private List<UserDTO> pendingList;
    private List<UserDTO> refusedList;
    private List<UserDTO> confirmedList;

    public List<UserDTO> getPendingList() {
        return pendingList;
    }

    public void setPendingList(List<UserDTO> pendingList) {
        this.pendingList = pendingList;
    }

    public List<UserDTO> getRefusedList() {
        return refusedList;
    }

    public void setRefusedList(List<UserDTO> refusedList) {
        this.refusedList = refusedList;
    }

    public List<UserDTO> getConfirmedList() {
        return confirmedList;
    }

    public void setConfirmedList(List<UserDTO> confirmedList) {
        this.confirmedList = confirmedList;
    }
}
