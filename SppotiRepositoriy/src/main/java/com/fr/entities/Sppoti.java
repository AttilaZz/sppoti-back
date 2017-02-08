package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
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

    @Column(unique = true)
    private Integer uuid = UUID.randomUUID().hashCode();

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
    private Sport sport;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private Users userSppoti;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_host_id", nullable = false)
    @JsonIgnore
    private Team teamHost;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_adverse_id")
    @JsonIgnore
    private Team teamAdverse;

    @OneToMany(mappedBy = "sppoti", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SppotiMember> sppotiMembers;

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

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
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

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
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


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<SppotiMember> getSppotiMembers() {
        return sppotiMembers;
    }

    public void setSppotiMembers(Set<SppotiMember> sppotiMembers) {
        this.sppotiMembers = sppotiMembers;
    }
}
