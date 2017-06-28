package com.fr.commons.dto.sppoti;

import javax.validation.constraints.NotNull;

public class SppoterDTO
{
	@NotNull
	private String userId;
	@NotNull
	private String sppotiId;
	@NotNull
	private String teamId;

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public String getSppotiId() {
		return this.sppotiId;
	}

	public void setSppotiId(final String sppotiId) {
		this.sppotiId = sppotiId;
	}

	public String getTeamId() {
		return this.teamId;
	}

	public void setTeamId(final String teamId) {
		this.teamId = teamId;
	}
}