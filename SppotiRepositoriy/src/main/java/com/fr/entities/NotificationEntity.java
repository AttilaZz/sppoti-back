package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.models.NotificationType;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by: Wail DJENANE on Nov 10, 2016
 */
@Entity
@Table(name = "NOTIFICATION")
@JsonInclude(Include.NON_EMPTY)
public class NotificationEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private Integer uuid = UUID.randomUUID().hashCode();

    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id", nullable = false)
    private UserEntity from;

    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", nullable = false)
    private UserEntity to;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date creationDate = new Date();

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(nullable = false)
    private boolean opened = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getFrom() {
        return from;
    }

    public void setFrom(UserEntity from) {
        this.from = from;
    }

    public UserEntity getTo() {
        return to;
    }

    public void setTo(UserEntity to) {
        this.to = to;
    }

    public Date getCreationDate()
    {
        return creationDate;
    }
    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }
    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }
}
