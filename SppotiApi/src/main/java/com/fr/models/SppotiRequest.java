/**
 *
 */
package com.fr.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Date;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@JsonInclude(Include.NON_EMPTY)
public class SppotiRequest {

    private String titre;
    private Long sportId;
    private String description;
    private Date datetimeCreated;
    private Long[] myTeam;
    private Long[] vsTeam;
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

    public Date getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(Date datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public Long[] getMyTeam() {
        return myTeam;
    }

    public void setMyTeam(Long[] myTeam) {
        this.myTeam = myTeam;
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

    public Long[] getVsTeam() {
        return vsTeam;
    }

    public void setVsTeam(Long[] vsTeam) {
        this.vsTeam = vsTeam;
    }
}
