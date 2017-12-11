package com.fr.entities;

import com.fr.commons.enumeration.FriendShipStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: Wail DJENANE on Jul 3, 2016
 */

@Entity
@Table(name = "friend_ship")
public class FriendShipEntity extends AbstractCommonEntity
{
	@Column(nullable = false, name = "datetime", columnDefinition = "DATETIME(6)")
	private Date datetimeCreated = new Date();
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private FriendShipStatus status = FriendShipStatus.PENDING;
	
	@OneToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "friend_id")
	private UserEntity friend;
	
	@OneToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	public Date getDatetime()
	{
		return this.datetimeCreated;
	}
	
	public void setDatetime(final Date datetime)
	{
		this.datetimeCreated = datetime;
	}
	
	public FriendShipStatus getStatus()
	{
		return this.status;
	}
	
	public void setStatus(final FriendShipStatus status)
	{
		this.status = status;
	}
	
	public UserEntity getFriend()
	{
		return this.friend;
	}
	
	public void setFriend(final UserEntity friend)
	{
		this.friend = friend;
	}
	
	public UserEntity getUser()
	{
		return this.user;
	}
	
	public void setUser(final UserEntity user)
	{
		this.user = user;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof FriendShipEntity))
			return false;
		if (!super.equals(o))
			return false;
		
		final FriendShipEntity that = (FriendShipEntity) o;
		
		if (this.datetimeCreated != null ? !this.datetimeCreated.equals(that.datetimeCreated) :
				that.datetimeCreated != null)
			return false;
		return this.status != null ? this.status.equals(that.status) : that.status == null;
		
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + (this.datetimeCreated != null ? this.datetimeCreated.hashCode() : 0);
		result = 31 * result + (this.status != null ? this.status.hashCode() : 0);
		return result;
	}
}