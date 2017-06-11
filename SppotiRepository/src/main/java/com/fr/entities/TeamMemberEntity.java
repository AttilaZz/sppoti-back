package com.fr.entities;

import com.fr.commons.enumeration.GlobalAppStatusEnum;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by djenanewail on 2/4/17.
 */
@Entity
@Table(name = "TEAM_MEMBER")
public class TeamMemberEntity extends AbstractCommonEntity
{
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Where(clause = "status!='DELETED'")
	private GlobalAppStatusEnum status = GlobalAppStatusEnum.PENDING;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date joinDate;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date invitationDate = new Date();
	
	private Integer xPosition;
	private Integer yPosition;
	
	@Column(nullable = false)
	private Boolean admin = false;
	
	@Column(nullable = false)
	private Boolean teamCaptain = false;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "team_id")
	private TeamEntity team;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	@OneToMany(mappedBy = "teamMember", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<SppoterEntity> sppotiMembers;
	
	public GlobalAppStatusEnum getStatus() {
		return this.status;
	}
	
	public void setStatus(final GlobalAppStatusEnum status) {
		this.status = status;
	}
	
	public Date getJoinDate() {
		return this.joinDate;
	}
	
	public void setJoinDate(final Date joinDate) {
		this.joinDate = joinDate;
	}
	
	public Date getInvitationDate() {
		return this.invitationDate;
	}
	
	public void setInvitationDate(final Date invitationDate) {
		this.invitationDate = invitationDate;
	}
	
	public TeamEntity getTeam() {
		return this.team;
	}
	
	public void setTeam(final TeamEntity team) {
		this.team = team;
	}
	
	public UserEntity getUser() {
		return this.user;
	}
	
	public void setUser(final UserEntity user) {
		this.user = user;
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
	
	public Set<SppoterEntity> getSppotiMembers() {
		return this.sppotiMembers;
	}
	
	public void setSppotiMembers(final Set<SppoterEntity> sppotiMembers) {
		this.sppotiMembers = sppotiMembers;
	}
	
	public Boolean getAdmin() {
		return this.admin;
	}
	
	public void setAdmin(final Boolean admin) {
		this.admin = admin;
	}
	
	public Boolean getTeamCaptain() {
		return this.teamCaptain;
	}
	
	public void setTeamCaptain(final Boolean teamCaptain) {
		this.teamCaptain = teamCaptain;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (!(o instanceof TeamMemberEntity))
			return false;
		if (!super.equals(o))
			return false;
		
		final TeamMemberEntity that = (TeamMemberEntity) o;
		
		if (this.status != null ? !this.status.equals(that.status) : that.status != null)
			return false;
		if (this.joinDate != null ? !this.joinDate.equals(that.joinDate) : that.joinDate != null)
			return false;
		if (this.invitationDate != null ? !this.invitationDate.equals(that.invitationDate) :
				that.invitationDate != null)
			return false;
		if (this.xPosition != null ? !this.xPosition.equals(that.xPosition) : that.xPosition != null)
			return false;
		if (this.yPosition != null ? !this.yPosition.equals(that.yPosition) : that.yPosition != null)
			return false;
		if (this.admin != null ? !this.admin.equals(that.admin) : that.admin != null)
			return false;
		return this.teamCaptain != null ? this.teamCaptain.equals(that.teamCaptain) : that.teamCaptain == null;
		
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (this.status != null ? this.status.hashCode() : 0);
		result = 31 * result + (this.joinDate != null ? this.joinDate.hashCode() : 0);
		result = 31 * result + (this.invitationDate != null ? this.invitationDate.hashCode() : 0);
		result = 31 * result + (this.xPosition != null ? this.xPosition.hashCode() : 0);
		result = 31 * result + (this.yPosition != null ? this.yPosition.hashCode() : 0);
		result = 31 * result + (this.admin != null ? this.admin.hashCode() : 0);
		result = 31 * result + (this.teamCaptain != null ? this.teamCaptain.hashCode() : 0);
		return result;
	}
}

