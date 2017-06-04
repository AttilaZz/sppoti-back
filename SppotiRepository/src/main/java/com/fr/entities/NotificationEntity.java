package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.NotificationTypeEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: Wail DJENANE on Nov 10, 2016
 */
@Entity
@Table(name = "NOTIFICATION")
@JsonInclude(Include.NON_EMPTY)
public class NotificationEntity extends AbstractCommonEntity
{
	
	@ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "from_user_id")
	private UserEntity from;
	
	@ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "to_user_id")
	private UserEntity to;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, columnDefinition = "DATETIME(6)", updatable = false)
	private Date creationDate = new Date();
	
	@Enumerated(EnumType.STRING)
	private NotificationTypeEnum notificationType;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private GlobalAppStatusEnum status = GlobalAppStatusEnum.UNREAD;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private TeamEntity team;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sppoti_id")
	private SppotiEntity sppoti;
	
	public UserEntity getFrom()
	{
		return this.from;
	}
	
	public void setFrom(final UserEntity from)
	{
		this.from = from;
	}
	
	public UserEntity getTo()
	{
		return this.to;
	}
	
	public void setTo(final UserEntity to)
	{
		this.to = to;
	}
	
	public Date getCreationDate()
	{
		return this.creationDate;
	}
	
	public void setCreationDate(final Date creationDate)
	{
		this.creationDate = creationDate;
	}
	
	public NotificationTypeEnum getNotificationType()
	{
		return this.notificationType;
	}
	
	public void setNotificationType(final NotificationTypeEnum notificationType)
	{
		this.notificationType = notificationType;
	}
	
	public TeamEntity getTeam()
	{
		return this.team;
	}
	
	public void setTeam(final TeamEntity team)
	{
		this.team = team;
	}
	
	public SppotiEntity getSppoti()
	{
		return this.sppoti;
	}
	
	public void setSppoti(final SppotiEntity sppoti)
	{
		this.sppoti = sppoti;
	}
	
	public GlobalAppStatusEnum getStatus()
	{
		return this.status;
	}
	
	public void setStatus(final GlobalAppStatusEnum status)
	{
		this.status = status;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof NotificationEntity))
			return false;
		if (!super.equals(o))
			return false;
		
		final NotificationEntity that = (NotificationEntity) o;
		
		if (this.status != that.status)
			return false;
		if (this.creationDate != null ? !this.creationDate.equals(that.creationDate) : that.creationDate != null)
			return false;
		return this.notificationType == that.notificationType;
		
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + (this.creationDate != null ? this.creationDate.hashCode() : 0);
		result = 31 * result + (this.notificationType != null ? this.notificationType.hashCode() : 0);
		result = 31 * result + this.status.hashCode();
		return result;
	}
}
