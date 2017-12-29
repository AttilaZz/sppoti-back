package com.fr.commons.dto.sppoti;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.commons.dto.ScoreDTO;
import com.fr.commons.dto.SportDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.SppotiStatus;
import com.fr.commons.utils.JsonDateDeserializer;
import com.fr.commons.utils.JsonDateSerializer;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.fr.commons.enumeration.GlobalAppStatusEnum.CONFIRMED;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

public class SppotiDTO extends AbstractCommonDTO
{
	
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date datetimeCreated;
	
	private String cover;
	@JsonProperty("sport")
	private SportDTO relatedSport;
	private TeamDTO teamHost;
	private Integer sppotiCounter;
	private Boolean mySppoti;
	private String adminUserId;
	private String adminTeamId;
	private String connectedUserId;
	private Long sppotiDuration;
	private ScoreDTO score;
	
	private List<TeamDTO> teamAdverse;
	
	@NotEmpty
	@JsonProperty("titre")
	private String name;
	
	@NotNull
	private Long sportId;
	
	private String description;
	
	@NotNull
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@JsonProperty("sppotiDatetime")
	private Date dateTimeStart;
	
	@JsonProperty("teamHostId")
	private String myTeamId;
	
	@JsonProperty("teamAdverseId")
	private String vsTeam;
	
	@NotEmpty
	private String location;
	
	@NotNull
	private Long altitude;
	
	@NotNull
	private Long longitude;
	
	private Integer maxTeamCount;
	private String tags;
	
	private String teamAdverseStatus;
	
	private String timeZone;
	private SppotiStatus type;
	
	public Date getDatetimeCreated()
	{
		return this.datetimeCreated;
	}
	
	public void setDatetimeCreated(final Date datetimeCreated)
	{
		this.datetimeCreated = datetimeCreated;
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
	
	public String getAdminUserId() {
		return this.adminUserId;
	}
	
	public void setAdminUserId(final String adminUserId) {
		this.adminUserId = adminUserId;
	}
	
	public String getAdminTeamId() {
		return this.adminTeamId;
	}
	
	public void setAdminTeamId(final String adminTeamId) {
		this.adminTeamId = adminTeamId;
	}
	
	public String getConnectedUserId() {
		return this.connectedUserId;
	}
	
	public void setConnectedUserId(final String connectedUserId) {
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
	
	public List<TeamDTO> getTeamAdverse()
	{
		return this.teamAdverse;
	}
	
	public void setTeamAdverse(final List<TeamDTO> teamAdverse)
	{
		this.teamAdverse = teamAdverse;
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
	
	public String getMyTeamId()
	{
		return this.myTeamId;
	}
	
	public void setMyTeamId(final String myTeamId)
	{
		this.myTeamId = myTeamId;
	}
	
	public String getVsTeam()
	{
		return this.vsTeam;
	}
	
	public void setVsTeam(final String vsTeam)
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
	
	public String getTimeZone() {
		return this.timeZone;
	}
	
	public void setTimeZone(final String timeZone) {
		this.timeZone = timeZone;
	}
	
	public SppotiStatus getType() {
		return this.type;
	}
	
	public void setType(final SppotiStatus type) {
		this.type = type;
	}
	
	public List<UserDTO> getSppotiMailingList() {
		final List<UserDTO> sppotiMembersMailingList = new ArrayList<>();
		
		final Optional<TeamDTO> adverseTeam = getTeamAdverse().stream()
				.filter(t -> t.getTeamAdverseStatus().equals(CONFIRMED.name())).findFirst();
		
		adverseTeam.ifPresent(a -> {
			final List<UserDTO> teamAdverseMailingList = a.getMembers().stream()
					.filter(m -> m.getSppotiStatus().equals(CONFIRMED)).collect(Collectors.toList());
			
			sppotiMembersMailingList.addAll(teamAdverseMailingList);
		});
		
		final List<UserDTO> teamHostMailingList = getTeamHost().getMembers().stream()
				.filter(m -> m.getSppotiStatus().equals(CONFIRMED)).collect(Collectors.toList());
		sppotiMembersMailingList.addAll(teamHostMailingList);
		return sppotiMembersMailingList;
	}
}
