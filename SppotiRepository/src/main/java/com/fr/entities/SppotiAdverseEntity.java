package com.fr.entities;

import com.fr.commons.enumeration.GlobalAppStatusEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by djenanewail on 4/17/17.
 */
@Entity
@Table(name = "CHALLENGE")
public class SppotiAdverseEntity extends AbstractCommonEntity
{
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private GlobalAppStatusEnum status = GlobalAppStatusEnum.PENDING;
	
	@ManyToOne
	@JoinColumn(name = "sppoti_id", nullable = false)
	private SppotiEntity sppoti;
	
	@ManyToOne
	@JoinColumn(name = "team_id", nullable = false)
	private TeamEntity team;
	
	@Column(name = "from_sppoti_admin")
	private Boolean fromSppotiAdmin = false;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date")
	private Date creationDate = new Date();
	
	public GlobalAppStatusEnum getStatus()
	{
		return this.status;
	}
	
	public void setStatus(final GlobalAppStatusEnum status)
	{
		this.status = status;
	}
	
	public SppotiEntity getSppoti()
	{
		return this.sppoti;
	}
	
	public void setSppoti(final SppotiEntity sppoti)
	{
		this.sppoti = sppoti;
	}
	
	public TeamEntity getTeam()
	{
		return this.team;
	}
	
	public void setTeam(final TeamEntity team)
	{
		this.team = team;
	}
	
	public Boolean getFromSppotiAdmin()
	{
		return this.fromSppotiAdmin;
	}
	
	public void setFromSppotiAdmin(final Boolean fromSppotiAdmin)
	{
		this.fromSppotiAdmin = fromSppotiAdmin;
	}
	
	public Date getCreationDate()
	{
		return this.creationDate;
	}
	
	public void setCreationDate(final Date creationDate)
	{
		this.creationDate = creationDate;
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
		
		final SppotiAdverseEntity entity = (SppotiAdverseEntity) o;
		
		if (this.status != entity.status)
			return false;
		if (this.fromSppotiAdmin != null ? !this.fromSppotiAdmin.equals(entity.fromSppotiAdmin) :
				entity.fromSppotiAdmin != null)
			return false;
		return this.creationDate != null ? this.creationDate.equals(entity.creationDate) : entity.creationDate == null;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + (this.status != null ? this.status.hashCode() : 0);
		result = 31 * result + (this.fromSppotiAdmin != null ? this.fromSppotiAdmin.hashCode() : 0);
		result = 31 * result + (this.creationDate != null ? this.creationDate.hashCode() : 0);
		return result;
	}
}
