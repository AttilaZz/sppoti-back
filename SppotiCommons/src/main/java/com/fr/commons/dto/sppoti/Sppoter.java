package com.fr.commons.dto.sppoti;

import javax.validation.constraints.NotNull;

public class Sppoter
{
	@NotNull
	private Integer userId;
	@NotNull
	private Integer sppotiId;
	@NotNull
	private Integer teamId;
	
	public Integer getUserId()
	{
		return this.userId;
	}
	
	public void setUserId(final Integer userId)
	{
		this.userId = userId;
	}
	
	public Integer getSppotiId()
	{
		return this.sppotiId;
	}
	
	public void setSppotiId(final Integer sppotiId)
	{
		this.sppotiId = sppotiId;
	}
	
	public Integer getTeamId()
	{
		return this.teamId;
	}
	
	public void setTeamId(final Integer teamId)
	{
		this.teamId = teamId;
	}
}