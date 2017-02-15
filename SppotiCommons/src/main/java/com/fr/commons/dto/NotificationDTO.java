package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * Created by djenanewail on 2/11/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDTO {

    private Integer id;
    private UserDTO from;
    private UserDTO to;
    private Integer notificationType;
    private Boolean opened;

    private Integer teamId;
    private Integer sppotiId;
    private Integer postId;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private String datetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserDTO getFrom() {
        return from;
    }

    public void setFrom(UserDTO from) {
        this.from = from;
    }

    public UserDTO getTo() {
        return to;
    }

    public void setTo(UserDTO to) {
        this.to = to;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Integer getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(Integer notificationType) {
        this.notificationType = notificationType;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getSppotiId() {
        return sppotiId;
    }

    public void setSppotiId(Integer sppotiId) {
        this.sppotiId = sppotiId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }
}


