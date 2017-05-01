package com.fr.commons.dto.post;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.HeaderDataDTO;

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
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+1")
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
	
	private List<HeaderDataDTO> likers;
	
	private int visibility;
	
	private String firstName;
	private String lastName;
	private String username;
	private String avatar;
	
	private TargetUser targetUser;
	
	private boolean myPost;
	
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
	
	public PostDTO()
	{
	
	}
	
	public int getVisibility()
	{
		return this.visibility;
	}
	
	public void setVisibility(final int visibility)
	{
		this.visibility = visibility;
	}
	
	public List<HeaderDataDTO> getLikers()
	{
		return this.likers;
	}
	
	public void setLikers(final List<HeaderDataDTO> likers)
	{
		this.likers = likers;
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
	
	public void setTargetUser(final TargetUser targetUser)
	{
		this.targetUser = targetUser;
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
	
	public String getAvatar()
	{
		return this.avatar;
	}
	
	public void setAvatar(final String avatar)
	{
		this.avatar = avatar;
	}
	
	public void setTargetUser(final String firstName, final String lastName, final String username, final int id,
							  final boolean myAccount)
	{
		this.targetUser = new TargetUser(firstName, lastName, username, id, myAccount);
	}
	
	public class TargetUser
	{
		public TargetUser()
		{
		}
		
		private int id;
		private String firstName;
		private String lastName;
		private String username;
		
		private boolean myAccount;
		
		
		public TargetUser(final String firstName, final String lastName, final String username, final int id,
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
		
		public int getId()
		{
			return this.id;
		}
		
		public boolean isMyAccount()
		{
			return this.myAccount;
		}
	}
}
