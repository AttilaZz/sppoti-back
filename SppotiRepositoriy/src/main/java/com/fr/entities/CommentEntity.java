package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Created by: Wail DJENANE on Aug 11, 2016
 */

@Entity
@Table(name = "COMMENT")
@JsonInclude(Include.NON_EMPTY)
public class CommentEntity
        extends AbstractCommonEntity
        implements Comparable<CommentEntity> {

    @JsonProperty("text")
    private String content;
    private String imageLink;
    private String videoLink;

    private boolean deleted = false;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetimeCreated = new Date();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private PostEntity post;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "comment")
    private Set<LikeContentEntity> likes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "comment")
    private Set<EditHistoryEntity> editList;

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

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    public Date getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(Date datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public Set<LikeContentEntity> getLikes() {
        return likes;
    }

    public void setLikes(Set<LikeContentEntity> likes) {
        this.likes = likes;
    }

    public Set<EditHistoryEntity> getEditList() {
        return editList;
    }

    public void setEditList(Set<EditHistoryEntity> editList) {
        this.editList = editList;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @SuppressWarnings("unused")
    @Override
    public int compareTo(CommentEntity o) {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentEntity)) return false;
        if (!super.equals(o)) return false;

        CommentEntity that = (CommentEntity) o;

        if (deleted != that.deleted) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (imageLink != null ? !imageLink.equals(that.imageLink) : that.imageLink != null) return false;
        if (videoLink != null ? !videoLink.equals(that.videoLink) : that.videoLink != null) return false;
        return datetimeCreated != null ? datetimeCreated.equals(that.datetimeCreated) : that.datetimeCreated == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (imageLink != null ? imageLink.hashCode() : 0);
        result = 31 * result + (videoLink != null ? videoLink.hashCode() : 0);
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + (datetimeCreated != null ? datetimeCreated.hashCode() : 0);
        return result;
    }
}
