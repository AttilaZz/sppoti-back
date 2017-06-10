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
	private int visibility;
	
	private Data content;
	
	@JsonProperty("targetUser")
	private Integer targetUserUuid;
	
	public int getVisibility()
	{
		return this.visibility;
	}
	
	public void setVisibility(final int visibility)
	{
		this.visibility = visibility;
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
	
	public Integer getTargetUserUuid()
	{
		return this.targetUserUuid;
	}
	
	public void setTargetUserUuid(final Integer targetUserUuid)
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
