package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by: Wail DJENANE on May 22, 2016
 */
@Entity
@Table(name = "MESSAGE")
@JsonInclude(Include.NON_EMPTY)
public class MessageEntity extends AbstractCommonEntity
{
	@Column(nullable = false)
	private String datetime = new DateTime().toString();
	@Column(nullable = false)
	private String object;
	@Column(nullable = false)
	private String content;
	@Column(nullable = false)
	@JsonIgnore
	private Long receiverId;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	// @JsonBackReference(value = "user_messages")
	@JsonIgnore
	private UserEntity userMessage;
	
	public MessageEntity()
	{
		super();
	}
	
	public MessageEntity(final MessageEntity msg)
	{
		this.content = msg.getContent();
		this.datetime = msg.getDatetime();
		this.receiverId = msg.getReceiverId();
		this.object = msg.getObject();
	}
	
	public String getDatetime()
	{
		return this.datetime;
	}
	
	public void setDatetime(final String datetime)
	{
		this.datetime = datetime;
	}
	
	public String getObject()
	{
		return this.object;
	}
	
	public void setObject(final String object)
	{
		this.object = object;
	}
	
	public String getContent()
	{
		return this.content;
	}
	
	public void setContent(final String content)
	{
		this.content = content;
	}
	
	public Long getReceiverId()
	{
		return this.receiverId;
	}
	
	public void setReceiverId(final Long receiverId)
	{
		this.receiverId = receiverId;
	}
	
	public UserEntity getUserMessage()
	{
		return this.userMessage;
	}
	
	public void setUserMessage(final UserEntity userMessage)
	{
		this.userMessage = userMessage;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;
		
		final MessageEntity that = (MessageEntity) o;
		
		if (this.datetime != null ? !this.datetime.equals(that.datetime) : that.datetime != null)
			return false;
		if (this.object != null ? !this.object.equals(that.object) : that.object != null)
			return false;
		if (this.content != null ? !this.content.equals(that.content) : that.content != null)
			return false;
		return this.receiverId != null ? this.receiverId.equals(that.receiverId) : that.receiverId == null;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + (this.datetime != null ? this.datetime.hashCode() : 0);
		result = 31 * result + (this.object != null ? this.object.hashCode() : 0);
		result = 31 * result + (this.content != null ? this.content.hashCode() : 0);
		result = 31 * result + (this.receiverId != null ? this.receiverId.hashCode() : 0);
		return result;
	}
}
