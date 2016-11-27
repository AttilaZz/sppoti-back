/**
 * 
 */
package com.fr.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fr.pojos.Address;
import com.fr.pojos.Sppoti;

/**
 * Created by: Wail DJENANE on Aug 7, 2016
 */
@JsonInclude(Include.NON_ABSENT)
public class PostResponse {

	private Long id;
	private Long sportId;

	private String datetime;

	@JsonProperty("text")
	private String content;
	private String[] imageLink;
	private String videoLink;

	private Sppoti game;

	private List<CommentModel> postComments;
	private Long commentsCount = 0L;

	private int likeCount = 0;
	private boolean isLikedByUser;

	private boolean isEdited;

	private List<HeaderData> postLikers;

	private Address address;

	private int visibility;

	private String firstName;
	private String lastName;
	private String username;

	private boolean isMyPost;

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

	public String[] getImageLink() {
		return imageLink;
	}

	public void setImageLink(String[] imageLink) {
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<CommentModel> getPostComments() {
		return postComments;
	}

	public void setPostComments(List<CommentModel> postComments) {
		this.postComments = postComments;
	}

	public Long getSportId() {
		return sportId;
	}

	public void setSportId(Long sportId) {
		this.sportId = sportId;
	}

	public Long getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(Long long1) {
		this.commentsCount = long1;
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
		return isEdited;
	}

	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public boolean isMyPost() {
		return isMyPost;
	}

	public void setMyPost(boolean isMyPost) {
		this.isMyPost = isMyPost;
	}

}
