package com.fr.commons.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fr.commons.dto.AbstractCommonDTO;

import java.util.List;

/**
 * Created by wdjenane on 14/02/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationListDTO extends AbstractCommonDTO
{
	
	private List<NotificationDTO> notifications;
	private Integer notifCounter;
	private Integer UnreadCounter;
	
	public List<NotificationDTO> getNotifications()
	{
		return this.notifications;
	}
	
	public void setNotifications(final List<NotificationDTO> notifications)
	{
		this.notifications = notifications;
	}
	
	public Integer getNotifCounter()
	{
		return this.notifCounter;
	}
	
	public void setNotifCounter(final Integer notifCounter)
	{
		this.notifCounter = notifCounter;
	}
	
	public Integer getUnreadCounter() {
		return this.UnreadCounter;
	}
	
	public void setUnreadCounter(final Integer unreadCounter) {
		this.UnreadCounter = unreadCounter;
	}
}
