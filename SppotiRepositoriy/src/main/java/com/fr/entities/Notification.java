package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.models.NotificationType;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: Wail DJENANE on Nov 10, 2016
 */
@Entity
@JsonInclude(Include.NON_EMPTY)
public class Notification {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "from_user_id", nullable = false)
    private Users from;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "to_user_id", nullable = false)
    private Users to;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getFrom() {
        return from;
    }

    public void setFrom(Users from) {
        this.from = from;
    }

    public Users getTo() {
        return to;
    }

    public void setTo(Users to) {
        this.to = to;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
}
