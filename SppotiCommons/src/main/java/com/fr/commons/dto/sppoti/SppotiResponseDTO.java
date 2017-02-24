package com.fr.commons.dto.sppoti;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fr.commons.dto.team.TeamResponseDTO;
import com.fr.entities.SportEntity;

import java.util.Date;

/**
 * Created by: Wail DJENANE on Jul 12, 2016
 */
@JsonInclude(Include.NON_NULL)
public class SppotiResponseDTO {

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

    @JsonProperty("sport")
    private SportEntity relatedSport;

    private TeamResponseDTO teamHost;

    private TeamResponseDTO teamAdverse;
    private Integer teamAdverseStatus;

    private Integer sppotiCounter;

    private Boolean mySppoti;

    private Integer adminUserId;
    private Integer adminTeamId;
    private Integer connectedUserId;

    public SppotiResponseDTO() {
    }

    public SppotiResponseDTO(String titre, Date datetimeCreated, Date dateTimeStart, String location, Integer maxMembersCount, SportEntity relatedSport) {
        this.titre = titre;
        this.datetimeCreated = datetimeCreated;
        this.dateTimeStart = dateTimeStart;
        this.location = location;
        this.maxMembersCount = maxMembersCount;
        this.relatedSport = relatedSport;
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

    public SportEntity getRelatedSport() {
        return relatedSport;
    }

    public void setRelatedSport(SportEntity relatedSport) {
        this.relatedSport = relatedSport;
    }

    public TeamResponseDTO getTeamHost() {
        return teamHost;
    }

    public void setTeamHost(TeamResponseDTO teamHost) {
        this.teamHost = teamHost;
    }

    public TeamResponseDTO getTeamAdverse() {
        return teamAdverse;
    }

    public void setTeamAdverse(TeamResponseDTO teamAdverse) {
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
}
