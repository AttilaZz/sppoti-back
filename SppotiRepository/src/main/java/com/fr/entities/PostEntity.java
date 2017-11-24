package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

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
	@CollectionTable(name = "post_album", joinColumns = @JoinColumn(name = "post_id"))
	@Column(name = "post_album", nullable = false)
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
	@Where(clause = "deleted='0'")
	private SortedSet<CommentEntity> commentEntities = new TreeSet<>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
	private List<LikeContentEntity> likes = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
	private List<EditHistoryEntity> editList = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "target_user")
	private UserEntity targetUserProfile;
	
	@Column(name = "time_zone")
	private String timeZone;
	
	/**
	 * to get trace of the connected user when using transformers.
	 */
	private transient Long connectedUserId;
	
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
	
	public List<LikeContentEntity> getLikes() {
		return this.likes;
	}
	
	public void setLikes(final List<LikeContentEntity> likes) {
		this.likes = likes;
	}
	
	public List<EditHistoryEntity> getEditList() {
		return this.editList;
	}
	
	public void setEditList(final List<EditHistoryEntity> editList) {
		this.editList = editList;
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
	
	public UserEntity getTargetUserProfile() {
		return this.targetUserProfile;
	}
	
	public void setTargetUserProfile(final UserEntity targetUserProfile) {
		this.targetUserProfile = targetUserProfile;
	}
	
	public Long getConnectedUserId() {
		return this.connectedUserId;
	}
	
	public void setConnectedUserId(final Long connectedUserId) {
		this.connectedUserId = connectedUserId;
	}
	
	public Set<String> getAlbum() {
		return this.album;
	}
	
	public void setAlbum(final Set<String> album) {
		this.album = album;
	}
	
	public String getTimeZone() {
		return this.timeZone;
	}
	
	public void setTimeZone(final String timeZone) {
		this.timeZone = timeZone;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@SuppressWarnings("unused")
	@Override
	public int compareTo(final PostEntity o)
	{
		return this.getDatetimeCreated().compareTo(o.getDatetimeCreated());
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
		if (this.targetUserProfile != that.targetUserProfile)
			return false;
		if (this.content != null ? !this.content.equals(that.content) : that.content != null)
			return false;
		if (this.datetimeCreated != null ? !this.datetimeCreated.equals(that.datetimeCreated) :
				that.datetimeCreated != null)
			return false;
		if (this.video != null ? !this.video.equals(that.video) : that.video != null)
			return false;
		if (this.timeZone != null ? !this.timeZone.equals(that.timeZone) : that.timeZone != null)
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
		result = 31 * result + (this.timeZone != null ? this.timeZone.hashCode() : 0);
		result = 31 * result + this.visibility;
		result = 31 * result + (this.deleted ? 1 : 0);
		return result;
	}
}
