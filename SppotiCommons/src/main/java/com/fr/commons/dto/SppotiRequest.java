package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@JsonInclude(Include.NON_NULL)
public class SppotiRequest {

    private String titre;
    private Long sportId;
    private String description;
    @JsonProperty("sppotiDatetime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private Date datetimeStart;

    @JsonProperty("teamHost")
    private TeamRequest myTeam;

    @JsonProperty("teamHostId")
    private int myTeamId;

    @JsonProperty("teamAdverse")
    private int vsTeam;
    @JsonProperty("location")
    private String address;
    private int maxTeamCount;
    private String tags;

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Long getSportId() {
        return sportId;
    }

    public void setSportId(Long sportId) {
        this.sportId = sportId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatetimeStart() {
        return datetimeStart;
    }

    public void setDatetimeStart(Date datetimeStart) {
        this.datetimeStart = datetimeStart;
    }

    public TeamRequest getMyTeam() {
        return myTeam;
    }

    public void setMyTeam(TeamRequest myTeam) {
        this.myTeam = myTeam;
    }

    public int getVsTeam() {
        return vsTeam;
    }

    public void setVsTeam(int vsTeam) {
        this.vsTeam = vsTeam;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMaxTeamCount() {
        return maxTeamCount;
    }

    public void setMaxTeamCount(int maxTeamCount) {
        this.maxTeamCount = maxTeamCount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getMyTeamId() {
        return myTeamId;
    }

    public void setMyTeamId(int myTeamId) {
        this.myTeamId = myTeamId;
    }
}
