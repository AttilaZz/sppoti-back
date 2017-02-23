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
 * sppoti 3: address of a sppoti 4: anything else related to a sppoti 5: related to
 * one sport
 */

@Entity
@Table(name = "POST")
@JsonInclude(Include.NON_EMPTY)
public class PostEntity
        extends AbstractCommonEntity
        implements Comparable<PostEntity> {

    @Column(length = 500)
    @SerializedName("text")
    private String content;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetimeCreated = new Date();

    private String video;

    @ElementCollection
    private Set<String> album;

    @Column(nullable = false)
    private int visibility = 0;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
    @OrderBy("dateTime DESC")
    private SortedSet<AddressEntity> addresses = new TreeSet<AddressEntity>();

    public PostEntity() {
        super();
    }

    public PostEntity(PostEntity post) {
        this.content = post.getContent();
        this.album = post.getAlbum();
        this.video = post.getVideo();
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "sport_id")
    private SportEntity sport;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
    @OrderBy("datetimeCreated DESC")
    private SortedSet<CommentEntity> commentEntities = new TreeSet<CommentEntity>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
    private Set<LikeContentEntity> likes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "post")
    private Set<EditHistoryEntity> editList;

    @Column(name = "target_user")
    private int targetUserProfileUuid;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public SportEntity getSport() {
        return sport;
    }

    public void setSport(SportEntity sport) {
        this.sport = sport;
    }

    public Date getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(Date datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public SortedSet<CommentEntity> getCommentEntities() {
        return commentEntities;
    }

    public void setCommentEntities(SortedSet<CommentEntity> commentEntities) {
        this.commentEntities = commentEntities;
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

    public SortedSet<AddressEntity> getAddresses() {
        return addresses;
    }

    public void setAddresses(SortedSet<AddressEntity> addresses) {
        this.addresses = addresses;
    }

    public int getTargetUserProfileUuid() {
        return targetUserProfileUuid;
    }

    public void setTargetUserProfileUuid(int targetUserProfileUuid) {
        this.targetUserProfileUuid = targetUserProfileUuid;
    }

    @SuppressWarnings("unused")
    @Override
    public int compareTo(PostEntity o) {

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
        if (!(o instanceof PostEntity)) return false;
        if (!super.equals(o)) return false;

        PostEntity that = (PostEntity) o;

        if (visibility != that.visibility) return false;
        if (isDeleted != that.isDeleted) return false;
        if (targetUserProfileUuid != that.targetUserProfileUuid) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (datetimeCreated != null ? !datetimeCreated.equals(that.datetimeCreated) : that.datetimeCreated != null)
            return false;
        if (video != null ? !video.equals(that.video) : that.video != null) return false;
        return album != null ? album.equals(that.album) : that.album == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (datetimeCreated != null ? datetimeCreated.hashCode() : 0);
        result = 31 * result + (video != null ? video.hashCode() : 0);
        result = 31 * result + (album != null ? album.hashCode() : 0);
        result = 31 * result + visibility;
        result = 31 * result + (isDeleted ? 1 : 0);
        result = 31 * result + targetUserProfileUuid;
        return result;
    }
}
