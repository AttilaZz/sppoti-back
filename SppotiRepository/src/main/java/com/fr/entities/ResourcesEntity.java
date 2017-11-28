package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */

/*
 * this table is for user resources, pictures, videos, documents, etc ...
 */
@Entity
@Table(name = "RESSOURCE")
@JsonInclude(Include.NON_ABSENT)
public class ResourcesEntity extends AbstractCommonEntity
{
	
	@Column(nullable = false)
	private String url;
	@Column
	private String description;
	@Column
	private Integer type; // 1: avatar, 2: cover, 3: document
	@Column
	private Integer typeExtension; // 1: image, 2: vidéo, 3: pdf, word ...
	@Column
	private String dateTime = new Date().toString();
	@Column
	private boolean selected = false;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;
	
	public String getUrl() {
		return this.url;
	}
	
	public void setUrl(final String url) {
		this.url = url;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(final String description) {
		this.description = description;
	}
	
	public Integer getType() {
		return this.type;
	}
	
	public void setType(final Integer type) {
		this.type = type;
	}
	
	public String getDateTime() {
		return this.dateTime;
	}
	
	public void setDateTime(final String dateTime) {
		this.dateTime = dateTime;
	}
	
	public boolean isSelected() {
		return this.selected;
	}
	
	public void setSelected(final boolean isSelected) {
		this.selected = isSelected;
	}
	
	public UserEntity getUserRessources() {
		return this.user;
	}
	
	public void setUserRessources(final UserEntity userRessources) {
		this.user = userRessources;
	}
	
	public Integer getTypeExtension() {
		return this.typeExtension;
	}
	
	public void setTypeExtension(final Integer typeExtension) {
		this.typeExtension = typeExtension;
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
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ResourcesEntity))
			return false;
		if (!super.equals(o))
			return false;
		
		final ResourcesEntity that = (ResourcesEntity) o;
		
		if (this.selected != that.selected)
			return false;
		if (this.url != null ? !this.url.equals(that.url) : that.url != null)
			return false;
		if (this.description != null ? !this.description.equals(that.description) : that.description != null)
			return false;
		if (this.type != null ? !this.type.equals(that.type) : that.type != null)
			return false;
		if (this.typeExtension != null ? !this.typeExtension.equals(that.typeExtension) : that.typeExtension != null)
			return false;
		return this.dateTime != null ? this.dateTime.equals(that.dateTime) : that.dateTime == null;
		
	}
	
	/**
	 * {@inheritDoc}.
	 */
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (this.url != null ? this.url.hashCode() : 0);
		result = 31 * result + (this.description != null ? this.description.hashCode() : 0);
		result = 31 * result + (this.type != null ? this.type.hashCode() : 0);
		result = 31 * result + (this.typeExtension != null ? this.typeExtension.hashCode() : 0);
		result = 31 * result + (this.dateTime != null ? this.dateTime.hashCode() : 0);
		result = 31 * result + (this.selected ? 1 : 0);
		return result;
	}
}
