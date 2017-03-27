package com.fr.entities;

import com.fr.models.GlobalAppStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by djenanewail on 2/4/17.
 */
@Entity
@Table(name = "TEAM_MEMBER")
public class TeamMemberEntity
        extends AbstractCommonEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GlobalAppStatus status = GlobalAppStatus.PENDING;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    private TeamEntity team;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity users;

    @OneToMany(mappedBy = "teamMember", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SppotiMemberEntity> sppotiMembers;

    public GlobalAppStatus getStatus() {
        return status;
    }

    public void setStatus(GlobalAppStatus status) {
        this.status = status;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Date getInvitationDate() {
        return invitationDate;
    }

    public void setInvitationDate(Date invitationDate) {
        this.invitationDate = invitationDate;
    }

    public TeamEntity getTeam() {
        return team;
    }

    public void setTeam(TeamEntity team) {
        this.team = team;
    }

    public UserEntity getUsers() {
        return users;
    }

    public void setUsers(UserEntity users) {
        this.users = users;
    }

    public Integer getxPosition() {
        return xPosition;
    }

    public void setxPosition(Integer xPosition) {
        this.xPosition = xPosition;
    }

    public Integer getyPosition() {
        return yPosition;
    }

    public void setyPosition(Integer yPosition) {
        this.yPosition = yPosition;
    }

    public Set<SppotiMemberEntity> getSppotiMembers() {
        return sppotiMembers;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public void setSppotiMembers(Set<SppotiMemberEntity> sppotiMembers) {
        this.sppotiMembers = sppotiMembers;
    }

    public Boolean getTeamCaptain() {
        return teamCaptain;
    }

    public void setTeamCaptain(Boolean teamCaptain) {
        this.teamCaptain = teamCaptain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeamMemberEntity)) return false;
        if (!super.equals(o)) return false;

        TeamMemberEntity that = (TeamMemberEntity) o;

        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (joinDate != null ? !joinDate.equals(that.joinDate) : that.joinDate != null) return false;
        if (invitationDate != null ? !invitationDate.equals(that.invitationDate) : that.invitationDate != null)
            return false;
        if (xPosition != null ? !xPosition.equals(that.xPosition) : that.xPosition != null) return false;
        if (yPosition != null ? !yPosition.equals(that.yPosition) : that.yPosition != null) return false;
        if (admin != null ? !admin.equals(that.admin) : that.admin != null) return false;
        return teamCaptain != null ? teamCaptain.equals(that.teamCaptain) : that.teamCaptain == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (joinDate != null ? joinDate.hashCode() : 0);
        result = 31 * result + (invitationDate != null ? invitationDate.hashCode() : 0);
        result = 31 * result + (xPosition != null ? xPosition.hashCode() : 0);
        result = 31 * result + (yPosition != null ? yPosition.hashCode() : 0);
        result = 31 * result + (admin != null ? admin.hashCode() : 0);
        result = 31 * result + (teamCaptain != null ? teamCaptain.hashCode() : 0);
        return result;
    }
}

