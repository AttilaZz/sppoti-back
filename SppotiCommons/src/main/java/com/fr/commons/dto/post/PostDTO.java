package com.fr.commons.dto.post;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.utils.JsonDateSerializer;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by: Wail DJENANE on Aug 7, 2016
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostDTO extends AbstractCommonDTO
{
	
	private Long sportId;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date datetimeCreated;
	
	@JsonProperty("text")
	private String content;
	
	@JsonProperty("album")
	private Set<String> imageLink;
	
	@JsonProperty("video")
	private String videoLink;
	
	private List<CommentDTO> comment;
	private int commentsCount;
	
	private int likeCount;
	private boolean isLikedByUser;
	
	private boolean edited;
	
	private List<UserDTO> likers;
	
	private int visibility;
	
	private String firstName;
	private String lastName;
	private String username;
	private String avatar;
	
	private TargetUser targetUser;
	
	private boolean myPost;
	
	private String timeZone;
	
	public String getFirstName()
	{
		return this.firstName;
	}
	
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}
	
	public String getLastName()
	{
		return this.lastName;
	}
	
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public void setUsername(final String username)
	{
		this.username = username;
	}
	
	public List<UserDTO> getLikers()
	{
		return this.likers;
	}
	
	public void setLikers(final List<UserDTO> likers)
	{
		this.likers = likers;
	}
	
	public int getVisibility()
	{
		return this.visibility;
	}
	
	public void setVisibility(final int visibility)
	{
		this.visibility = visibility;
	}
	
	
	public String getContent()
	{
		return this.content;
	}
	
	public void setContent(final String content)
	{
		this.content = content;
	}
	
	
	public Set<String> getImageLink()
	{
		return this.imageLink;
	}
	
	public void setImageLink(final Set<String> imageLink)
	{
		this.imageLink = imageLink;
	}
	
	public String getVideoLink()
	{
		return this.videoLink;
	}
	
	public void setVideoLink(final String videoLink)
	{
		this.videoLink = videoLink;
	}
	
	public Long getSportId()
	{
		return this.sportId;
	}
	
	public void setSportId(final Long sportId)
	{
		this.sportId = sportId;
	}
	
	public int getLikeCount()
	{
		return this.likeCount;
	}
	
	public void setLikeCount(final int likeCount)
	{
		this.likeCount = likeCount;
	}
	
	public boolean isLikedByUser()
	{
		return this.isLikedByUser;
	}
	
	public void setLikedByUser(final boolean isLikedByUser)
	{
		this.isLikedByUser = isLikedByUser;
	}
	
	public boolean isEdited()
	{
		return this.edited;
	}
	
	public void setEdited(final boolean isEdited)
	{
		this.edited = isEdited;
	}
	
	public boolean isMyPost()
	{
		return this.myPost;
	}
	
	public void setMyPost(final boolean isMyPost)
	{
		this.myPost = isMyPost;
	}
	
	public int getCommentsCount()
	{
		return this.commentsCount;
	}
	
	public void setCommentsCount(final int commentsCount)
	{
		this.commentsCount = commentsCount;
	}
	
	public Date getDatetimeCreated()
	{
		return this.datetimeCreated;
	}
	
	public void setDatetimeCreated(final Date datetimeCreated)
	{
		this.datetimeCreated = datetimeCreated;
	}
	
	public List<CommentDTO> getComment()
	{
		return this.comment;
	}
	
	public void setComment(final List<CommentDTO> comment)
	{
		this.comment = comment;
	}
	
	public TargetUser getTargetUser()
	{
		return this.targetUser;
	}
	
	public void setTargetUser(final TargetUser targetUser)
	{
		this.targetUser = targetUser;
	}
	
	public String getAvatar()
	{
		return this.avatar;
	}
	
	public void setAvatar(final String avatar)
	{
		this.avatar = avatar;
	}
	
	public String getTimeZone() {
		return this.timeZone;
	}
	
	public void setTimeZone(final String timeZone) {
		this.timeZone = timeZone;
	}
	
	public void setTargetUser(final String firstName, final String lastName, final String username, final String id,
							  final boolean myAccount)
	{
		this.targetUser = new TargetUser(firstName, lastName, username, id, myAccount);
	}
	
	public class TargetUser
	{
		private String id;
		private String firstName;
		private String lastName;
		private String username;
		private boolean myAccount;
		
		public TargetUser()
		{
		}
		
		
		public TargetUser(final String firstName, final String lastName, final String username, final String id,
						  final boolean myAccount)
		{
			this.firstName = firstName;
			this.lastName = lastName;
			this.username = username;
			this.id = id;
			this.myAccount = myAccount;
		}
		
		public String getFirstName()
		{
			return this.firstName;
		}
		
		public String getLastName()
		{
			return this.lastName;
		}
		
		public String getUsername()
		{
			return this.username;
		}
		
		public String getId()
		{
			return this.id;
		}
		
		public boolean isMyAccount()
		{
			return this.myAccount;
		}
	}
}
