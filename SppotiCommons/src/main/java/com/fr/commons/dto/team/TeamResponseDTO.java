package com.fr.commons.dto.team;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fr.commons.SportDTO;
import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.commons.dto.UserDTO;

import java.util.List;

/**
 * Created by djenanewail on 1/26/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamResponseDTO extends AbstractCommonDTO {

    private String name;
    private String logoPath;
    private String coverPath;
    private SportDTO sport;

    private List<UserDTO> teamMembers;

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

    public List<UserDTO> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<UserDTO> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public SportDTO getSport() {
        return sport;
    }

    public void setSport(SportDTO sport) {
        this.sport = sport;
    }
}
