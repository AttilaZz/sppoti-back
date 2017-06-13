package com.fr.commons.dto.sppoti;

import javax.validation.constraints.NotNull;

public class Sppoter
{
	@NotNull
	private String userId;
	@NotNull
	private String sppotiId;
	@NotNull
	private String teamId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSppotiId() {
		return sppotiId;
	}

	public void setSppotiId(String sppotiId) {
		this.sppotiId = sppotiId;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
}