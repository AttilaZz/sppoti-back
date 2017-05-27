package com.fr.commons.dto.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.commons.dto.UserDTO;

import java.util.List;

/**
 * Created by: Wail DJENANE on Jun 25, 2016
 */

@JsonInclude(Include.NON_EMPTY)
public class MessageRequestDTO extends AbstractCommonDTO
{
	
	private Long userId;
	private Long receivedId;
	
	private MessageDTO msg;
	
	private UserDTO sender;
	private UserDTO receiver;
	
	private List<MessageDTO> sentMessages;
	private List<MessageDTO> receivedMessages;
	
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
	
	public MessageDTO getMsg()
	{
		return this.msg;
	}
	
	public void setMsg(final MessageDTO msg)
	{
		this.msg = msg;
	}
	
	public UserDTO getSender()
	{
		return this.sender;
	}
	
	public void setSender(final UserDTO sender)
	{
		this.sender = sender;
	}
	
	public UserDTO getReceiver()
	{
		return this.receiver;
	}
	
	public void setReceiver(final UserDTO receiver)
	{
		this.receiver = receiver;
	}
	
	public List<MessageDTO> getSentMessages()
	{
		return this.sentMessages;
	}
	
	public void setSentMessages(final List<MessageDTO> sentMessages)
	{
		this.sentMessages = sentMessages;
	}
	
	public List<MessageDTO> getReceivedMessages()
	{
		return this.receivedMessages;
	}
	
	public void setReceivedMessages(final List<MessageDTO> receivedMessages)
	{
		this.receivedMessages = receivedMessages;
	}
}
