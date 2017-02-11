package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by djenanewail on 1/26/17.
 */

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class TeamResponse {

    private int id;
    private String name;
    private String logoPath;
    private String coverPath;
    private Long sportId;

    private List<User> teamMembers;

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

    public List<User> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<User> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public Long getSportId() {
        return sportId;
    }

    public void setSportId(Long sportId) {
        this.sportId = sportId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
