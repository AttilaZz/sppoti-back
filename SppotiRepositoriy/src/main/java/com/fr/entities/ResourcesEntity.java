package com.fr.entities;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */

/*
 * this table is for user resources, pictures, videos, documents, etc ...
 */
@Entity @Table(name = "RESSOURCE") @JsonInclude(Include.NON_ABSENT) public class ResourcesEntity
        extends AbstractCommonEntity
{

    @Column(nullable = false) private String url;
    @Column private String description;
    @Column private Integer type; // 1: avatar, 2: cover, 3: document
    @Column private Integer typeExtension; // 1: image, 2: vidéo, 3: pdf, word ...
    @Column private String dateTime = new Date().toString();
    @Column private boolean isSelected = false;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) @JoinColumn(name = "user_id")
    private UserEntity user;

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(String dateTime)
    {
        this.dateTime = dateTime;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
    }

    public UserEntity getUserRessources()
    {
        return user;
    }

    public void setUserRessources(UserEntity userRessources)
    {
        this.user = userRessources;
    }

    public Integer getTypeExtension()
    {
        return typeExtension;
    }

    public void setTypeExtension(Integer typeExtension)
    {
        this.typeExtension = typeExtension;
    }

    public UserEntity getUser()
    {
        return user;
    }

    public void setUser(UserEntity user)
    {
        this.user = user;
    }

}