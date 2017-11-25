package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by djenanewail on 12/18/16.
 */

@Entity
@Table(name = "ADDRESS")
public class AddressEntity extends AbstractCommonEntity implements Comparable<AddressEntity>
{
	
	private String address;
	private String dateTime = new DateTime().toString();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition = "post_id")
	@JsonIgnore
	private PostEntity post;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(columnDefinition = "userid")
	@JsonIgnore
	private UserEntity user;
	
	public AddressEntity() {
	}
	
	public AddressEntity(final String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public void setAddress(final String address) {
		this.address = address;
	}
	
	public String getDateTime() {
		return this.dateTime;
	}
	
	public void setDateTime(final String dateTime) {
		this.dateTime = dateTime;
	}
	
	public PostEntity getPost() {
		return this.post;
	}
	
	public void setPost(final PostEntity post) {
		this.post = post;
	}
	
	public UserEntity getUser() {
		return this.user;
	}
	
	public void setUser(final UserEntity user) {
		this.user = user;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int compareTo(final AddressEntity o) {
		if (this != null) {
			if (o != null) {
				return this.dateTime.compareTo(o.dateTime);
			} else {
				return 1;
			}
		}
		
		if (o != null)
			return -1;
		
		return 0;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AddressEntity))
			return false;
		if (!super.equals(o))
			return false;
		
		final AddressEntity that = (AddressEntity) o;
		
		if (this.address != null ? !this.address.equals(that.address) : that.address != null)
			return false;
		return this.dateTime != null ? this.dateTime.equals(that.dateTime) : that.dateTime == null;
		
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (this.address != null ? this.address.hashCode() : 0);
		result = 31 * result + (this.dateTime != null ? this.dateTime.hashCode() : 0);
		return result;
	}
}
