package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Entity
@Table(name = "SPPOTI")
public class SppotiEntity
        extends AbstractCommonEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetimeCreated = new Date();

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeStart;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer maxTeamCount;

    private Long sppotiDuration;
    private String cover;
    private String tags;

    @Column(nullable = false)
    private Long altitude;
    @Column(nullable = false)
    private Long longitude;

    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "sport_id", nullable = false)
    @JsonIgnore
    private SportEntity sport;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity userSppoti;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_host_id")
    @JsonIgnore
    private TeamEntity teamHostEntity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sppoti", cascade = CascadeType.ALL)
    private Set<SppotiAdverseEntity> adverseTeams = new HashSet<>();

    @OneToMany(mappedBy = "sppoti", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SppoterEntity> sppotiMembers = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "score_id")
    private ScoreEntity scoreEntity;

    /**
     * to get trace of the connected user when using transformers.
     */
    private transient Long connectedUserId;

    public ScoreEntity getScoreEntity() {
        return scoreEntity;
    }

    public void setScoreEntity(ScoreEntity scoreEntity) {
        this.scoreEntity = scoreEntity;
    }

    public Date getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(Date datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public SportEntity getSport() {
        return sport;
    }

    public void setSport(SportEntity sport) {
        this.sport = sport;
    }

    public UserEntity getUserSppoti() {
        return userSppoti;
    }

    public void setUserSppoti(UserEntity userSppoti) {
        this.userSppoti = userSppoti;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxTeamCount() {
        return maxTeamCount;
    }

    public void setMaxTeamCount(Integer maxTeamCount) {
        this.maxTeamCount = maxTeamCount;
    }

    public Date getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(Date dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public TeamEntity getTeamHostEntity() {
        return teamHostEntity;
    }

    public void setTeamHostEntity(TeamEntity teamHostEntity) {
        this.teamHostEntity = teamHostEntity;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<SppoterEntity> getSppotiMembers() {
        return sppotiMembers;
    }

    public void setSppotiMembers(Set<SppoterEntity> sppotiMembers) {
        this.sppotiMembers = sppotiMembers;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Long getSppotiDuration() {
        return sppotiDuration;
    }

    public void setSppotiDuration(Long sppotiDuration) {
        this.sppotiDuration = sppotiDuration;
    }

    public Long getAltitude() {
        return altitude;
    }

    public void setAltitude(Long altitude) {
        this.altitude = altitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public Set<SppotiAdverseEntity> getAdverseTeams() {
        return adverseTeams;
    }

    public void setAdverseTeams(Set<SppotiAdverseEntity> adverseTeams) {
        this.adverseTeams = adverseTeams;
    }

    public Long getConnectedUserId() {
        return connectedUserId;
    }

    public void setConnectedUserId(Long connectedUserId) {
        this.connectedUserId = connectedUserId;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SppotiEntity that = (SppotiEntity) o;

        if (deleted != that.deleted) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (datetimeCreated != null ? !datetimeCreated.equals(that.datetimeCreated) : that.datetimeCreated != null)
            return false;
        if (dateTimeStart != null ? !dateTimeStart.equals(that.dateTimeStart) : that.dateTimeStart != null)
            return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (maxTeamCount != null ? !maxTeamCount.equals(that.maxTeamCount) : that.maxTeamCount != null) return false;
        if (sppotiDuration != null ? !sppotiDuration.equals(that.sppotiDuration) : that.sppotiDuration != null)
            return false;
        if (cover != null ? !cover.equals(that.cover) : that.cover != null) return false;
        if (tags != null ? !tags.equals(that.tags) : that.tags != null) return false;
        if (altitude != null ? !altitude.equals(that.altitude) : that.altitude != null) return false;
        return longitude != null ? longitude.equals(that.longitude) : that.longitude == null;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (datetimeCreated != null ? datetimeCreated.hashCode() : 0);
        result = 31 * result + (dateTimeStart != null ? dateTimeStart.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (cover != null ? cover.hashCode() : 0);
        result = 31 * result + (maxTeamCount != null ? maxTeamCount.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + (sppotiDuration != null ? sppotiDuration.hashCode() : 0);
        result = 31 * result + (altitude != null ? altitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }
}
