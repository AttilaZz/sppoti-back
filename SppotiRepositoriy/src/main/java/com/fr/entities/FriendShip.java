package com.fr.entities;

import com.fr.models.FriendStatus;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by: Wail DJENANE on Jul 3, 2016
 */

@Entity
public class FriendShip {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String datetime = new DateTime().toString();

    @Column(nullable = false)
    private String status = FriendStatus.PENDING.name();

    @Column(name = "friend_id")
    private int friend;

    @Column(name = "user_id")
    private int user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFriend() {
        return friend;
    }

    public void setFriend(int friend) {
        this.friend = friend;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}