package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.commons.dto.notification.NotificationDTO;

import java.util.List;

/**
 * Created by: Wail DJENANE on Oct 17, 2016
 */
@JsonInclude(value = Include.NON_DEFAULT)
public class HeaderDataDTO extends AbstractCommonDTO
{
	
	private String firstName;
	private String lastName;
	
	private Long avatarId;
	private String avatar;
	
	private Long coverId;
	private String cover;
	private int coverType;
	
	private String username;
	private int nbNotif;
	
	private int pendingFriendRequestCount;
	
	private int confirmedFriendRequestCount;
	
	private List<NotificationDTO> notifList;//post-like like + content share + tag
	private int notifListCount;
	
	public HeaderDataDTO()
	{
		super();
	}
	
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
	
	public Long getAvatarId()
	{
		return this.avatarId;
	}
	
	public void setAvatarId(final Long avatarId)
	{
		this.avatarId = avatarId;
	}
	
	public String getAvatar()
	{
		return this.avatar;
	}
	
	public void setAvatar(final String avatar)
	{
		this.avatar = avatar;
	}
	
	public Long getCoverId()
	{
		return this.coverId;
	}
	
	public void setCoverId(final Long coverId)
	{
		this.coverId = coverId;
	}
	
	public String getCover()
	{
		return this.cover;
	}
	
	public void setCover(final String cover)
	{
		this.cover = cover;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public void setUsername(final String username)
	{
		this.username = username;
	}
	
	public int getNbNotif()
	{
		return this.nbNotif;
	}
	
	public void setNbNotif(final int nbNotif)
	{
		this.nbNotif = nbNotif;
	}
	
	public int getCoverType()
	{
		return this.coverType;
	}
	
	public void setCoverType(final int coverType)
	{
		this.coverType = coverType;
	}
	
	public int getPendingFriendRequestCount()
	{
		return this.pendingFriendRequestCount;
	}
	
	public void setPendingFriendRequestCount(final int pendingFriendRequestCount)
	{
		this.pendingFriendRequestCount = pendingFriendRequestCount;
	}
	
	public int getConfirmedFriendRequestCount()
	{
		return this.confirmedFriendRequestCount;
	}
	
	public void setConfirmedFriendRequestCount(final int confirmedFriendRequestCount)
	{
		this.confirmedFriendRequestCount = confirmedFriendRequestCount;
	}
	
	public List<NotificationDTO> getNotifList()
	{
		return this.notifList;
	}
	
	public void setNotifList(final List<NotificationDTO> notifList)
	{
		this.notifList = notifList;
	}
	
	public int getNotifListCount()
	{
		return this.notifListCount;
	}
	
	public void setNotifListCount(final int notifListCount)
	{
		this.notifListCount = notifListCount;
	}
	
	public int getNotifListcount()
	{
		return this.notifListCount;
	}
	
	public void setNotifListcount(final int notifListcount)
	{
		this.notifListCount = notifListcount;
	}
	
}
