package com.fr.commons.dto.team;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fr.commons.SportDTO;
import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.commons.dto.UserDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by djenanewail on 1/26/17.
 */

public class TeamDTO extends AbstractCommonDTO {

    private String name;
    private String logoPath;
    private String coverPath;
    private UserDTO teamAdmin;
    private SportDTO sport;
    private String color;
    private List<UserDTO> teamMembers;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Europe/Paris")
    private Date creationDate;

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

    public UserDTO getTeamAdmin() {
        return teamAdmin;
    }

    public void setTeamAdmin(UserDTO teamAdmin) {
        this.teamAdmin = teamAdmin;
    }

    public SportDTO getSport() {
        return sport;
    }

    public void setSport(SportDTO sport) {
        this.sport = sport;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<UserDTO> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<UserDTO> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
