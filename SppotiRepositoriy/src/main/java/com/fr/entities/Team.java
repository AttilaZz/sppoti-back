package com.fr.entities;

import javax.persistence.*;
import java.util.Set;

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
    private String name;
    @Column(nullable = false)
    private String logoPath;
    @Column(nullable = false)
    private String coverPath;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "team")
    private Set<Users> teamMembers;

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
}
