package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.commons.enumeration.GlobalAppStatusEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Entity
@Table(name = "SPPOTI")
@JsonInclude(Include.NON_EMPTY)
public class SppotiEntity
        extends AbstractCommonEntity {

    @Column(nullable = false)
    private String titre;

    @Column
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
    private int maxMembersCount;

    @Column
    private String cover;

    @Column
    private String tags;

    @Column
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
    private TeamEntity teamHost;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_adverse_id")
    @JsonIgnore
    private TeamEntity teamAdverse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GlobalAppStatusEnum teamAdverseStatus = GlobalAppStatusEnum.NO_CHALLENGE_YET;

    @OneToMany(mappedBy = "sppoti", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SppotiMemberEntity> sppotiMembers;

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

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxMembersCount() {
        return maxMembersCount;
    }

    public void setMaxMembersCount(int maxMembersCount) {
        this.maxMembersCount = maxMembersCount;
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

    public TeamEntity getTeamHost() {
        return teamHost;
    }

    public void setTeamHost(TeamEntity teamHost) {
        this.teamHost = teamHost;
    }

    public TeamEntity getTeamAdverse() {
        return teamAdverse;
    }

    public void setTeamAdverse(TeamEntity teamAdverse) {
        this.teamAdverse = teamAdverse;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<SppotiMemberEntity> getSppotiMembers() {
        return sppotiMembers;
    }

    public void setSppotiMembers(Set<SppotiMemberEntity> sppotiMembers) {
        this.sppotiMembers = sppotiMembers;
    }

    public GlobalAppStatusEnum getTeamAdverseStatus() {
        return teamAdverseStatus;
    }

    public void setTeamAdverseStatus(GlobalAppStatusEnum teamAdverseStatus) {
        this.teamAdverseStatus = teamAdverseStatus;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SppotiEntity)) return false;
        if (!super.equals(o)) return false;

        SppotiEntity that = (SppotiEntity) o;

        if (maxMembersCount != that.maxMembersCount) return false;
        if (deleted != that.deleted) return false;
        if (titre != null ? !titre.equals(that.titre) : that.titre != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (datetimeCreated != null ? !datetimeCreated.equals(that.datetimeCreated) : that.datetimeCreated != null)
            return false;
        if (dateTimeStart != null ? !dateTimeStart.equals(that.dateTimeStart) : that.dateTimeStart != null)
            return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (tags != null ? !tags.equals(that.tags) : that.tags != null) return false;
        return teamAdverseStatus != null ? teamAdverseStatus.equals(that.teamAdverseStatus) : that.teamAdverseStatus == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (titre != null ? titre.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (datetimeCreated != null ? datetimeCreated.hashCode() : 0);
        result = 31 * result + (dateTimeStart != null ? dateTimeStart.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (cover != null ? cover.hashCode() : 0);
        result = 31 * result + maxMembersCount;
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + (teamAdverseStatus != null ? teamAdverseStatus.hashCode() : 0);
        return result;
    }
}
