package com.fr.entities;

import com.fr.models.GlobalAppStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Created by djenanewail on 2/4/17.
 */
@Entity
@Table(name = "TEAM_MEMBER")
public class TeamMemberEntity
{

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private int uuid = UUID.randomUUID().hashCode();

    @Column(nullable = false)
    private String status = GlobalAppStatus.PENDING.name();

    @Column(nullable = false)
    private Date joinDate;

    @Column(nullable = false)
    private Date invitationDate = new Date();

    private Integer xPosition;
    private Integer yPosition;

    @Column(nullable = false)
    private Boolean admin = false;

    @Column(nullable = false)
    private Boolean teamCaptain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity users;

    @OneToMany(mappedBy = "teamMember", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SppotiMember> sppotiMembers;

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

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public Set<SppotiMember> getSppotiMembers() {
        return sppotiMembers;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public void setSppotiMembers(Set<SppotiMember> sppotiMembers) {
        this.sppotiMembers = sppotiMembers;
    }

    public Boolean getTeamCaptain() {
        return teamCaptain;
    }

    public void setTeamCaptain(Boolean teamCaptain) {
        this.teamCaptain = teamCaptain;
    }
}

