package com.fr.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Created by: Wail DJENANE on Aug 16, 2016
 */
@Entity
@Table(name = "PERSISTENT_LOGINS")
public class PersistentLogin
        implements Serializable {

    /**
     * Id de sérialisation.
     */
    private static final long serialVersionUID = 1L;

    @Id
    private String series;

    @Column(name = "USERNAME", unique = true, nullable = false)
    private String username;

    @Column(name = "TOKEN", unique = true, nullable = false)
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    private Date last_used;

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLast_used() {
        return last_used;
    }

    public void setLast_used(Date last_used) {
        this.last_used = last_used;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PersistentLogin that = (PersistentLogin) o;

        if (series != null ? !series.equals(that.series) : that.series != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        return last_used != null ? last_used.equals(that.last_used) : that.last_used == null;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public int hashCode() {
        int result = 31;
        result = 31 * result + (series != null ? series.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (last_used != null ? last_used.hashCode() : 0);
        return result;
    }
}
