package com.fr.commons.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.entities.CommentEntity;

/**
 * Created by: Wail DJENANE on Aug 12, 2016
 */
@JsonInclude(Include.NON_EMPTY)
public class CommentDTO {

    private int id;
    private int postId;

    private String authorFirstName;
    private String authorLastName;
    private String authorUsername;
    private String authorAvatar;

    private String creationDate;

    private String text;
    private String imageLink;
    private String videoLink;

    private boolean isMyComment;

    private boolean isLikedByUser;
    private int likeCount;

    private boolean isEdited = false;

    private List<HeaderDataDTO> commentLikers;

    public CommentDTO() {
    }

    public CommentDTO(CommentEntity commentEntity, UserDTO authorComment) {
        this.id = commentEntity.getUuid();

        this.authorAvatar = authorComment.getAvatar();
        this.authorFirstName = commentEntity.getUser().getFirstName();
        this.authorUsername = commentEntity.getUser().getUsername();
        this.authorLastName = commentEntity.getUser().getLastName();

        this.creationDate = commentEntity.getDatetimeCreated();
        this.text = commentEntity.getContent();
        this.imageLink = commentEntity.getImageLink();
        this.videoLink = commentEntity.getVideoLink();

        this.isEdited = !commentEntity.getEditList().isEmpty();
    }

    public List<HeaderDataDTO> getCommentLikers() {
        return commentLikers;
    }

    public void setCommentLikers(List<HeaderDataDTO> commentLikers) {
        this.commentLikers = commentLikers;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public boolean isMyComment() {
        return isMyComment;
    }

    public void setMyComment(boolean isMyComment) {
        this.isMyComment = isMyComment;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public boolean isLikedByUser() {
        return isLikedByUser;
    }

    public void setLikedByUser(boolean isLikedByUser) {
        this.isLikedByUser = isLikedByUser;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean isEdited) {
        this.isEdited = isEdited;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
