package com.fr.entities;

import com.fr.models.GlobalAppStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by djenanewail on 2/4/17.
 */
@Entity
@Table(name = "sppoters")
public class Users_sppoti {

    @Id
    @GeneratedValue
    private Long id;

    private String status = GlobalAppStatus.PENDING.name();
    private Date invitationDate = new Date();

    private Date acceptationDate;
    private String xPosition;
    private String yPosition;

    @ManyToOne
    @JoinColumn(name = "sppoti_id")
    private Sppoti sppotis;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

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

    public Sppoti getSppotis() {
        return sppotis;
    }

    public void setSppotis(Sppoti sppotis) {
        this.sppotis = sppotis;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getxPosition() {
        return xPosition;
    }

    public void setxPosition(String xPosition) {
        this.xPosition = xPosition;
    }

    public String getyPosition() {
        return yPosition;
    }

    public void setyPosition(String yPosition) {
        this.yPosition = yPosition;
    }
}
