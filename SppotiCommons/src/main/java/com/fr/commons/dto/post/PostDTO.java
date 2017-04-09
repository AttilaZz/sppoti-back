package com.fr.commons.dto.post;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fr.commons.dto.AbstractCommonDTO;
import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.HeaderDataDTO;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by: Wail DJENANE on Aug 7, 2016
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostDTO extends AbstractCommonDTO {

    private Long sportId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+1")
    private Date datetimeCreated;

    @JsonProperty("text")
    private String content;

    @JsonProperty("album")
    private Set<String> imageLink;

    @JsonProperty("video")
    private String videoLink;

    private List<CommentDTO> comment;
    private int commentsCount;

    private int likeCount;
    private boolean isLikedByUser;

    private boolean edited;

    private List<HeaderDataDTO> likers;

    private int visibility;

    private String firstName;
    private String lastName;
    private String username;
    private String avatar;

    private TargetUser targetUser;

    private boolean myPost;

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

    public PostDTO() {

    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public List<HeaderDataDTO> getLikers() {
        return likers;
    }

    public void setLikers(List<HeaderDataDTO> likers) {
        this.likers = likers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public boolean isMyPost() {
        return myPost;
    }

    public void setMyPost(boolean isMyPost) {
        this.myPost = isMyPost;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Date getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(Date datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public void setTargetUser(TargetUser targetUser) {
        this.targetUser = targetUser;
    }

    public List<CommentDTO> getComment() {
        return comment;
    }

    public void setComment(List<CommentDTO> comment) {
        this.comment = comment;
    }

    public TargetUser getTargetUser() {
        return targetUser;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setTargetUser(String firstName, String lastName, String username, int id, boolean myAccount) {
        this.targetUser = new TargetUser(firstName, lastName, username, id, myAccount);
    }

    public class TargetUser {
        public TargetUser() {
        }

        private int id;
        private String firstName;
        private String lastName;
        private String username;

        private boolean myAccount;


        public TargetUser(String firstName, String lastName, String username, int id, boolean myAccount) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.id = id;
            this.myAccount = myAccount;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getUsername() {
            return username;
        }

        public int getId() {
            return id;
        }

        public boolean isMyAccount() {
            return myAccount;
        }
    }
}
