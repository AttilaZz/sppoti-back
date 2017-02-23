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
    private Date invitationDate = new Date();
    private Date acceptationDate;
    private Integer xPosition;
    private Integer yPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sppoti_id")
    private SppotiEntity sppoti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sppoter_id")
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
}
