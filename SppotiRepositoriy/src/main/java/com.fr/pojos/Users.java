package com.fr.pojos;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */

@Entity
@JsonInclude(Include.NON_EMPTY)
public class Users implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id;

	@Column(nullable = false)
	private String lastName;
	@Column(nullable = false)
	private String firstName;
	@Column(nullable = false)
	private String dateBorn;
	@Column(nullable = false)
	private String sexe;
	@Column(nullable = true)
	private String telephone;
	@Column(nullable = false)
	private String email;
	@Column(nullable = true)
	private String confirmationCode;
	@Column(nullable = false)
	private boolean isConfirmed = false;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String username;
	@Column(nullable = true)
	private String job;
	@Column(nullable = true)
	private String description;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	@OrderBy("datetimeCreated DESC")
	private Set<Address> userAddress;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	@OrderBy("datetimeCreated DESC")
	private SortedSet<Post> userPosts;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userGame")
	private Set<Sppoti> userGames;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userMessage")
	private Set<Messages> userMessages;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "role_id", nullable = false)
	private Set<UserRoles> userRoles;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "sport_id", nullable = false)
	private Set<Sport> relatedSports;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	private Set<FriendShip> myFriends;

	@OneToOne(mappedBy = "friend", cascade = CascadeType.ALL)
	private FriendShip friend;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Comment> comments;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	private Set<LikeContent> likes;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "whoSentNotification")
	private Set<Notifications> notifications;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "team_id")
	@JsonIgnore
	private Sppoti gameTeam;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Resources> ressources;

	public Users() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getDateBorn() {
		return dateBorn;
	}

	public void setDateBorn(String dateBorn) {
		this.dateBorn = dateBorn;
	}

	public String getSexe() {
		return sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Address> getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(Set<Address> userAddress) {
		this.userAddress = userAddress;
	}

	public Set<Post> getUserPosts() {
		return userPosts;
	}

	public void setUserPosts(SortedSet<Post> userPosts) {
		this.userPosts = userPosts;
	}

	public Set<Sppoti> getUserGames() {
		return userGames;
	}

	public void setUserGames(Set<Sppoti> userGames) {
		this.userGames = userGames;
	}

	public Set<Messages> getUserMessages() {
		return userMessages;
	}

	public void setUserMessages(Set<Messages> userMessages) {
		this.userMessages = userMessages;
	}

	public Set<UserRoles> getUserProfile() {
		return userRoles;
	}

	public void setUserProfile(Set<UserRoles> userProfile) {
		this.userRoles = userProfile;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Set<LikeContent> getLikes() {
		return likes;
	}

	public void setLikes(Set<LikeContent> likes) {
		this.likes = likes;
	}

	public Set<Sport> getRelatedSports() {
		return relatedSports;
	}

	public void setRelatedSports(Set<Sport> relatedSports) {
		this.relatedSports = relatedSports;
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

	public boolean isConfirmed() {
		return isConfirmed;
	}

	public void setConfirmed(boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public Set<FriendShip> getMyFriends() {
		return myFriends;
	}

	public void setMyFriends(Set<FriendShip> myFriends) {
		this.myFriends = myFriends;
	}

	public FriendShip getFriend() {
		return friend;
	}

	public void setFriend(FriendShip friend) {
		this.friend = friend;
	}

	public Sppoti getTeamMemnbers() {
		return gameTeam;
	}

	public void setTeamMemnbers(Sppoti gameTeam) {
		this.gameTeam = gameTeam;
	}

	public Sppoti getGameTeam() {
		return gameTeam;
	}

	public void setGameTeam(Sppoti gameTeam) {
		this.gameTeam = gameTeam;
	}

	public Set<UserRoles> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRoles> userRoles) {
		this.userRoles = userRoles;
	}

	public Set<Resources> getRessources() {
		return ressources;
	}

	public void setRessources(Set<Resources> ressources) {
		this.ressources = ressources;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Notifications> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<Notifications> notifications) {
		this.notifications = notifications;
	}

}
