package com.fr.commons.dto.sppoti;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fr.commons.SportDTO;
import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.commons.dto.ScoreDTO;
import com.fr.commons.dto.team.TeamDTO;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

public class SppotiDTO extends AbstractCommonDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private Date datetimeCreated;

    private Integer maxMembersCount;
    private String cover;
    @JsonProperty("sport")
    private SportDTO relatedSport;
    private TeamDTO teamHost;
    private Integer teamAdverseStatus;
    private Integer sppotiCounter;
    private Boolean mySppoti;
    private Integer adminUserId;
    private Integer adminTeamId;
    private Integer connectedUserId;
    private Long sppotiDuration;
    private ScoreDTO score;
    private TeamDTO teamAdverse;

    @NotEmpty
    private String titre;
    @NotNull
    private Long sportId;
    @NotEmpty
    private String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonProperty("sppotiDatetime")
    private Date dateTimeStart;

    @JsonProperty("teamHostId")
    private int myTeamId;
    @JsonProperty("teamAdverseId")
    private int vsTeam;
    @NotEmpty
    private String location;
    @NotNull
    private Long altitude;
    @NotNull
    private Long longitude;
    private Integer maxTeamCount;
    private String tags;

    public Date getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(Date datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public Integer getMaxMembersCount() {
        return maxMembersCount;
    }

    public void setMaxMembersCount(Integer maxMembersCount) {
        this.maxMembersCount = maxMembersCount;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public Integer getTeamAdverseStatus() {
        return teamAdverseStatus;
    }

    public void setTeamAdverseStatus(Integer teamAdverseStatus) {
        this.teamAdverseStatus = teamAdverseStatus;
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

    public Long getSppotiDuration() {
        return sppotiDuration;
    }

    public void setSppotiDuration(Long sppotiDuration) {
        this.sppotiDuration = sppotiDuration;
    }

    public ScoreDTO getScore() {
        return score;
    }

    public void setScore(ScoreDTO score) {
        this.score = score;
    }

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

    public Date getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(Date dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public int getMyTeamId() {
        return myTeamId;
    }

    public void setMyTeamId(int myTeamId) {
        this.myTeamId = myTeamId;
    }

    public int getVsTeam() {
        return vsTeam;
    }

    public void setVsTeam(int vsTeam) {
        this.vsTeam = vsTeam;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getAltitude() {
        return altitude;
    }

    public void setAltitude(Long altitude) {
        this.altitude = altitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public Integer getMaxTeamCount() {
        return maxTeamCount;
    }

    public void setMaxTeamCount(Integer maxTeamCount) {
        this.maxTeamCount = maxTeamCount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
