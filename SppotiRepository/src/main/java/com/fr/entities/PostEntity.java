package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by: Wail DJENANE on May 22, 2016
 */

/**
 * 1: A post can be a simple custom content shared by the user 2: score of a
 * sppoti 3: address of a sppoti 4: anything else related to a sppoti 5: related to
 * one sport
 */

@Entity
@Table(name = "POST")
@JsonInclude(Include.NON_EMPTY)
public class PostEntity extends AbstractCommonEntity implements Comparable<PostEntity>
{
	
	@Column(length = 500)
	@SerializedName("text")
	private String content;
	
	@Column(nullable = false, columnDefinition = "DATETIME(6)", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date datetimeCreated = new Date();
	
	private String video;
	
	@ElementCollection
	private Set<String> album;
	
	@Column(nullable = false)
	private int visibility = 0;
	
	@Column(nullable = false, name = "deleted")
	private boolean deleted = false;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
	@OrderBy("dateTime DESC")
	private SortedSet<AddressEntity> addresses = new TreeSet<AddressEntity>();
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private UserEntity user;
	@ManyToOne
	@JoinColumn(name = "sport_id")
	private SportEntity sport;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
	@OrderBy("datetimeCreated DESC")
	private SortedSet<CommentEntity> commentEntities = new TreeSet<CommentEntity>();
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
	private Set<LikeContentEntity> likes;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
	private Set<EditHistoryEntity> editList;
	@Column(name = "target_user")
	private int targetUserProfileUuid;
	
	public PostEntity()
	{
		super();
	}
	
	public PostEntity(final PostEntity post)
	{
		this.content = post.getContent();
		this.album = post.getAlbum();
		this.video = post.getVideo();
	}
	
	public String getContent()
	{
		return this.content;
	}
	
	public void setContent(final String content)
	{
		this.content = content;
	}
	
	public UserEntity getUser()
	{
		return this.user;
	}
	
	public void setUser(final UserEntity user)
	{
		this.user = user;
	}
	
	public String getVideo()
	{
		return this.video;
	}
	
	public void setVideo(final String video)
	{
		this.video = video;
	}
	
	public SportEntity getSport()
	{
		return this.sport;
	}
	
	public void setSport(final SportEntity sport)
	{
		this.sport = sport;
	}
	
	public Date getDatetimeCreated()
	{
		return this.datetimeCreated;
	}
	
	public void setDatetimeCreated(final Date datetimeCreated)
	{
		this.datetimeCreated = datetimeCreated;
	}
	
	public SortedSet<CommentEntity> getCommentEntities()
	{
		return this.commentEntities;
	}
	
	public void setCommentEntities(final SortedSet<CommentEntity> commentEntities)
	{
		this.commentEntities = commentEntities;
	}
	
	public Set<LikeContentEntity> getLikes()
	{
		return this.likes;
	}
	
	public void setLikes(final Set<LikeContentEntity> likes)
	{
		this.likes = likes;
	}
	
	public Set<EditHistoryEntity> getEditList()
	{
		return this.editList;
	}
	
	public void setEditList(final Set<EditHistoryEntity> editList)
	{
		this.editList = editList;
	}
	
	public Set<String> getAlbum()
	{
		return this.album;
	}
	
	public void setAlbum(final Set<String> album)
	{
		this.album = album;
	}
	
	public int getVisibility()
	{
		return this.visibility;
	}
	
	public void setVisibility(final int visibility)
	{
		this.visibility = visibility;
	}
	
	public boolean isDeleted()
	{
		return this.deleted;
	}
	
	public void setDeleted(final boolean isDeleted)
	{
		this.deleted = isDeleted;
	}
	
	public SortedSet<AddressEntity> getAddresses()
	{
		return this.addresses;
	}
	
	public void setAddresses(final SortedSet<AddressEntity> addresses)
	{
		this.addresses = addresses;
	}
	
	public int getTargetUserProfileUuid()
	{
		return this.targetUserProfileUuid;
	}
	
	public void setTargetUserProfileUuid(final int targetUserProfileUuid)
	{
		this.targetUserProfileUuid = targetUserProfileUuid;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@SuppressWarnings("unused")
	@Override
	public int compareTo(final PostEntity o)
	{
		
		if (this != null) {
			if (o != null) {
				return this.datetimeCreated.compareTo(o.datetimeCreated);
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
	public boolean equals(final Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof PostEntity))
			return false;
		if (!super.equals(o))
			return false;
		
		final PostEntity that = (PostEntity) o;
		
		if (this.visibility != that.visibility)
			return false;
		if (this.deleted != that.deleted)
			return false;
		if (this.targetUserProfileUuid != that.targetUserProfileUuid)
			return false;
		if (this.content != null ? !this.content.equals(that.content) : that.content != null)
			return false;
		if (this.datetimeCreated != null ? !this.datetimeCreated.equals(that.datetimeCreated) :
				that.datetimeCreated != null)
			return false;
		if (this.video != null ? !this.video.equals(that.video) : that.video != null)
			return false;
		return this.album != null ? this.album.equals(that.album) : that.album == null;
		
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + (this.content != null ? this.content.hashCode() : 0);
		result = 31 * result + (this.datetimeCreated != null ? this.datetimeCreated.hashCode() : 0);
		result = 31 * result + (this.video != null ? this.video.hashCode() : 0);
		result = 31 * result + (this.album != null ? this.album.hashCode() : 0);
		result = 31 * result + this.visibility;
		result = 31 * result + (this.deleted ? 1 : 0);
		result = 31 * result + this.targetUserProfileUuid;
		return result;
	}
}
