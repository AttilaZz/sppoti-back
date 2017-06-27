package com.fr.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Add class description.
 *
 * Created by wdjenane on 27/06/2017.
 */
@Entity
@Table(name = "password_history")
public class PasswordHistory extends AbstractCommonEntity {

    @Column(updatable = false, nullable = false)
    private String password;

    @Column(updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetimeCreated;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private UserEntity user;

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Date getDatetimeCreated() {
        return this.datetimeCreated;
    }

    public void setDatetimeCreated(final Date datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(final UserEntity user) {
        this.user = user;
    }
}
