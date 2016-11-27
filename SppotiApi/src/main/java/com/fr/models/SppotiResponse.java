/**
 * 
 */
package com.fr.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.pojos.Address;
import com.fr.pojos.Sport;
import com.fr.pojos.Users;

/**
 * Created by: Wail DJENANE on Jul 12, 2016
 */
@JsonInclude(Include.NON_EMPTY)
public class SppotiResponse {

	private Users userSpooti;
	private String titre;
	private Sport sportId;
	private String description;
	private String date;
	private List<Users> teamPeopleId;
	private Address spotAddress;

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public Sport getSportId() {
		return sportId;
	}

	public void setSportId(Sport sportId) {
		this.sportId = sportId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Users> getTeamPeopleId() {
		return teamPeopleId;
	}

	public void setTeamPeopleId(List<Users> teamPeopleId) {
		this.teamPeopleId = teamPeopleId;
	}

	public Address getSpotAddress() {
		return spotAddress;
	}

	public void setSpotAddress(Address spotAddress) {
		this.spotAddress = spotAddress;
	}

	public Users getUserSpooti() {
		return userSpooti;
	}

	public void setUserSpooti(Users userSpooti) {
		this.userSpooti = userSpooti;
	}

}
