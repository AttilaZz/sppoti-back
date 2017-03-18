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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GlobalAppStatus status = GlobalAppStatus.PENDING;

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

    private Boolean hasRateOtherSppoter;

    public GlobalAppStatus getStatus() {
        return status;
    }

    public void setStatus(GlobalAppStatus status) {
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

    public Boolean getHasRateOtherSppoter() {
        return hasRateOtherSppoter;
    }

    public void setHasRateOtherSppoter(Boolean hasRateOtherSppoter) {
        this.hasRateOtherSppoter = hasRateOtherSppoter;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (invitationDate != null ? invitationDate.hashCode() : 0);
        result = 31 * result + (acceptationDate != null ? acceptationDate.hashCode() : 0);
        result = 31 * result + (xPosition != null ? xPosition.hashCode() : 0);
        result = 31 * result + (yPosition != null ? yPosition.hashCode() : 0);
        result = 31 * result + (hasRateOtherSppoter != null ? 1: 0);
        return result;
    }
}
