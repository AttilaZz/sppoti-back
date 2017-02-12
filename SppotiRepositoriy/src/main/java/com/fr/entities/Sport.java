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
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by: Wail DJENANE on May 22, 2016
 */
@Entity
@JsonInclude(Include.NON_EMPTY)
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;
    private String icon;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "relatedSports")
    @JsonIgnore
    private Set<UserEntity> subscribedUsers;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sport")
    @JsonIgnore
    private Set<Post> post;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sport")
    @JsonIgnore
    private Set<Sppoti> game;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sport")
    @JsonIgnore
    private Set<EditHistory> editList;

    public Sport() {
    }

    public Sport(String name) {
        super();
        this.name = name;
    }

    public Long getId() {
        return id;
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

    public Set<Post> getPost() {
        return post;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setPost(Set<Post> post) {
        this.post = post;
    }

    public Set<UserEntity> getSubscribedUsers() {
        return subscribedUsers;
    }

    public void setSubscribedUsers(Set<UserEntity> subscribedUsers) {
        this.subscribedUsers = subscribedUsers;
    }

    public Set<Sppoti> getGame() {
        return game;
    }

    public void setGame(Set<Sppoti> game) {
        this.game = game;
    }

    public Set<EditHistory> getEditList() {
        return editList;
    }

    public void setEditList(Set<EditHistory> editList) {
        this.editList = editList;
    }

}