package com.fr.entities;

import com.fr.models.GlobalAppStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by djenanewail on 2/4/17.
 */
@Entity
@Table
public class SppotiMember {

    @Id
    @GeneratedValue
    private Long id;

    private String status = GlobalAppStatus.PENDING.name();
    private Date invitationDate = new Date();
    private Date acceptationDate;
    private Integer xPosition;
    private Integer yPosition;

    @ManyToOne
    @JoinColumn(name = "sppoti_id")
    private Sppoti sppoti;

    @ManyToOne
    @JoinColumn(name = "sppoter_id")
    private TeamMembers usersTeam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Sppoti getSppoti() {
        return sppoti;
    }

    public void setSppoti(Sppoti sppoti) {
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

    public TeamMembers getUsersTeam() {
        return usersTeam;
    }

    public void setUsersTeam(TeamMembers usersTeam) {
        this.usersTeam = usersTeam;
    }
}
