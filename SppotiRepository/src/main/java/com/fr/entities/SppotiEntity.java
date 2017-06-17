package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fr.commons.enumeration.GlobalAppStatusEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Entity
@Table(name = "SPPOTI")
public class SppotiEntity extends AbstractCommonEntity
{
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private final GlobalAppStatusEnum status = GlobalAppStatusEnum.IN_PROGRESS;
	
	@Column(nullable = false)
	private String name;
	
	private String description;
	
	@Column(nullable = false, name = "datetime_created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date datetimeCreated = new Date();
	
	@Column(nullable = false, name = "date_time_start")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTimeStart;
	
	@Column(nullable = false)
	private String location;
	
	@Column(nullable = false)
	private Integer maxTeamCount;
	
	private Long sppotiDuration;
	private String cover;
	private String tags;
	
	@Column(nullable = false)
	private Long altitude;
	
	@Column(nullable = false)
	private Long longitude;
	
	@Column(nullable = false)
	private boolean deleted = false;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "sport_id", nullable = false)
	@JsonIgnore
	private SportEntity sport;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private UserEntity userSppoti;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "team_host_id")
	@JsonIgnore
	private TeamEntity teamHostEntity;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sppoti", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<SppotiAdverseEntity> adverseTeams = new HashSet<>();
	
	@OneToMany(mappedBy = "sppoti", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<SppoterEntity> sppotiMembers = new HashSet<>();
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "score_id")
	private ScoreEntity scoreEntity;
	
	@Column(name = "time_zone")
	private String timeZone;
	
	/**
	 * to get trace of the connected user when using transformers.
	 */
	private transient Long connectedUserId;
	
	public ScoreEntity getScoreEntity()
	{
		return this.scoreEntity;
	}
	
	public void setScoreEntity(final ScoreEntity scoreEntity)
	{
		this.scoreEntity = scoreEntity;
	}
	
	public Date getDatetimeCreated()
	{
		return this.datetimeCreated;
	}
	
	public void setDatetimeCreated(final Date datetimeCreated)
	{
		this.datetimeCreated = datetimeCreated;
	}
	
	public String getLocation()
	{
		return this.location;
	}
	
	public void setLocation(final String location)
	{
		this.location = location;
	}
	
	public SportEntity getSport()
	{
		return this.sport;
	}
	
	public void setSport(final SportEntity sport)
	{
		this.sport = sport;
	}
	
	public UserEntity getUserSppoti()
	{
		return this.userSppoti;
	}
	
	public void setUserSppoti(final UserEntity userSppoti)
	{
		this.userSppoti = userSppoti;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}
	
	public Integer getMaxTeamCount()
	{
		return this.maxTeamCount;
	}
	
	public void setMaxTeamCount(final Integer maxTeamCount)
	{
		this.maxTeamCount = maxTeamCount;
	}
	
	public Date getDateTimeStart()
	{
		return this.dateTimeStart;
	}
	
	public void setDateTimeStart(final Date dateTimeStart)
	{
		this.dateTimeStart = dateTimeStart;
	}
	
	public String getTags()
	{
		return this.tags;
	}
	
	public void setTags(final String tags)
	{
		this.tags = tags;
	}
	
	public TeamEntity getTeamHostEntity()
	{
		return this.teamHostEntity;
	}
	
	public void setTeamHostEntity(final TeamEntity teamHostEntity)
	{
		this.teamHostEntity = teamHostEntity;
	}
	
	public boolean isDeleted()
	{
		return this.deleted;
	}
	
	public void setDeleted(final boolean deleted)
	{
		this.deleted = deleted;
	}
	
	public Set<SppoterEntity> getSppotiMembers()
	{
		return this.sppotiMembers;
	}
	
	public void setSppotiMembers(final Set<SppoterEntity> sppotiMembers)
	{
		this.sppotiMembers = sppotiMembers;
	}
	
	public String getCover()
	{
		return this.cover;
	}
	
	public void setCover(final String cover)
	{
		this.cover = cover;
	}
	
	public Long getSppotiDuration()
	{
		return this.sppotiDuration;
	}
	
	public void setSppotiDuration(final Long sppotiDuration)
	{
		this.sppotiDuration = sppotiDuration;
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
	
	public Set<SppotiAdverseEntity> getAdverseTeams()
	{
		return this.adverseTeams;
	}
	
	public void setAdverseTeams(final Set<SppotiAdverseEntity> adverseTeams)
	{
		this.adverseTeams = adverseTeams;
	}
	
	public Long getConnectedUserId()
	{
		return this.connectedUserId;
	}
	
	public void setConnectedUserId(final Long connectedUserId)
	{
		this.connectedUserId = connectedUserId;
	}
	
	public String getTimeZone() {
		return this.timeZone;
	}
	
	public void setTimeZone(final String timeZone) {
		this.timeZone = timeZone;
	}
	
	public GlobalAppStatusEnum getStatus() {
		return this.status;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;
		
		final SppotiEntity that = (SppotiEntity) o;
		
		if (this.deleted != that.deleted)
			return false;
		if (this.name != null ? !this.name.equals(that.name) : that.name != null)
			return false;
		if (this.description != null ? !this.description.equals(that.description) : that.description != null)
			return false;
		if (this.datetimeCreated != null ? !this.datetimeCreated.equals(that.datetimeCreated) :
				that.datetimeCreated != null)
			return false;
		if (this.dateTimeStart != null ? !this.dateTimeStart.equals(that.dateTimeStart) : that.dateTimeStart != null)
			return false;
		if (this.location != null ? !this.location.equals(that.location) : that.location != null)
			return false;
		if (this.maxTeamCount != null ? !this.maxTeamCount.equals(that.maxTeamCount) : that.maxTeamCount != null)
			return false;
		if (this.sppotiDuration != null ? !this.sppotiDuration.equals(that.sppotiDuration) :
				that.sppotiDuration != null)
			return false;
		if (this.cover != null ? !this.cover.equals(that.cover) : that.cover != null)
			return false;
		if (this.tags != null ? !this.tags.equals(that.tags) : that.tags != null)
			return false;
		if (this.altitude != null ? !this.altitude.equals(that.altitude) : that.altitude != null)
			return false;
		if (this.timeZone != null ? !this.timeZone.equals(that.timeZone) : that.timeZone != null)
			return false;
		if (!this.status.equals(that.status))
			return false;
		return this.longitude != null ? this.longitude.equals(that.longitude) : that.longitude == null;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
		result = 31 * result + (this.description != null ? this.description.hashCode() : 0);
		result = 31 * result + (this.datetimeCreated != null ? this.datetimeCreated.hashCode() : 0);
		result = 31 * result + (this.dateTimeStart != null ? this.dateTimeStart.hashCode() : 0);
		result = 31 * result + (this.location != null ? this.location.hashCode() : 0);
		result = 31 * result + (this.cover != null ? this.cover.hashCode() : 0);
		result = 31 * result + (this.maxTeamCount != null ? this.maxTeamCount.hashCode() : 0);
		result = 31 * result + (this.tags != null ? this.tags.hashCode() : 0);
		result = 31 * result + (this.deleted ? 1 : 0);
		result = 31 * result + (this.sppotiDuration != null ? this.sppotiDuration.hashCode() : 0);
		result = 31 * result + (this.altitude != null ? this.altitude.hashCode() : 0);
		result = 31 * result + (this.longitude != null ? this.longitude.hashCode() : 0);
		result = 31 * result + (this.timeZone != null ? this.timeZone.hashCode() : 0);
		result = 31 * result + this.status.hashCode();
		return result;
	}
}
