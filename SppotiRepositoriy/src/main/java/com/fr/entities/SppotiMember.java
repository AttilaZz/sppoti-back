package com.fr.entities;

import com.fr.models.GlobalAppStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by djenanewail on 2/4/17.
 */
@Entity
@Table(name = "SPPOTI_MEMBER")
public class SppotiMember
        extends AbstractCommonEntity {

    private String status = GlobalAppStatus.PENDING.name();

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getAcceptationDate() {
        return acceptationDate;
    }

    public void setAcceptationDate(Date acceptationDate) {
        this.acceptationDate = acceptationDate;
    }

    public Date getInvitationDate() {
        return invitationDate;
    }

    public void setInvitationDate(Date invitationDate) {
        this.invitationDate = invitationDate;
    }

    public SppotiEntity getSppoti() {
        return sppoti;
    }

    public void setSppoti(SppotiEntity sppoti) {
        this.sppoti = sppoti;
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

    public TeamMemberEntity getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(TeamMemberEntity teamMember) {
        this.teamMember = teamMember;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SppotiMember)) return false;
        if (!super.equals(o)) return false;

        SppotiMember that = (SppotiMember) o;

        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (invitationDate != null ? !invitationDate.equals(that.invitationDate) : that.invitationDate != null)
            return false;
        if (acceptationDate != null ? !acceptationDate.equals(that.acceptationDate) : that.acceptationDate != null)
            return false;
        if (xPosition != null ? !xPosition.equals(that.xPosition) : that.xPosition != null) return false;
        return yPosition != null ? yPosition.equals(that.yPosition) : that.yPosition == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (invitationDate != null ? invitationDate.hashCode() : 0);
        result = 31 * result + (acceptationDate != null ? acceptationDate.hashCode() : 0);
        result = 31 * result + (xPosition != null ? xPosition.hashCode() : 0);
        result = 31 * result + (yPosition != null ? yPosition.hashCode() : 0);
        return result;
    }
}
