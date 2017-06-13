package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.commons.enumeration.GenderEnum;
import com.fr.commons.enumeration.LanguageEnum;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.util.*;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */

@Entity
@Table(name = "USER")
@JsonInclude(Include.NON_EMPTY)
public class UserEntity extends AbstractCommonEntity
{
	
	@Column(nullable = false)
	private String lastName;
	
	@Column(nullable = false)
	private String firstName;
	
	@Temporal(TemporalType.DATE)
	@Past
	@Column(nullable = false)
	private Date dateBorn;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private GenderEnum gender;
	
	@Column(unique = true)
	private String telephone;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	@Column(nullable = false, unique = true)
	private String confirmationCode;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date accountCreationDate = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date accountMaxActivationDate;
	
	@Column(nullable = false)
	private String password;
	
	private boolean deleted;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deactivate_date")
	private Date deactivationDate;
	
	private boolean confirmed = false;
	
	private String job;
	private String description;
	
	private String recoverCode;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date recoverCodeCreationDate;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	@OrderBy("datetimeCreated DESC")
	private SortedSet<PostEntity> userPosts = new TreeSet<PostEntity>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userSppoti")
	private Set<SppotiEntity> userSppoties;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userMessage")
	private Set<MessageEntity> userMessages;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	private Set<CommentEntity> commentEntities;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	private Set<LikeContentEntity> likes;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
	@Where(clause = "selected='1'")
	private Set<ResourcesEntity> resources;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "role_id", nullable = false)
	@JsonIgnore
	private Set<RoleEntity> roles;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "sport_id")
	@JsonIgnore
	private Set<SportEntity> relatedSports;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "users")
	@OrderBy("dateTime DESC")
	private SortedSet<AddressEntity> addresses;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "language")
	private LanguageEnum languageEnum = LanguageEnum.fr;
	
	//	@ElementCollection(fetch = FetchType.LAZY)
	//	@MapKeyColumn(name = "date_connexion")
	//	@MapKeyTemporal(TemporalType.TIMESTAMP)
	//	@Column(name = "ip_address", nullable = false)
	//	@JoinTable(name = "connexion_history")
	//	@JoinColumn(name = "user_id")
	//	private Map<Date, String> ipHistory = new LinkedHashMap<>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
	private List<ConnexionHistoryEntity> connexionHistory;
	
	@Column(name = "time_zone")
	private String timeZone = "02";
	
	public boolean isDeleted()
	{
		return this.deleted;
	}
	
	public void setDeleted(final boolean deleted)
	{
		this.deleted = deleted;
	}
	
	public boolean isConfirmed()
	{
		return this.confirmed;
	}
	
	public void setConfirmed(final boolean confirmed)
	{
		this.confirmed = confirmed;
	}
	
	public Set<PostEntity> getUserPosts()
	{
		return this.userPosts;
	}
	
	public void setUserPosts(final SortedSet<PostEntity> userPosts)
	{
		this.userPosts = userPosts;
	}
	
	public Set<SppotiEntity> getUserSppoties()
	{
		return this.userSppoties;
	}
	
	public void setUserSppoties(final Set<SppotiEntity> userSppoties)
	{
		this.userSppoties = userSppoties;
	}
	
	public Set<MessageEntity> getUserMessages()
	{
		return this.userMessages;
	}
	
	public void setUserMessages(final Set<MessageEntity> userMessages)
	{
		this.userMessages = userMessages;
	}
	
	public Set<CommentEntity> getCommentEntities()
	{
		return this.commentEntities;
	}
	
	public void setCommentEntities(final Set<CommentEntity> commentEntities)
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
	
	public Set<ResourcesEntity> getResources()
	{
		return this.resources;
	}
	
	public void setResources(final Set<ResourcesEntity> resources)
	{
		this.resources = resources;
	}
	
	public String getConfirmationCode()
	{
		return this.confirmationCode;
	}
	
	public void setConfirmationCode(final String confirmationCode)
	{
		this.confirmationCode = confirmationCode;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public void setPassword(final String password)
	{
		this.password = password;
	}
	
	public String getJob()
	{
		return this.job;
	}
	
	public void setJob(final String job)
	{
		this.job = job;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public void setDescription(final String description)
	{
		this.description = description;
	}
	
	public Set<RoleEntity> getRoles()
	{
		return this.roles;
	}
	
	public void setRoles(final Set<RoleEntity> roles)
	{
		this.roles = roles;
	}
	
	public Set<SportEntity> getRelatedSports()
	{
		return this.relatedSports;
	}
	
	public void setRelatedSports(final Set<SportEntity> relatedSports)
	{
		this.relatedSports = relatedSports;
	}
	
	public SortedSet<AddressEntity> getAddresses()
	{
		return this.addresses;
	}
	
	public void setAddresses(final SortedSet<AddressEntity> addresses)
	{
		this.addresses = addresses;
	}
	
	public String getLastName()
	{
		return this.lastName;
	}
	
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}
	
	public String getFirstName()
	{
		return this.firstName;
	}
	
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}
	
	public Date getDateBorn()
	{
		return this.dateBorn;
	}
	
	public void setDateBorn(final Date dateBorn)
	{
		this.dateBorn = dateBorn;
	}
	
	public String getTelephone()
	{
		return this.telephone;
	}
	
	public void setTelephone(final String telephone)
	{
		this.telephone = telephone;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public void setEmail(final String email)
	{
		this.email = email;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public void setUsername(final String username)
	{
		this.username = username;
	}
	
	public String getRecoverCode()
	{
		return this.recoverCode;
	}
	
	public void setRecoverCode(final String recoverCode)
	{
		this.recoverCode = recoverCode;
	}
	
	public Date getRecoverCodeCreationDate()
	{
		return this.recoverCodeCreationDate;
	}
	
	public void setRecoverCodeCreationDate(final Date recoverCodeCreationDate)
	{
		this.recoverCodeCreationDate = recoverCodeCreationDate;
	}
	
	public Date getAccountCreationDate()
	{
		return this.accountCreationDate;
	}
	
	public void setAccountCreationDate(final Date accountCreationDate)
	{
		this.accountCreationDate = accountCreationDate;
	}
	
	public GenderEnum getGender()
	{
		return this.gender;
	}
	
	public void setGender(final GenderEnum gender)
	{
		this.gender = gender;
	}
	
	public Date getAccountMaxActivationDate()
	{
		return this.accountMaxActivationDate;
	}
	
	public void setAccountMaxActivationDate(final Date accountMaxActivationDate)
	{
		this.accountMaxActivationDate = accountMaxActivationDate;
	}
	
	public LanguageEnum getLanguageEnum()
	{
		return this.languageEnum;
	}
	
	public void setLanguageEnum(final LanguageEnum languageEnum)
	{
		this.languageEnum = languageEnum;
	}
	
	public String getTimeZone()
	{
		return this.timeZone;
	}
	
	public void setTimeZone(final String timeZone)
	{
		this.timeZone = timeZone;
	}
	
	public Date getDeactivationDate() {
		return this.deactivationDate;
	}
	
	public void setDeactivationDate(final Date deactivationDate) {
		this.deactivationDate = deactivationDate;
	}
	
	public List<ConnexionHistoryEntity> getConnexionHistory() {
		return this.connexionHistory;
	}
	
	public void setConnexionHistory(final List<ConnexionHistoryEntity> connexionHistory) {
		this.connexionHistory = connexionHistory;
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
		
		final UserEntity entity = (UserEntity) o;
		
		if (this.deleted != entity.deleted)
			return false;
		if (this.confirmed != entity.confirmed)
			return false;
		if (this.lastName != null ? !this.lastName.equals(entity.lastName) : entity.lastName != null)
			return false;
		if (this.firstName != null ? !this.firstName.equals(entity.firstName) : entity.firstName != null)
			return false;
		if (this.dateBorn != null ? !this.dateBorn.equals(entity.dateBorn) : entity.dateBorn != null)
			return false;
		if (this.gender != entity.gender)
			return false;
		if (this.telephone != null ? !this.telephone.equals(entity.telephone) : entity.telephone != null)
			return false;
		if (this.email != null ? !this.email.equals(entity.email) : entity.email != null)
			return false;
		if (this.username != null ? !this.username.equals(entity.username) : entity.username != null)
			return false;
		if (this.confirmationCode != null ? !this.confirmationCode.equals(entity.confirmationCode) :
				entity.confirmationCode != null)
			return false;
		if (this.accountCreationDate != null ? !this.accountCreationDate.equals(entity.accountCreationDate) :
				entity.accountCreationDate != null)
			if (this.deactivationDate != null ? !this.deactivationDate.equals(entity.deactivationDate) :
					entity.deactivationDate != null)
				return false;
		if (this.accountMaxActivationDate != null ?
				!this.accountMaxActivationDate.equals(entity.accountMaxActivationDate) :
				entity.accountMaxActivationDate != null)
			return false;
		if (this.password != null ? !this.password.equals(entity.password) : entity.password != null)
			return false;
		if (this.job != null ? !this.job.equals(entity.job) : entity.job != null)
			return false;
		if (this.description != null ? !this.description.equals(entity.description) : entity.description != null)
			return false;
		if (this.recoverCode != null ? !this.recoverCode.equals(entity.recoverCode) : entity.recoverCode != null)
			return false;
		if (this.recoverCodeCreationDate != null ?
				!this.recoverCodeCreationDate.equals(entity.recoverCodeCreationDate) :
				entity.recoverCodeCreationDate != null)
			return false;
		if (this.languageEnum != entity.languageEnum)
			return false;
		return this.timeZone != null ? this.timeZone.equals(entity.timeZone) : entity.timeZone == null;
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + (this.lastName != null ? this.lastName.hashCode() : 0);
		result = 31 * result + (this.firstName != null ? this.firstName.hashCode() : 0);
		result = 31 * result + (this.dateBorn != null ? this.dateBorn.hashCode() : 0);
		result = 31 * result + (this.gender != null ? this.gender.hashCode() : 0);
		result = 31 * result + (this.telephone != null ? this.telephone.hashCode() : 0);
		result = 31 * result + (this.email != null ? this.email.hashCode() : 0);
		result = 31 * result + (this.username != null ? this.username.hashCode() : 0);
		result = 31 * result + (this.confirmationCode != null ? this.confirmationCode.hashCode() : 0);
		result = 31 * result + (this.password != null ? this.password.hashCode() : 0);
		result = 31 * result + (this.deleted ? 1 : 0);
		result = 31 * result + (this.confirmed ? 1 : 0);
		result = 31 * result + (this.job != null ? this.job.hashCode() : 0);
		result = 31 * result + (this.description != null ? this.description.hashCode() : 0);
		result = 31 * result + (this.recoverCode != null ? this.recoverCode.hashCode() : 0);
		result = 31 * result + (this.recoverCodeCreationDate != null ? this.recoverCodeCreationDate.hashCode() : 0);
		result = 31 * result + (this.accountCreationDate != null ? this.accountCreationDate.hashCode() : 0);
		result = 31 * result + (this.accountMaxActivationDate != null ? this.accountMaxActivationDate.hashCode() : 0);
		result = 31 * result + (this.languageEnum != null ? this.languageEnum.hashCode() : 0);
		result = 31 * result + (this.timeZone != null ? this.timeZone.hashCode() : 0);
		result = 31 * result + (this.deactivationDate != null ? this.deactivationDate.hashCode() : 0);
		return result;
	}
}
