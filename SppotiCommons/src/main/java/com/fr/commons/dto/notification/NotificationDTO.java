package com.fr.commons.dto.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiResponseDTO;
import com.fr.commons.dto.team.TeamResponseDTO;

import java.util.Date;

/**
 * Created by djenanewail on 2/11/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDTO extends AbstractCommonDTO {

    private UserDTO from;
    private UserDTO to;
    private Integer notificationType;
    private Boolean opened;

    private Integer teamId;
    private Integer sppotiId;
    private Integer postId;

    private TeamResponseDTO teamResponseDTO;
    private SppotiResponseDTO sppotiResponseDTO;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private Date datetime;

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

    public Date getDatetime()
    {
        return datetime;
    }
    public void setDatetime(Date datetime)
    {
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

    public TeamResponseDTO getTeamResponseDTO() {
        return teamResponseDTO;
    }

    public void setTeamResponseDTO(TeamResponseDTO teamResponseDTO) {
        this.teamResponseDTO = teamResponseDTO;
    }

    public SppotiResponseDTO getSppotiResponseDTO() {
        return sppotiResponseDTO;
    }

    public void setSppotiResponseDTO(SppotiResponseDTO sppotiResponseDTO) {
        this.sppotiResponseDTO = sppotiResponseDTO;
    }
}


