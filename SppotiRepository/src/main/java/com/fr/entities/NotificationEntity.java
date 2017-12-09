package com.fr.entities;

import com.fr.commons.enumeration.notification.NotificationStatus;
import com.fr.commons.enumeration.notification.NotificationTypeEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: Wail DJENANE on Nov 10, 2016
 */
@Entity
@Table(name = "NOTIFICATION")
public class NotificationEntity extends AbstractCommonEntity
{
	@ManyToOne
	@JoinColumn(name = "FROM_USER_ID", nullable = false, referencedColumnName = "ID")
	private UserEntity from;
	
	@ManyToOne
	@JoinColumn(name = "TO_USER_ID", nullable = false, referencedColumnName = "ID")
	private UserEntity to;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, columnDefinition = "DATETIME(6)", updatable = false)
	private Date creationDate = new Date();
	
	@Enumerated(EnumType.STRING)
	@Column(name = "NOTIFICATION_TYPE", nullable = false)
	private NotificationTypeEnum notificationType;
	
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificationStatus status = NotificationStatus.UNREAD;
	
	@ManyToOne
	@JoinColumn(name = "TEAM_ID")
	private TeamEntity team;
	
	@ManyToOne
	@JoinColumn(name = "SPPOTI_ID")
	private SppotiEntity sppoti;
	
	@ManyToOne
	@JoinColumn(name = "POST_ID")
	private PostEntity post;
	
	@ManyToOne
	@JoinColumn(name = "COMMENT_ID")
	private CommentEntity comment;
	
	@ManyToOne
	@JoinColumn(name = "SCORE_ID")
	private ScoreEntity score;
	
	@ManyToOne
	@JoinColumn(name = "RATING_ID")
	private RatingEntity rating;
	
	/**
	 * to get trace of the connected user when using transformers.
	 */
	private transient Long connectedUserId;
	
	public UserEntity getFrom()
	{
		return this.from;
	}
	
	public void setFrom(final UserEntity from)
	{
		this.from = from;
	}
	
	public UserEntity getTo()
	{
		return this.to;
	}
	
	public void setTo(final UserEntity to)
	{
		this.to = to;
	}
	
	public Date getCreationDate()
	{
		return this.creationDate;
	}
	
	public void setCreationDate(final Date creationDate)
	{
		this.creationDate = creationDate;
	}
	
	public NotificationTypeEnum getNotificationType()
	{
		return this.notificationType;
	}
	
	public void setNotificationType(final NotificationTypeEnum notificationType)
	{
		this.notificationType = notificationType;
	}
	
	public TeamEntity getTeam()
	{
		return this.team;
	}
	
	public void setTeam(final TeamEntity team)
	{
		this.team = team;
	}
	
	public SppotiEntity getSppoti()
	{
		return this.sppoti;
	}
	
	public void setSppoti(final SppotiEntity sppoti)
	{
		this.sppoti = sppoti;
	}
	
	public NotificationStatus getStatus()
	{
		return this.status;
	}
	
	public void setStatus(final NotificationStatus status)
	{
		this.status = status;
	}
	
	public PostEntity getPost() {
		return this.post;
	}
	
	public void setPost(final PostEntity post) {
		this.post = post;
	}
	
	public CommentEntity getComment() {
		return this.comment;
	}
	
	public void setComment(final CommentEntity comment) {
		this.comment = comment;
	}
	
	public ScoreEntity getScore() {
		return this.score;
	}
	
	public void setScore(final ScoreEntity score) {
		this.score = score;
	}
	
	public RatingEntity getRating() {
		return this.rating;
	}
	
	public void setRating(final RatingEntity rating) {
		this.rating = rating;
	}
	
	public Long getConnectedUserId() {
		return this.connectedUserId;
	}
	
	public void setConnectedUserId(final Long connectedUserId) {
		this.connectedUserId = connectedUserId;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof NotificationEntity))
			return false;
		if (!super.equals(o))
			return false;
		
		final NotificationEntity that = (NotificationEntity) o;
		
		if (this.status != that.status)
			return false;
		if (this.creationDate != null ? !this.creationDate.equals(that.creationDate) : that.creationDate != null)
			return false;
		return this.notificationType == that.notificationType;
		
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + (this.creationDate != null ? this.creationDate.hashCode() : 0);
		result = 31 * result + (this.notificationType != null ? this.notificationType.hashCode() : 0);
		result = 31 * result + this.status.hashCode();
		return result;
	}
}
