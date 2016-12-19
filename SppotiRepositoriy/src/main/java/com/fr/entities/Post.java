package com.fr.entities;

import java.util.*;

import javax.persistence.*;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;

/**
 * Created by: Wail DJENANE on May 22, 2016
 */

/**
 * 1: A post can be a simple custom content shared by the user 2: score of a
 * game 3: address of a game 4: anything else related to a game 5: related to
 * one sport
 */

@Entity
@JsonInclude(Include.NON_EMPTY)
public class Post implements Comparable<Post> {

    @Id
    @GeneratedValue
    private Long id;

    private int uuid = UUID.randomUUID().hashCode();

    @Column(length = 500)
    @SerializedName("text")
    private String content;

    @Column(nullable = false)
    private String datetimeCreated = new DateTime().toString();

    private String videoLink;

    @ElementCollection
    private Set<String> album = new HashSet<String>();

    @Column(nullable = false)
    private int visibility = 0;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "postTag")
    private Set<Notifications> notifications;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
    @OrderBy("dateTime DESC")
    private SortedSet<Address> addresses;

    public Post() {
        super();
    }

    public Post(Post post) {
        this.content = post.getContent();
        this.album = post.getAlbum();
        this.videoLink = post.getVideoLink();
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;

    @ManyToOne
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Sppoti game;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "postComment")
    @OrderBy("datetimeCreated DESC")
    private SortedSet<Comment> Comments;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
    private Set<LikeContent> likes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
    private Set<EditHistory> editList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Sppoti getGame() {
        return game;
    }

    public void setGame(Sppoti game) {
        this.game = game;
    }

    public String getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(String datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public SortedSet<Comment> getComments() {
        return Comments;
    }

    public void setComments(SortedSet<Comment> comments) {
        Comments = comments;
    }

    public Set<LikeContent> getLikes() {
        return likes;
    }

    public void setLikes(Set<LikeContent> likes) {
        this.likes = likes;
    }

    public Set<EditHistory> getEditList() {
        return editList;
    }

    public void setEditList(Set<EditHistory> editList) {
        this.editList = editList;
    }


    public Set<String> getAlbum() {
        return album;
    }

    public void setAlbum(Set<String> album) {
        this.album = album;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Set<Notifications> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notifications> notifications) {
        this.notifications = notifications;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public SortedSet<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(SortedSet<Address> addresses) {
        this.addresses = addresses;
    }

    @SuppressWarnings("unused")
    @Override
    public int compareTo(Post o) {

        if (this != null) {
            if (o != null) {
                return this.datetimeCreated.compareTo(o.datetimeCreated);
            } else {
                return 1;
            }
        }

        if (o != null)
            return -1;

        return 0;
    }

}