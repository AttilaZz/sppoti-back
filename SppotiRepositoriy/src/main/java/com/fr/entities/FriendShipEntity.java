package com.fr.entities;

import com.fr.models.GlobalAppStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: Wail DJENANE on Jul 3, 2016
 */

@Entity
@Table(name = "friend_ship")
public class FriendShipEntity
        extends AbstractCommonEntity {
    @Column(nullable = false)
    private Date datetime = new Date();

    @Column(nullable = false)
    private String status = GlobalAppStatus.PENDING.name();

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private UserEntity friend;

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column
    private boolean deleted;

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

    public UserEntity getFriend() {
        return friend;
    }

    public void setFriend(UserEntity friend) {
        this.friend = friend;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendShipEntity)) return false;
        if (!super.equals(o)) return false;

        FriendShipEntity that = (FriendShipEntity) o;

        if (deleted != that.deleted) return false;
        if (datetime != null ? !datetime.equals(that.datetime) : that.datetime != null) return false;
        return status != null ? status.equals(that.status) : that.status == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (datetime != null ? datetime.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (deleted ? 1 : 0);
        return result;
    }
}