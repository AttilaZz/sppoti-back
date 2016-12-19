package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */

@Entity
@JsonInclude(Include.NON_EMPTY)
public class Users extends Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;

    @Column(nullable = false, unique = true)
    private String confirmationCode;

    @Column(nullable = false)
    private String password;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @OrderBy("datetimeCreated DESC")
    private SortedSet<Post> userPosts;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userGame")
    private Set<Sppoti> userGames;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userMessage")
    private Set<Messages> userMessages;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "friend_id")
    private Set<Friend> friends;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<LikeContent> likes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "notifSender")
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

    public Set<Resources> getRessources() {
        return ressources;
    }

    public void setRessources(Set<Resources> ressources) {
        this.ressources = ressources;
    }

    public Set<Notifications> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notifications> notifications) {
        this.notifications = notifications;
    }

    public Set<Friend> getFriends() {
        return friends;
    }

    public void setFriends(Set<Friend> friends) {
        this.friends = friends;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
