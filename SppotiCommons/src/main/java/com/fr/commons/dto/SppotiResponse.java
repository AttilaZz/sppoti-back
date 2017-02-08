package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fr.entities.Sport;

import java.util.Date;

/**
 * Created by: Wail DJENANE on Jul 12, 2016
 */
@JsonInclude(Include.NON_NULL)
public class SppotiResponse {

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
    private Sport relatedSport;

    private TeamResponse teamHost;

    @JsonProperty("teamAdverse")
    private TeamResponse teamGuest;

    private Integer sppotiCounter;

    private Boolean mySppoti;

    public SppotiResponse() {
    }

    public SppotiResponse(String titre, Date datetimeCreated, Date dateTimeStart, String location, Integer maxMembersCount, Sport relatedSport) {
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

    public SppotiResponse(Integer id) {
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

    public Sport getRelatedSport() {
        return relatedSport;
    }

    public void setRelatedSport(Sport relatedSport) {
        this.relatedSport = relatedSport;
    }

    public TeamResponse getTeamHost() {
        return teamHost;
    }

    public void setTeamHost(TeamResponse teamHost) {
        this.teamHost = teamHost;
    }

    public TeamResponse getTeamGuest() {
        return teamGuest;
    }

    public void setTeamGuest(TeamResponse teamGuest) {
        this.teamGuest = teamGuest;
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
}
