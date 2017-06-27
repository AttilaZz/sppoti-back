package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fr.commons.enumeration.TeamStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by djenanewail on 1/21/17.
 */

@Entity
@Table(name = "TEAM")
public class TeamEntity extends AbstractCommonEntity
{
	
	@Column(nullable = false)
	private String name;
	private String logoPath;
	private String coverPath;
	
	@Column(nullable = false, columnDefinition = "varchar (255) default '#30d3c2'")
	private String color = "#30d3c2";
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date creationDate = new Date();
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "team")
	private Set<TeamMemberEntity> teamMembers;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "sport_id")
	private SportEntity sport;
	
	@OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<NotificationEntity> notificationEntities;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TeamStatus type = TeamStatus.PUBLIC;
	
	/**
	 * When creating a host team from a sppotigit co, we need a reference to the sppoti,
	 * in order to save team host ang get i's id, otherwise, we get a transient exception.
	 */
	@OneToMany(mappedBy = "teamHostEntity", cascade = CascadeType.ALL)
	private Set<SppotiEntity> sppotiEntity;
	
	@Column(name = "deleted")
	private boolean deleted = false;
	
	@Column(name = "time_zone")
	private String timeZone;
	
	/**
	 * Determine with sppoti is linked to the team when performing transformation of
	 */
	private transient Long relatedSppotiId;
	
	public String getName() {
		return this.name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getLogoPath() {
		return this.logoPath;
	}
	
	public void setLogoPath(final String logoPath) {
		this.logoPath = logoPath;
	}
	
	public String getCoverPath() {
		return this.coverPath;
	}
	
	public void setCoverPath(final String coverPath) {
		this.coverPath = coverPath;
	}
	
	public Set<TeamMemberEntity> getTeamMembers() {
		return this.teamMembers;
	}
	
	public void setTeamMembers(final Set<TeamMemberEntity> teamMembers) {
		this.teamMembers = teamMembers;
	}
	
	public SportEntity getSport() {
		return this.sport;
	}
	
	public void setSport(final SportEntity sport) {
		this.sport = sport;
	}
	
	public boolean isDeleted() {
		return this.deleted;
	}
	
	public void setDeleted(final boolean deleted) {
		this.deleted = deleted;
	}
	
	public Set<SppotiEntity> getSppotiEntity() {
		return this.sppotiEntity;
	}
	
	public void setSppotiEntity(final Set<SppotiEntity> sppotiEntity) {
		this.sppotiEntity = sppotiEntity;
	}
	
	public Set<NotificationEntity> getNotificationEntities() {
		return this.notificationEntities;
	}
	
	public void setNotificationEntities(final Set<NotificationEntity> notificationEntities) {
		this.notificationEntities = notificationEntities;
	}
	
	public Date getCreationDate() {
		return this.creationDate;
	}
	
	public void setCreationDate(final Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public void setColor(final String color) {
		this.color = color;
	}
	
	public Long getRelatedSppotiId() {
		return this.relatedSppotiId;
	}
	
	public void setRelatedSppotiId(final Long relatedSppotiId) {
		this.relatedSppotiId = relatedSppotiId;
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
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (!(o instanceof TeamEntity))
			return false;
		if (!super.equals(o))
			return false;
		
		final TeamEntity that = (TeamEntity) o;
		
		if (this.deleted != that.deleted)
			return false;
		if (this.timeZone != null ? !this.timeZone.equals(that.timeZone) : that.timeZone != null)
			return false;
		if (this.name != null ? !this.name.equals(that.name) : that.name != null)
			return false;
		if (this.logoPath != null ? !this.logoPath.equals(that.logoPath) : that.logoPath != null)
			return false;
		if (this.creationDate != null ? !this.creationDate.equals(that.creationDate) : that.creationDate != null)
			return false;
		if (this.color != null ? !this.color.equals(that.color) : that.color != null)
			return false;
		if (!this.type.equals(that.type))
			return false;
		return this.coverPath != null ? this.coverPath.equals(that.coverPath) : that.coverPath == null;
		
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
		result = 31 * result + (this.timeZone != null ? this.timeZone.hashCode() : 0);
		result = 31 * result + (this.logoPath != null ? this.logoPath.hashCode() : 0);
		result = 31 * result + (this.coverPath != null ? this.coverPath.hashCode() : 0);
		result = 31 * result + (this.creationDate != null ? this.creationDate.hashCode() : 0);
		result = 31 * result + (this.color != null ? this.color.hashCode() : 0);
		result = 31 * result + this.type.hashCode();
		result = 31 * result + (this.deleted ? 1 : 0);
		return result;
	}
}
