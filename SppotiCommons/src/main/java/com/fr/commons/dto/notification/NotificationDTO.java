package com.fr.commons.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fr.commons.dto.*;
import com.fr.commons.dto.post.PostDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.NotificationStatus;
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
	private NotificationStatus status;
	
	private String teamId;
	private String sppotiId;
	private String postId;
	
	private TeamDTO team;
	private SppotiDTO sppoti;
	private PostDTO post;
	private CommentDTO comment;
	private RatingDTO rating;
	private ScoreDTO score;
	
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
	
	public NotificationStatus getStatus()
	{
		return this.status;
	}
	
	public void setStatus(final NotificationStatus status)
	{
		this.status = status;
	}
	
	public String getTeamId() {
		return this.teamId;
	}
	
	public void setTeamId(final String teamId) {
		this.teamId = teamId;
	}
	
	public String getSppotiId() {
		return this.sppotiId;
	}
	
	public void setSppotiId(final String sppotiId) {
		this.sppotiId = sppotiId;
	}
	
	public String getPostId() {
		return this.postId;
	}
	
	public void setPostId(final String postId) {
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
	
	public RatingDTO getRating() {
		return this.rating;
	}
	
	public void setRating(final RatingDTO rating) {
		this.rating = rating;
	}
	
	public ScoreDTO getScore() {
		return this.score;
	}
	
	public void setScore(final ScoreDTO score) {
		this.score = score;
	}
}


