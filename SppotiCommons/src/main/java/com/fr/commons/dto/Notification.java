package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Date;

/**
 * Created by: Wail DJENANE on Oct 17, 2016
 */
@JsonInclude(Include.NON_ABSENT)
public class Notification {

    // Notif informations
    private String picture;
    private Date dateTime;
    private String notifFromFirstName;
    private String notifFromLastName;
    private String notifMessage;
    private int notifType;

    // Notif type
    private boolean isTag;
    private boolean isShare;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getNotifType() {
        return notifType;
    }

    public void setNotifType(int notifType) {
        this.notifType = notifType;
    }

    public String getNotifMessage() {
        return notifMessage;
    }

    public void setNotifMessage(String notifMessage) {
        this.notifMessage = notifMessage;
    }

    public String getNotifFromFirstName() {
        return notifFromFirstName;
    }

    public void setNotifFromFirstName(String notifFromFirstName) {
        this.notifFromFirstName = notifFromFirstName;
    }

    public String getNotifFromLastName() {
        return notifFromLastName;
    }

    public void setNotifFromLastName(String notifFromLastName) {
        this.notifFromLastName = notifFromLastName;
    }

    public boolean isTag() {
        return isTag;
    }

    public void setTag(boolean isTag) {
        this.isTag = isTag;
    }

    public boolean isShare() {
        return isShare;
    }

    public void setShare(boolean isShare) {
        this.isShare = isShare;
    }

}
