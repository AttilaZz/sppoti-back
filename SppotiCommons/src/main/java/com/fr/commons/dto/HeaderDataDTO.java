package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.commons.dto.notification.NotificationDTO;

import java.util.List;

/**
 * Created by: Wail DJENANE on Oct 17, 2016
 */
@JsonInclude(value = Include.NON_DEFAULT)
public class HeaderDataDTO extends AbstractCommonDTO{

    private String firstName;
    private String lastName;

    private Long avatarId;
    private String avatar;

    private Long coverId;
    private String cover;
    private int coverType;

    private String username;
    private int nbNotif;

    private int pendingFriendRequestCount;

    private int confirmedFriendRequestCount;

    private List<NotificationDTO> notifList;//post-like like + content share + tag
    private int notifListCount;

    public HeaderDataDTO() {
        super();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getCoverId() {
        return coverId;
    }

    public void setCoverId(Long coverId) {
        this.coverId = coverId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNbNotif() {
        return nbNotif;
    }

    public void setNbNotif(int nbNotif) {
        this.nbNotif = nbNotif;
    }

    public int getCoverType() {
        return coverType;
    }

    public void setCoverType(int coverType) {
        this.coverType = coverType;
    }

    public int getPendingFriendRequestCount() {
        return pendingFriendRequestCount;
    }

    public void setPendingFriendRequestCount(int pendingFriendRequestCount) {
        this.pendingFriendRequestCount = pendingFriendRequestCount;
    }
    public int getConfirmedFriendRequestCount() {
        return confirmedFriendRequestCount;
    }

    public void setConfirmedFriendRequestCount(int confirmedFriendRequestCount) {
        this.confirmedFriendRequestCount = confirmedFriendRequestCount;
    }

    public List<NotificationDTO> getNotifList() {
        return notifList;
    }

    public void setNotifList(List<NotificationDTO> notifList) {
        this.notifList = notifList;
    }

    public int getNotifListCount() {
        return notifListCount;
    }

    public void setNotifListCount(int notifListCount) {
        this.notifListCount = notifListCount;
    }

    public int getNotifListcount() {
        return notifListCount;
    }

    public void setNotifListcount(int notifListcount) {
        this.notifListCount = notifListcount;
    }

}
