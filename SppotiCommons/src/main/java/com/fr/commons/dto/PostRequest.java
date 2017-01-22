package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fr.entities.Address;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

/**
 * Created by: Wail DJENANE on Jun 13, 2016
 */
@JsonInclude(Include.NON_ABSENT)
public class PostRequest {

    private Long sportId;

    private Long gameId;

    private Data content;

    private Address address;

    private String avatar;

    private String cover;

    private int visibility;

    @JsonProperty("targetUser")
    private int targetUseruuid;

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getSportId() {
        return sportId;
    }

    public void setSportId(Long sportId) {
        this.sportId = sportId;
    }

    public Data getContent() {
        return content;
    }

    public void setContent(Data content) {
        this.content = content;
    }

    public String getNewAvatar() {
        return avatar;
    }

    public void setNewAvatar(String newAvatar) {
        this.avatar = newAvatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getTargetUseruuid() {
        return targetUseruuid;
    }

    public void setTargetUseruuid(int targetUseruuid) {
        this.targetUseruuid = targetUseruuid;
    }

    public class Data {
        @SerializedName("text")
        @JsonProperty("text")
        private String content;
        private Set<String> imageLink;
        private String videoLink;

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

    }
}
