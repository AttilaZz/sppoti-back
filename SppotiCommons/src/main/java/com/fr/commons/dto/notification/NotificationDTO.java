package com.fr.commons.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.post.PostDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.utils.JsonDateSerializer;

import java.util.Date;

/**
 * Created by djenanewail on 2/11/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDTO extends AbstractCommonDTO
{
	
	private UserDTO from;
	private UserDTO to;
	private Integer notificationType;
	private GlobalAppStatusEnum status;
	
	private String teamId;
	private String sppotiId;
	private String postId;
	
	private TeamDTO team;
	private SppotiDTO sppoti;
	private PostDTO post;
	private CommentDTO comment;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date datetime;
	
	public UserDTO getFrom()
	{
		return this.from;
	}
	
	public void setFrom(final UserDTO from)
	{
		this.from = from;
	}
	
	public UserDTO getTo()
	{
		return this.to;
	}
	
	public void setTo(final UserDTO to)
	{
		this.to = to;
	}
	
	public Date getDatetime()
	{
		return this.datetime;
	}
	
	public void setDatetime(final Date datetime)
	{
		this.datetime = datetime;
	}
	
	public Integer getNotificationType()
	{
		return this.notificationType;
	}
	
	public void setNotificationType(final Integer notificationType)
	{
		this.notificationType = notificationType;
	}
	
	public GlobalAppStatusEnum getStatus()
	{
		return this.status;
	}
	
	public void setStatus(final GlobalAppStatusEnum status)
	{
		this.status = status;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getSppotiId() {
		return sppotiId;
	}

	public void setSppotiId(String sppotiId) {
		this.sppotiId = sppotiId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public TeamDTO getTeam()
	{
		return this.team;
	}
	
	public void setTeam(final TeamDTO team)
	{
		this.team = team;
	}
	
	public SppotiDTO getSppoti()
	{
		return this.sppoti;
	}
	
	public void setSppoti(final SppotiDTO sppoti)
	{
		this.sppoti = sppoti;
	}
	
	public PostDTO getPost() {
		return this.post;
	}
	
	public void setPost(final PostDTO post) {
		this.post = post;
	}
	
	public CommentDTO getComment() {
		return this.comment;
	}
	
	public void setComment(final CommentDTO comment) {
		this.comment = comment;
	}
}


