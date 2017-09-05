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
	
	@Column(nullable = false, name = "last_name")
	private String lastName;
	
	@Column(nullable = false, name = "first_name")
	private String firstName;
	
	@Temporal(TemporalType.DATE)
	@Past
	@Column(nullable = false, name = "date_born")
	private Date dateBorn;
	
	@Column(name = "gender")
	@Enumerated(EnumType.STRING)
	private GenderEnum gender;
	
	@Column(unique = true)
	private String telephone;
	
	@Column(nullable = false, unique = true, name = "email")
	private String email;
	
	@Column(nullable = false, unique = true, name = "username")
	private String username;
	
	@Column(name = "facebook_id")
	private String facebookId;
	
	@Column(name = "google_id")
	private String googleId;
	
	@Column(name = "twitter_id")
	private String twitterId;
	
	@Column(nullable = false, unique = true, name = "confirmation_code")
	private String confirmationCode;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, name = "account_creation_date")
	private Date accountCreationDate = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, name = "account_max_activation_date")
	private Date accountMaxActivationDate;
	
	@Column(nullable = false, name = "password")
	private String password;
	
	@Column(name = "confirmed")
	private boolean confirmed = false;
	
	@Column(name = "deleted")
	private boolean deleted;
	
	@Column(name = "first_connexion")
	private Boolean firstConnexion = Boolean.FALSE;
	
	@Column(name = "profile_complete")
	private Boolean isProfileComplete = Boolean.FALSE;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deactivate_date")
	private Date deactivationDate;
	
	private String job;
	private String description;
	
	@Column(name = "recover_code")
	private String recoverCode;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "recover_code_creation_date")
	private Date recoverCodeCreationDate;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "language")
	private LanguageEnum languageEnum = LanguageEnum.fr;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
	private List<ConnexionHistoryEntity> connexionHistory = new ArrayList<>();
	
	@Column(name = "time_zone")
	private String timeZone = "02";
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	@OrderBy("datetimeCreated DESC")
	private List<PasswordHistory> passwordHistories = new ArrayList<>();
	
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
	private Set<RoleEntity> roles = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "sport_id")
	@JsonIgnore
	private Set<SportEntity> relatedSports;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "users")
	@OrderBy("dateTime DESC")
	private SortedSet<AddressEntity> addresses;
	
	private transient Long connectedUserId;
	
	public Boolean getFirstConnexion() {
		return this.firstConnexion;
	}
	
	public void setFirstConnexion(final Boolean firstConnexion) {
		this.firstConnexion = firstConnexion;
	}
	
	public Boolean getProfileComplete() {
		return this.isProfileComplete;
	}
	
	public void setProfileComplete(final Boolean profileComplete) {
		this.isProfileComplete = profileComplete;
	}
	
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
	
	public List<PasswordHistory> getPasswordHistories() {
		return this.passwordHistories;
	}
	
	public void setPasswordHistories(final List<PasswordHistory> passwordHistories) {
		this.passwordHistories = passwordHistories;
	}
	
	public Long getConnectedUserId() {
		return this.connectedUserId;
	}
	
	public void setConnectedUserId(final Long connectedUserId) {
		this.connectedUserId = connectedUserId;
	}
	
	public String getFacebookId() {
		return this.facebookId;
	}
	
	public void setFacebookId(final String facebookId) {
		this.facebookId = facebookId;
	}
	
	public String getGoogleId() {
		return this.googleId;
	}
	
	public void setGoogleId(final String googleId) {
		this.googleId = googleId;
	}
	
	public String getTwitterId() {
		return this.twitterId;
	}
	
	public void setTwitterId(final String twitterId) {
		this.twitterId = twitterId;
	}
}
