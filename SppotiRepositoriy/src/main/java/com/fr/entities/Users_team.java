package com.fr.entities;

import com.fr.models.GlobalAppStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by djenanewail on 2/4/17.
 */
@Entity
@Table
public class Users_team {

    @Id
    @GeneratedValue
    private Long id;

    private int uuid = UUID.randomUUID().variant();

    private String status = GlobalAppStatus.PENDING.name();

    private Date joinDate;

    private Date invitationDate = new Date();

    private Integer xPosition;
    private Integer yPosition;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team teams;

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

    public Team getTeams() {
        return teams;
    }

    public void setTeams(Team teams) {
        this.teams = teams;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
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

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }
}
