package com.fr.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */

/*
 * this table is for user resources, pictures, videos, documents, etc ...
 */
@Entity
@JsonInclude(Include.NON_ABSENT)
public class Resources {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(nullable = false)
	private String url;

	@Column(nullable = true)
	private String description;

	@Column(nullable = true)
	private Integer type; // 0: avatar, 2: cover, 3: document

	@Column(nullable = true)
	private Integer typeExtension; // 0: image, 2: vidéo, 3: pdf, word ...

	@Column(nullable = false)
	private String dateTime = new Date().toString();

	@Column(nullable = false)
	private boolean isSelected = false;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private Users user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Users getUserRessources() {
		return user;
	}

	public void setUserRessources(Users userRessources) {
		this.user = userRessources;
	}

	public Integer getTypeExtension() {
		return typeExtension;
	}

	public void setTypeExtension(Integer typeExtension) {
		this.typeExtension = typeExtension;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}
