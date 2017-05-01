package com.fr.commons.dto.sppoti;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.commons.dto.ScoreDTO;
import com.fr.commons.dto.SportDTO;
import com.fr.commons.dto.team.TeamDTO;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

public class SppotiDTO extends AbstractCommonDTO
{
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private Date datetimeCreated;
	
	private Integer maxMembersCount;
	private String cover;
	@JsonProperty("sport")
	private SportDTO relatedSport;
	private TeamDTO teamHost;
	private Integer sppotiCounter;
	private Boolean mySppoti;
	private Integer adminUserId;
	private Integer adminTeamId;
	private Integer connectedUserId;
	private Long sppotiDuration;
	private ScoreDTO score;
	
	private List<TeamDTO> teamAdverse;
	
	@NotEmpty
	@JsonProperty("titre")
	private String name;
	@NotNull
	private Long sportId;
	@NotEmpty
	private String description;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonProperty("sppotiDatetime")
	private Date dateTimeStart;
	
	@JsonProperty("teamHostId")
	private Integer myTeamId;
	@JsonProperty("teamAdverseId")
	private Integer vsTeam;
	@NotEmpty
	private String location;
	@NotNull
	private Long altitude;
	@NotNull
	private Long longitude;
	
	private Integer maxTeamCount;
	private String tags;
	
	private String teamAdverseStatus;
	
	public Date getDatetimeCreated()
	{
		return this.datetimeCreated;
	}
	
	public void setDatetimeCreated(final Date datetimeCreated)
	{
		this.datetimeCreated = datetimeCreated;
	}
	
	public Integer getMaxMembersCount()
	{
		return this.maxMembersCount;
	}
	
	public void setMaxMembersCount(final Integer maxMembersCount)
	{
		this.maxMembersCount = maxMembersCount;
	}
	
	public String getCover()
	{
		return this.cover;
	}
	
	public void setCover(final String cover)
	{
		this.cover = cover;
	}
	
	public SportDTO getRelatedSport()
	{
		return this.relatedSport;
	}
	
	public void setRelatedSport(final SportDTO relatedSport)
	{
		this.relatedSport = relatedSport;
	}
	
	public TeamDTO getTeamHost()
	{
		return this.teamHost;
	}
	
	public void setTeamHost(final TeamDTO teamHost)
	{
		this.teamHost = teamHost;
	}
	
	public List<TeamDTO> getTeamAdverse()
	{
		return this.teamAdverse;
	}
	
	public void setTeamAdverse(final List<TeamDTO> teamAdverse)
	{
		this.teamAdverse = teamAdverse;
	}
	
	public Integer getSppotiCounter()
	{
		return this.sppotiCounter;
	}
	
	public void setSppotiCounter(final Integer sppotiCounter)
	{
		this.sppotiCounter = sppotiCounter;
	}
	
	public Boolean getMySppoti()
	{
		return this.mySppoti;
	}
	
	public void setMySppoti(final Boolean mySppoti)
	{
		this.mySppoti = mySppoti;
	}
	
	public Integer getAdminUserId()
	{
		return this.adminUserId;
	}
	
	public void setAdminUserId(final Integer adminUserId)
	{
		this.adminUserId = adminUserId;
	}
	
	public Integer getAdminTeamId()
	{
		return this.adminTeamId;
	}
	
	public void setAdminTeamId(final Integer adminTeamId)
	{
		this.adminTeamId = adminTeamId;
	}
	
	public Integer getConnectedUserId()
	{
		return this.connectedUserId;
	}
	
	public void setConnectedUserId(final Integer connectedUserId)
	{
		this.connectedUserId = connectedUserId;
	}
	
	public Long getSppotiDuration()
	{
		return this.sppotiDuration;
	}
	
	public void setSppotiDuration(final Long sppotiDuration)
	{
		this.sppotiDuration = sppotiDuration;
	}
	
	public ScoreDTO getScore()
	{
		return this.score;
	}
	
	public void setScore(final ScoreDTO score)
	{
		this.score = score;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	public Long getSportId()
	{
		return this.sportId;
	}
	
	public void setSportId(final Long sportId)
	{
		this.sportId = sportId;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}
	
	public Date getDateTimeStart()
	{
		return this.dateTimeStart;
	}
	
	public void setDateTimeStart(final Date dateTimeStart)
	{
		this.dateTimeStart = dateTimeStart;
	}
	
	public Integer getMyTeamId()
	{
		return this.myTeamId;
	}
	
	public void setMyTeamId(final Integer myTeamId)
	{
		this.myTeamId = myTeamId;
	}
	
	public Integer getVsTeam()
	{
		return this.vsTeam;
	}
	
	public void setVsTeam(final Integer vsTeam)
	{
		this.vsTeam = vsTeam;
	}
	
	public String getLocation()
	{
		return this.location;
	}
	
	public void setLocation(final String location)
	{
		this.location = location;
	}
	
	public Long getAltitude()
	{
		return this.altitude;
	}
	
	public void setAltitude(final Long altitude)
	{
		this.altitude = altitude;
	}
	
	public Long getLongitude()
	{
		return this.longitude;
	}
	
	public void setLongitude(final Long longitude)
	{
		this.longitude = longitude;
	}
	
	public Integer getMaxTeamCount()
	{
		return this.maxTeamCount;
	}
	
	public void setMaxTeamCount(final Integer maxTeamCount)
	{
		this.maxTeamCount = maxTeamCount;
	}
	
	public String getTags()
	{
		return this.tags;
	}
	
	public void setTags(final String tags)
	{
		this.tags = tags;
	}
	
	public String getTeamAdverseStatus()
	{
		return this.teamAdverseStatus;
	}
	
	public void setTeamAdverseStatus(final String teamAdverseStatus)
	{
		this.teamAdverseStatus = teamAdverseStatus;
	}
}
