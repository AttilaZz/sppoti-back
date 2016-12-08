package com.fr.pojos;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Entity
@JsonInclude(Include.NON_EMPTY)
public class Sppoti {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String titre;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private String datetime = new DateTime().toString();

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "addressId", nullable = false)
	private Address gameAddress;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "sport_id", nullable = false)
	@JsonIgnore
	private Sport relatedSport;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private Users userGame;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "game")
	@JsonIgnore
	private Set<Post> post;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "gameTeam")
	private Set<Users> teamMemnbers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public Address getGameAddress() {
		return gameAddress;
	}

	public void setGameAddress(Address gameAddress) {
		this.gameAddress = gameAddress;
	}

	public Sport getRelatedSport() {
		return relatedSport;
	}

	public void setRelatedSport(Sport relatedSport) {
		this.relatedSport = relatedSport;
	}

	public Users getUserGame() {
		return userGame;
	}

	public void setUserGame(Users userGame) {
		this.userGame = userGame;
	}

	public Set<Post> getPost() {
		return post;
	}

	public void setPost(Set<Post> post) {
		this.post = post;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Users> getTeamMemnbers() {
		return teamMemnbers;
	}

	public void setTeamMemnbers(Set<Users> teamMemnbers) {
		this.teamMemnbers = teamMemnbers;
	}

}
