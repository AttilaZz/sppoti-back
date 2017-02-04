package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 * Created by djenanewail on 1/21/17.
 */

@Entity
@Table
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private int uuid = UUID.randomUUID().hashCode();

    @Column(nullable = false)
    private String name;

    private String logoPath;
    private String coverPath;
    private String xPosition;
    private String yPosition;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "teams")
    private Set<Users_team> users_teams;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id")
    private Set<Users> admins;

    @OneToOne
    @JoinColumn(name = "sport_id")
    private Sport sport;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public Set<Users> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<Users> admins) {
        this.admins = admins;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public Set<Users_team> getUsers_teams() {
        return users_teams;
    }

    public void setUsers_teams(Set<Users_team> users_teams) {
        this.users_teams = users_teams;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public String getyPosition() {
        return yPosition;
    }

    public void setyPosition(String yPosition) {
        this.yPosition = yPosition;
    }

    public String getxPosition() {
        return xPosition;
    }

    public void setxPosition(String xPosition) {
        this.xPosition = xPosition;
    }
}
