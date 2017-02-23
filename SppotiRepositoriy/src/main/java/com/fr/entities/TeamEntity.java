package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 * Created by djenanewail on 1/21/17.
 */

@Entity
@Table(name = "TEAM")
public class TeamEntity
        extends AbstractCommonEntity {

    @Column(nullable = false)
    private String name;

    private String logoPath;
    private String coverPath;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "team")
    private Set<TeamMemberEntity> teamMembers;

    @OneToOne
    @JoinColumn(name = "sport_id")
    private SportEntity sport;

    private boolean deleted = false;

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

    public Set<TeamMemberEntity> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(Set<TeamMemberEntity> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public SportEntity getSport() {
        return sport;
    }

    public void setSport(SportEntity sport) {
        this.sport = sport;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
