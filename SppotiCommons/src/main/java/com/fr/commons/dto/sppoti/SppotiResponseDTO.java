package com.fr.commons.dto.sppoti;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fr.commons.SportDTO;
import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.commons.dto.team.TeamDTO;

import java.util.Date;

/**
 * Created by: Wail DJENANE on Jul 12, 2016
 */
@JsonInclude(Include.NON_NULL)
public class SppotiResponseDTO extends AbstractCommonDTO {

    private Integer id;

    private String titre;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private Date datetimeCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private Date dateTimeStart;

    private String location;

    private Integer maxMembersCount;

    private String tags;

    private String cover;

    @JsonProperty("sport")
    private SportDTO relatedSport;

    private TeamDTO teamHost;

    private TeamDTO teamAdverse;
    private Integer teamAdverseStatus;

    private Integer sppotiCounter;

    private Boolean mySppoti;

    private Integer adminUserId;
    private Integer adminTeamId;
    private Integer connectedUserId;

    public SppotiResponseDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SppotiResponseDTO(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(Date datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public Date getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(Date dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMaxMembersCount() {
        return maxMembersCount;
    }

    public void setMaxMembersCount(Integer maxMembersCount) {
        this.maxMembersCount = maxMembersCount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public SportDTO getRelatedSport() {
        return relatedSport;
    }

    public void setRelatedSport(SportDTO relatedSport) {
        this.relatedSport = relatedSport;
    }

    public TeamDTO getTeamHost() {
        return teamHost;
    }

    public void setTeamHost(TeamDTO teamHost) {
        this.teamHost = teamHost;
    }

    public TeamDTO getTeamAdverse() {
        return teamAdverse;
    }

    public void setTeamAdverse(TeamDTO teamAdverse) {
        this.teamAdverse = teamAdverse;
    }

    public Integer getSppotiCounter() {
        return sppotiCounter;
    }

    public void setSppotiCounter(Integer sppotiCounter) {
        this.sppotiCounter = sppotiCounter;
    }

    public Boolean getMySppoti() {
        return mySppoti;
    }

    public void setMySppoti(Boolean mySppoti) {
        this.mySppoti = mySppoti;
    }

    public Integer getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Integer adminUserId) {
        this.adminUserId = adminUserId;
    }

    public Integer getAdminTeamId() {
        return adminTeamId;
    }

    public void setAdminTeamId(Integer adminTeamId) {
        this.adminTeamId = adminTeamId;
    }

    public Integer getConnectedUserId() {
        return connectedUserId;
    }

    public void setConnectedUserId(Integer connectedUserId) {
        this.connectedUserId = connectedUserId;
    }

    public Integer getTeamAdverseStatus() {
        return teamAdverseStatus;
    }

    public void setTeamAdverseStatus(Integer teamAdverseStatus) {
        this.teamAdverseStatus = teamAdverseStatus;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
