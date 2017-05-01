package com.fr.commons.dto.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fr.commons.dto.AbstractCommonDTO;

import java.util.Set;

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */
@JsonInclude(Include.NON_ABSENT)
public class PostRequestDTO extends AbstractCommonDTO
{
	
	private Long sportId;
	
	private Long gameId;
	
	private Data content;
	
	private String avatar;
	
	private String cover;
	
	private int visibility;
	
	@JsonProperty("targetUser")
	private int targetUserUuid;
	
	public int getVisibility()
	{
		return this.visibility;
	}
	
	public void setVisibility(final int visibility)
	{
		this.visibility = visibility;
	}
	
	public String getAvatar()
	{
		return this.avatar;
	}
	
	public void setAvatar(final String avatar)
	{
		this.avatar = avatar;
	}
	
	public Long getGameId()
	{
		return this.gameId;
	}
	
	public void setGameId(final Long gameId)
	{
		this.gameId = gameId;
	}
	
	public Long getSportId()
	{
		return this.sportId;
	}
	
	public void setSportId(final Long sportId)
	{
		this.sportId = sportId;
	}
	
	public Data getContent()
	{
		return this.content;
	}
	
	public void setContent(final Data content)
	{
		this.content = content;
	}
	
	public String getNewAvatar()
	{
		return this.avatar;
	}
	
	public void setNewAvatar(final String newAvatar)
	{
		this.avatar = newAvatar;
	}
	
	public String getCover()
	{
		return this.cover;
	}
	
	public void setCover(final String cover)
	{
		this.cover = cover;
	}
	
	public int getTargetUserUuid()
	{
		return this.targetUserUuid;
	}
	
	public void setTargetUserUuid(final int targetUserUuid)
	{
		this.targetUserUuid = targetUserUuid;
	}
	
	public class Data
	{
		
		@JsonProperty("text")
		private String content;
		private Set<String> imageLink;
		private String videoLink;
		
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
		
	}
}
