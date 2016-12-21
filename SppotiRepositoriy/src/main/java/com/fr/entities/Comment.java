package com.fr.entities;

import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by: Wail DJENANE on Aug 11, 2016
 */

@Entity
@JsonInclude(Include.NON_EMPTY)
public class Comment implements Comparable<Comment> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    protected int uuid = UUID.randomUUID().hashCode();

    @JsonProperty("text")
    private String content;
    private String imageLink;
    private String videoLink;

    private boolean deleted = false;

    @Column(nullable = false)
    private String datetimeCreated = new DateTime().toString();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private Users user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private Post postComment;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "comment")
    private Set<LikeContent> likes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "comment")
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

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Post getPostComment() {
        return postComment;
    }

    public void setPostComment(Post postComment) {
        this.postComment = postComment;
    }

    public String getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(String datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    @SuppressWarnings("unused")
    @Override
    public int compareTo(Comment o) {
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
