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

    @Column(nullable = false, unique = true)
    private String name;

    private String logoPath;
    private String coverPath;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "team")
    private Set<TeamMemberEntity> teamMembers;

    @OneToOne
    @JoinColumn(name = "sport_id")
    private SportEntity sport;

    /**
     * When creating a host team from a sppoti, we need a reference to the sppoti,
     * in order to save team host ang get i's id, otherwise, we get a transient exception.
     */
    @OneToMany(mappedBy = "teamHost", cascade = CascadeType.ALL)
    private Set<SppotiEntity> sppotiEntity;

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

    public Set<SppotiEntity> getSppotiEntity() {
        return sppotiEntity;
    }

    public void setSppotiEntity(Set<SppotiEntity> sppotiEntity) {
        this.sppotiEntity = sppotiEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeamEntity)) return false;
        if (!super.equals(o)) return false;

        TeamEntity that = (TeamEntity) o;

        if (deleted != that.deleted) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (logoPath != null ? !logoPath.equals(that.logoPath) : that.logoPath != null) return false;
        return coverPath != null ? coverPath.equals(that.coverPath) : that.coverPath == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (logoPath != null ? logoPath.hashCode() : 0);
        result = 31 * result + (coverPath != null ? coverPath.hashCode() : 0);
        result = 31 * result + (deleted ? 1 : 0);
        return result;
    }
}
