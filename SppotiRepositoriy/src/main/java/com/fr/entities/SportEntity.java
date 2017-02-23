package com.fr.entities;

import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by: Wail DJENANE on May 22, 2016
 */
@Entity
@Table(name = "SPORT")
@JsonInclude(Include.NON_EMPTY)
public class SportEntity
        extends AbstractCommonEntity {
    @Column(nullable = false)
    private String name;
    private String description;
    private String icon;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "relatedSports")
    @JsonIgnore
    private Set<UserEntity> subscribedUsers;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sport")
    @JsonIgnore
    private Set<PostEntity> post;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sport")
    @JsonIgnore
    private Set<SppotiEntity> game;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sport")
    @JsonIgnore
    private Set<EditHistoryEntity> editList;

    public SportEntity() {
    }

    public SportEntity(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public Set<PostEntity> getPost() {
        return post;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setPost(Set<PostEntity> post) {
        this.post = post;
    }

    public Set<UserEntity> getSubscribedUsers() {
        return subscribedUsers;
    }

    public void setSubscribedUsers(Set<UserEntity> subscribedUsers) {
        this.subscribedUsers = subscribedUsers;
    }

    public Set<SppotiEntity> getGame() {
        return game;
    }

    public void setGame(Set<SppotiEntity> game) {
        this.game = game;
    }

    public Set<EditHistoryEntity> getEditList() {
        return editList;
    }

    public void setEditList(Set<EditHistoryEntity> editList) {
        this.editList = editList;
    }

}