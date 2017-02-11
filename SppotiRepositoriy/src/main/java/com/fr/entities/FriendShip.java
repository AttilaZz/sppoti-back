package com.fr.entities;

import com.fr.models.GlobalAppStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: Wail DJENANE on Jul 3, 2016
 */

@Entity
public class FriendShip {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Date datetime = new Date();

    @Column(nullable = false)
    private String status = GlobalAppStatus.PENDING.name();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private Users friend;

    @Column(name = "user_id")
    private Integer user;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Column
    private boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Users getFriend() {
        return friend;
    }

    public void setFriend(Users friend) {
        this.friend = friend;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}