package com.fr.commons.dto.team;

import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.commons.dto.UserDTO;

import java.util.List;

/**
 * Created by djenanewail on 1/23/17.
 */
public class TeamRequestDTO extends AbstractCommonDTO {

    private String name;
    private String logoPath;
    private String coverPath;
    private List<UserDTO> members;

    private Integer status;
    private Integer xPosition;
    private Integer yPosition;

    private Long sportId;

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

    public List<UserDTO> getMembers() {
        return members;
    }

    public void setMembers(List<UserDTO> members) {
        this.members = members;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getxPosition() {
        return xPosition;
    }

    public void setxPosition(Integer xPosition) {
        this.xPosition = xPosition;
    }

    public Integer getyPosition() {
        return yPosition;
    }

    public void setyPosition(Integer yPosition) {
        this.yPosition = yPosition;
    }

    public Long getSportId() {
        return sportId;
    }

    public void setSportId(Long sportId) {
        this.sportId = sportId;
    }
}