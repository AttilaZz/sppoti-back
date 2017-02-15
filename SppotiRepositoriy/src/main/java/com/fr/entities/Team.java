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

    @Column(nullable = false)
    private int uuid = UUID.randomUUID().hashCode();

    @Column(nullable = false)
    private String name;

    private String logoPath;
    private String coverPath;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "team")
    private Set<TeamMembers> teamMemberss;

    @OneToOne
    @JoinColumn(name = "sport_id")
    private Sport sport;

    private boolean deleted = false;

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

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public Set<TeamMembers> getTeamMemberss() {
        return teamMemberss;
    }

    public void setTeamMemberss(Set<TeamMembers> teamMemberss) {
        this.teamMemberss = teamMemberss;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
