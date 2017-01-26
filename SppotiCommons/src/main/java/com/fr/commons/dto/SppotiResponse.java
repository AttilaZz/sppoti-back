package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.entities.Sport;

import java.util.Date;

/**
 * Created by: Wail DJENANE on Jul 12, 2016
 */
@JsonInclude(Include.NON_DEFAULT)
public class SppotiResponse {

    private int id;

    private String titre;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private Date datetimeCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private Date dateTimeStart;

    private String location;

    private int maxMembersCount;

    private String tags;

    private Sport relatedSport;

    private User userSppoti;

    private TeamResponse teamHost;

    private TeamResponse teamGuest;

    public SppotiResponse() {
    }

    public SppotiResponse(String titre, Date datetimeCreated, Date dateTimeStart, String location, int maxMembersCount, Sport relatedSport) {
        this.titre = titre;
        this.datetimeCreated = datetimeCreated;
        this.dateTimeStart = dateTimeStart;
        this.location = location;
        this.maxMembersCount = maxMembersCount;
        this.relatedSport = relatedSport;
    }

    public SppotiResponse(int id) {
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

    public int getMaxMembersCount() {
        return maxMembersCount;
    }

    public void setMaxMembersCount(int maxMembersCount) {
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

    public User getUserSppoti() {
        return userSppoti;
    }

    public void setUserSppoti(User userSppoti) {
        this.userSppoti = userSppoti;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
