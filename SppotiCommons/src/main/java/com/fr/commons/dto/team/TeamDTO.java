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

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamDTO extends AbstractCommonDTO {

    private String name;
    private String logoPath;
    private String coverPath;
    private UserDTO teamAdmin;
    private SportDTO sport;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Europe/Paris")
    private Date creationDate;

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

    public UserDTO getTeamAdmin() {
        return teamAdmin;
    }

    public void setTeamAdmin(UserDTO teamAdmin) {
        this.teamAdmin = teamAdmin;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
