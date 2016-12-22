/**
 *
 */
package com.fr.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fr.entities.Address;
import com.fr.entities.Post;
import com.fr.entities.Sppoti;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * Created by: Wail DJENANE on Aug 7, 2016
 */
@JsonInclude(Include.NON_ABSENT)
public class PostResponse {

    private int id;
    private Long sportId;

    private String datetime;

    @JsonProperty("text")
    private String content;

    @JsonProperty("album")
    private Set<String> imageLink;

    @JsonProperty("video")
    private String videoLink;

    private Sppoti game;

    private int commentsCount;

    private int likeCount;
    private boolean isLikedByUser;

    private boolean edited;

    private List<HeaderData> postLikers;
    private SortedSet<Address> addresses;

    private int visibility;

    private String firstName;
    private String lastName;
    private String username;

    private boolean myPost;

    public PostResponse(Post post) {
        this.content = post.getContent();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PostResponse() {

    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public List<HeaderData> getPostLikers() {
        return postLikers;
    }

    public void setPostLikers(List<HeaderData> postLikers) {
        this.postLikers = postLikers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatetimeCreated() {
        return datetime;
    }

    public void setDatetimeCreated(String datetimeCreated) {
        this.datetime = datetimeCreated;
    }

    public Set<String> getImageLink() {
        return imageLink;
    }

    public void setImageLink(Set<String> imageLink) {
        this.imageLink = imageLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public Sppoti getGame() {
        return game;
    }

    public void setGame(Sppoti game) {
        this.game = game;
    }


    public Long getSportId() {
        return sportId;
    }

    public void setSportId(Long sportId) {
        this.sportId = sportId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLikedByUser() {
        return isLikedByUser;
    }

    public void setLikedByUser(boolean isLikedByUser) {
        this.isLikedByUser = isLikedByUser;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean isEdited) {
        this.edited = isEdited;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public boolean isMyPost() {
        return myPost;
    }

    public void setMyPost(boolean isMyPost) {
        this.myPost = isMyPost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SortedSet<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(SortedSet<Address> addresses) {
        this.addresses = addresses;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }
}
