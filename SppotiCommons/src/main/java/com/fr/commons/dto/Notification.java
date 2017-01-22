package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by: Wail DJENANE on Oct 17, 2016
 */
@JsonInclude(Include.NON_ABSENT)
public class Notification {

    // Notif informations
    private String picture;
    private String dateTime;
    private String notifFromFirstName;
    private String notifFromLastName;

    // Notif type
    private boolean isTag;
    private boolean isShare;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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
