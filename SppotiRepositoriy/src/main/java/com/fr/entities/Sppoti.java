package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Entity
@JsonInclude(Include.NON_EMPTY)
public class Sppoti {

    @Id
    @GeneratedValue
    private Long id;

    private int uuid = UUID.randomUUID().hashCode();

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Date datetimeCreated = new Date();

    @Column(nullable = false)
    private Date dateTimeStart;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private int maxMembersCount;

    @Column
    private String tags;

    @Column
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "sport_id", nullable = false)
    @JsonIgnore
    private Sport relatedSport;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private Users userSppoti;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_adverse_id", nullable = false)
    @JsonIgnore
    private Team teamHost;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_guest_id")
    @JsonIgnore
    private Team teamAdverse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Sport getRelatedSport() {
        return relatedSport;
    }

    public void setRelatedSport(Sport relatedSport) {
        this.relatedSport = relatedSport;
    }

    public Users getUserSppoti() {
        return userSppoti;
    }

    public void setUserSppoti(Users userSppoti) {
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

    public Team getTeamHost() {
        return teamHost;
    }

    public void setTeamHost(Team teamHost) {
        this.teamHost = teamHost;
    }

    public Team getTeamAdverse() {
        return teamAdverse;
    }

    public void setTeamAdverse(Team teamAdverse) {
        this.teamAdverse = teamAdverse;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
