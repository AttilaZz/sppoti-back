package com.fr.commons.dto.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;

import java.util.Date;

/**
 * Created by djenanewail on 2/11/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDTO extends AbstractCommonDTO
{
	
	private UserDTO from;
	private UserDTO to;
	private Integer notificationType;
	private Boolean opened;
	
	private Integer teamId;
	private Integer sppotiId;
	private Integer postId;
	
	private TeamDTO team;
	private SppotiDTO sppoti;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private Date datetime;
	
	public UserDTO getFrom()
	{
		return this.from;
	}
	
	public void setFrom(final UserDTO from)
	{
		this.from = from;
	}
	
	public UserDTO getTo()
	{
		return this.to;
	}
	
	public void setTo(final UserDTO to)
	{
		this.to = to;
	}
	
	public Date getDatetime()
	{
		return this.datetime;
	}
	
	public void setDatetime(final Date datetime)
	{
		this.datetime = datetime;
	}
	
	public Integer getNotificationType()
	{
		return this.notificationType;
	}
	
	public void setNotificationType(final Integer notificationType)
	{
		this.notificationType = notificationType;
	}
	
	public Boolean getOpened()
	{
		return this.opened;
	}
	
	public void setOpened(final Boolean opened)
	{
		this.opened = opened;
	}
	
	public Integer getTeamId()
	{
		return this.teamId;
	}
	
	public void setTeamId(final Integer teamId)
	{
		this.teamId = teamId;
	}
	
	public Integer getSppotiId()
	{
		return this.sppotiId;
	}
	
	public void setSppotiId(final Integer sppotiId)
	{
		this.sppotiId = sppotiId;
	}
	
	public Integer getPostId()
	{
		return this.postId;
	}
	
	public void setPostId(final Integer postId)
	{
		this.postId = postId;
	}
	
	public TeamDTO getTeam()
	{
		return this.team;
	}
	
	public void setTeam(final TeamDTO team)
	{
		this.team = team;
	}
	
	public SppotiDTO getSppoti()
	{
		return this.sppoti;
	}
	
	public void setSppoti(final SppotiDTO sppoti)
	{
		this.sppoti = sppoti;
	}
}


