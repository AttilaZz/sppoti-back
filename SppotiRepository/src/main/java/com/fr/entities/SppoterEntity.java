package com.fr.entities;

import com.fr.commons.enumeration.GlobalAppStatusEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by djenanewail on 2/4/17.
 */
@Entity
@Table(name = "SPPOTER")
public class SppoterEntity extends AbstractCommonEntity
{
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private GlobalAppStatusEnum status = GlobalAppStatusEnum.PENDING;
	
	private Boolean requestSentFromUser = Boolean.FALSE;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date invitationDate = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date acceptationDate;
	
	private Integer xPosition;
	private Integer yPosition;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "sppoti_id")
	private SppotiEntity sppoti;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "team_member_id")
	private TeamMemberEntity teamMember;
	
	private Boolean hasRateOtherSppoter = Boolean.FALSE;
	
	public GlobalAppStatusEnum getStatus() {
		return this.status;
	}
	
	public void setStatus(final GlobalAppStatusEnum status) {
		this.status = status;
	}
	
	public Date getAcceptationDate() {
		return this.acceptationDate;
	}
	
	public void setAcceptationDate(final Date acceptationDate) {
		this.acceptationDate = acceptationDate;
	}
	
	public Date getInvitationDate() {
		return this.invitationDate;
	}
	
	public void setInvitationDate(final Date invitationDate) {
		this.invitationDate = invitationDate;
	}
	
	public SppotiEntity getSppoti() {
		return this.sppoti;
	}
	
	public void setSppoti(final SppotiEntity sppoti) {
		this.sppoti = sppoti;
	}
	
	public Integer getxPosition() {
		return this.xPosition;
	}
	
	public void setxPosition(final Integer xPosition) {
		this.xPosition = xPosition;
	}
	
	public Integer getyPosition() {
		return this.yPosition;
	}
	
	public void setyPosition(final Integer yPosition) {
		this.yPosition = yPosition;
	}
	
	public TeamMemberEntity getTeamMember() {
		return this.teamMember;
	}
	
	public void setTeamMember(final TeamMemberEntity teamMember) {
		this.teamMember = teamMember;
	}
	
	public Boolean getHasRateOtherSppoter() {
		return this.hasRateOtherSppoter;
	}
	
	public void setHasRateOtherSppoter(final Boolean hasRateOtherSppoter) {
		this.hasRateOtherSppoter = hasRateOtherSppoter;
	}
	
	public Boolean getRequestSentFromUser() {
		return this.requestSentFromUser;
	}
	
	public void setRequestSentFromUser(final Boolean requestSentFromUser) {
		this.requestSentFromUser = requestSentFromUser;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;
		
		final SppoterEntity that = (SppoterEntity) o;
		
		if (this.status != that.status)
			return false;
		if (this.invitationDate != null ? !this.invitationDate.equals(that.invitationDate) :
				that.invitationDate != null)
			return false;
		if (this.acceptationDate != null ? !this.acceptationDate.equals(that.acceptationDate) :
				that.acceptationDate != null)
			return false;
		if (this.xPosition != null ? !this.xPosition.equals(that.xPosition) : that.xPosition != null)
			return false;
		if (this.yPosition != null ? !this.yPosition.equals(that.yPosition) : that.yPosition != null)
			return false;
		return this.hasRateOtherSppoter != null ? this.hasRateOtherSppoter.equals(that.hasRateOtherSppoter) :
				that.hasRateOtherSppoter == null;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
		result = 31 * result + (getInvitationDate() != null ? getInvitationDate().hashCode() : 0);
		result = 31 * result + (getAcceptationDate() != null ? getAcceptationDate().hashCode() : 0);
		result = 31 * result + (getxPosition() != null ? getxPosition().hashCode() : 0);
		result = 31 * result + (getyPosition() != null ? getyPosition().hashCode() : 0);
		result = 31 * result + (getHasRateOtherSppoter() != null ? getHasRateOtherSppoter().hashCode() : 0);
		return result;
	}
}
