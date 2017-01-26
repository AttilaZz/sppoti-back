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
    @Column
    private String logoPath;
    @Column
    private String coverPath;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "team")
    private Set<Users> teamMembers;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "teamAdmin")
    private Set<Users> admins;

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

    public Set<Users> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(Set<Users> teamMembers) {
        this.teamMembers = teamMembers;
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
}
