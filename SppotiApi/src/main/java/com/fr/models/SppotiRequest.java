/**
 * 
 */
package com.fr.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.pojos.Address;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@JsonInclude(Include.NON_EMPTY)
public class SppotiRequest {

	private String titre;
	private Long sportId;
	private String description;
	private String date;
	private Long[] teamPeopleId;
	private Address address;
	private int membersCount;
	private int type; // public/private
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long[] getTeamPeopleId() {
		return teamPeopleId;
	}

	public void setTeamPeopleId(Long[] teamPeopleId) {
		this.teamPeopleId = teamPeopleId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address spotAddress) {
		this.address = spotAddress;
	}

	public int getMembersCount() {
		return membersCount;
	}

	public void setMembersCount(int membersCount) {
		this.membersCount = membersCount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

}
