package com.fr.commons.dto.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.commons.dto.SportDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.TeamStatus;
import com.fr.commons.utils.JsonDateSerializer;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by djenanewail on 1/26/17.
 */

public class TeamDTO extends AbstractCommonDTO
{
	
	@NotEmpty
	private String name;
	private String logoPath;
	private String coverPath;
	private String color;
	private String teamAdverseStatus;
	private Boolean sentFromSppotiAdmin;
	private TeamStatus type;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date creationDate = new Date();
	
	private UserDTO teamAdmin;
	
	@JsonProperty("sport")
	private SportDTO sportDTO;
	
	@NotNull
	@JsonProperty("teamMembers")
	private List<UserDTO> members;
	
	private Integer status;
	private Integer xPosition;
	private Integer yPosition;
	
	@NotNull
	private Long sportId;
	
	@NotNull
	private String timeZone;
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	public String getLogoPath()
	{
		return this.logoPath;
	}
	
	public void setLogoPath(final String logoPath)
	{
		this.logoPath = logoPath;
	}
	
	public String getCoverPath()
	{
		return this.coverPath;
	}
	
	public void setCoverPath(final String coverPath)
	{
		this.coverPath = coverPath;
	}
	
	public UserDTO getTeamAdmin()
	{
		return this.teamAdmin;
	}
	
	public void setTeamAdmin(final UserDTO teamAdmin)
	{
		this.teamAdmin = teamAdmin;
	}
	
	public SportDTO getSportDTO()
	{
		return this.sportDTO;
	}
	
	public void setSportDTO(final SportDTO sportDTO)
	{
		this.sportDTO = sportDTO;
	}
	
	public String getColor()
	{
		return this.color;
	}
	
	public void setColor(final String color)
	{
		this.color = color;
	}
	
	public List<UserDTO> getMembers()
	{
		return this.members;
	}
	
	public void setMembers(final List<UserDTO> members)
	{
		this.members = members;
	}
	
	public Integer getStatus()
	{
		return this.status;
	}
	
	public void setStatus(final Integer status)
	{
		this.status = status;
	}
	
	public Integer getxPosition()
	{
		return this.xPosition;
	}
	
	public void setxPosition(final Integer xPosition)
	{
		this.xPosition = xPosition;
	}
	
	public Integer getyPosition()
	{
		return this.yPosition;
	}
	
	public void setyPosition(final Integer yPosition)
	{
		this.yPosition = yPosition;
	}
	
	public Long getSportId()
	{
		return this.sportId;
	}
	
	public void setSportId(final Long sportId)
	{
		this.sportId = sportId;
	}
	
	public Date getCreationDate()
	{
		return this.creationDate;
	}
	
	public void setCreationDate(final Date creationDate)
	{
		this.creationDate = creationDate;
	}
	
	public String getTeamAdverseStatus()
	{
		return this.teamAdverseStatus;
	}
	
	public void setTeamAdverseStatus(final String teamAdverseStatus)
	{
		this.teamAdverseStatus = teamAdverseStatus;
	}
	
	public Boolean getSentFromSppotiAdmin()
	{
		return this.sentFromSppotiAdmin;
	}
	
	public void setSentFromSppotiAdmin(final Boolean sentFromSppotiAdmin)
	{
		this.sentFromSppotiAdmin = sentFromSppotiAdmin;
	}
	
	public String getTimeZone() {
		return this.timeZone;
	}
	
	public void setTimeZone(final String timeZone) {
		this.timeZone = timeZone;
	}
	
	public TeamStatus getType() {
		return this.type;
	}
	
	public void setType(final TeamStatus type) {
		this.type = type;
	}
}
