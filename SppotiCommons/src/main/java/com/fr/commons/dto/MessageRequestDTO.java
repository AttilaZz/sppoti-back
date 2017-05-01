package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by: Wail DJENANE on Jun 25, 2016
 */

@JsonInclude(Include.NON_EMPTY)
public class MessageRequestDTO
{
	
	private Long userId;
	private Long receivedId;
	
	//    private MessageEntity msg;
	//
	//    private UserEntity sender;
	//    private UserEntity receiver;
	//
	//    private List<MessageEntity> sentMessages;
	//    private List<MessageEntity> receivedMessages;
	
	public Long getUserId()
	{
		return this.userId;
	}
	
	public void setUserId(final Long userId)
	{
		this.userId = userId;
	}
	
	public Long getReceivedId()
	{
		return this.receivedId;
	}
	
	public void setReceivedId(final Long receivedId)
	{
		this.receivedId = receivedId;
	}
	
	
}
