package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fr.commons.utils.JsonDateSerializer;

import java.util.Date;
import java.util.List;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */
@JsonInclude(Include.NON_EMPTY)
public class CommentDTO extends AbstractCommonDTO
{
	
	private String postId;
	
	private String authorId;
	private String authorFirstName;
	private String authorLastName;
	private String authorUsername;
	private String authorAvatar;
	private String authorCover;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date creationDate;
	
	private String text;
	private String imageLink;
	private String videoLink;
	
	private boolean isMyComment;
	
	private boolean isLikedByUser;
	private int likeCount;
	
	private boolean isEdited = false;
	
	private List<HeaderDataDTO> commentLikers;
	
	private String timeZone;
	
	public List<HeaderDataDTO> getCommentLikers()
	{
		return this.commentLikers;
	}
	
	public void setCommentLikers(final List<HeaderDataDTO> commentLikers)
	{
		this.commentLikers = commentLikers;
	}
	
	public String getAuthorUsername()
	{
		return this.authorUsername;
	}
	
	public void setAuthorUsername(final String authorUsername)
	{
		this.authorUsername = authorUsername;
	}
	
	public String getAuthorLastName()
	{
		return this.authorLastName;
	}
	
	public void setAuthorLastName(final String authorLastName)
	{
		this.authorLastName = authorLastName;
	}
	
	public String getAuthorFirstName()
	{
		return this.authorFirstName;
	}
	
	public void setAuthorFirstName(final String authorFirstName)
	{
		this.authorFirstName = authorFirstName;
	}
	
	public String getAuthorAvatar()
	{
		return this.authorAvatar;
	}
	
	public void setAuthorAvatar(final String authorAvatar)
	{
		this.authorAvatar = authorAvatar;
	}
	
	public Date getCreationDate()
	{
		return this.creationDate;
	}
	
	public void setCreationDate(final Date creationDate)
	{
		this.creationDate = creationDate;
	}
	
	public String getText()
	{
		return this.text;
	}
	
	public void setText(final String text)
	{
		this.text = text;
	}
	
	public String getImageLink()
	{
		return this.imageLink;
	}
	
	public void setImageLink(final String imageLink)
	{
		this.imageLink = imageLink;
	}
	
	public boolean isMyComment()
	{
		return this.isMyComment;
	}
	
	public void setMyComment(final boolean isMyComment)
	{
		this.isMyComment = isMyComment;
	}
	
	public String getVideoLink()
	{
		return this.videoLink;
	}
	
	public void setVideoLink(final String videoLink)
	{
		this.videoLink = videoLink;
	}
	
	public boolean isLikedByUser()
	{
		return this.isLikedByUser;
	}
	
	public void setLikedByUser(final boolean isLikedByUser)
	{
		this.isLikedByUser = isLikedByUser;
	}
	
	public int getLikeCount()
	{
		return this.likeCount;
	}
	
	public void setLikeCount(final int likeCount)
	{
		this.likeCount = likeCount;
	}
	
	public boolean isEdited()
	{
		return this.isEdited;
	}
	
	public void setEdited(final boolean isEdited)
	{
		this.isEdited = isEdited;
	}
	
	public String getPostId()
	{
		return this.postId;
	}
	
	public void setPostId(final String postId)
	{
		this.postId = postId;
	}
	
	public String getTimeZone() {
		return this.timeZone;
	}
	
	public void setTimeZone(final String timeZone) {
		this.timeZone = timeZone;
	}
	
	public String getAuthorCover() {
		return this.authorCover;
	}
	
	public void setAuthorCover(final String authorCover) {
		this.authorCover = authorCover;
	}
	
	public String getAuthorId() {
		return this.authorId;
	}
	
	public void setAuthorId(final String authorId) {
		this.authorId = authorId;
	}
}
