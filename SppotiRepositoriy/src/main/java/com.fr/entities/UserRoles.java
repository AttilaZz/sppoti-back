package com.fr.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "ROLES")
@JsonInclude(Include.NON_EMPTY)
public class UserRoles {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "userRoles", cascade = CascadeType.ALL)
	private Set<Users> userProfile;

	public UserRoles() {
	}

	public UserRoles(String name) {
		super();
		this.name = name;
	}

	public UserRoles(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Users> getUser() {
		return userProfile;
	}

	public void setUser(Set<Users> user) {
		this.userProfile = user;
	}

}
