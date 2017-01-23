package com.fr.entities;

import com.fr.models.FriendStatus;
import org.joda.time.DateTime;

import javax.persistence.*;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private Users friend;

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

    public Users getFriend() {
        return friend;
    }

    public void setFriend(Users friend) {
        this.friend = friend;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}