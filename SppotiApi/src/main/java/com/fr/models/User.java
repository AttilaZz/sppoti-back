package com.fr.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by djenanewail on 12/16/16.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String address;

    @SerializedName("sports")
    @JsonProperty("sports")
    private List<SportModel> sportModels;

    private String password;
    private String phone;
    private String email;

    private String cover;
    private String avatar;
    private int coverType;

    @SerializedName("friend_id")
    @JsonProperty("friend_id")
    private int friendUuid = -1;
    @SerializedName("friend_status")
    @JsonProperty("friend_status")
    private int friendStatus = -1;

    private boolean myProfile = false;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCoverType() {
        return coverType;
    }

    public void setCoverType(int coverType) {
        this.coverType = coverType;
    }

    public List<SportModel> getSportModels() {
        return sportModels;
    }

    public void setSportModels(List<SportModel> sportModels) {
        this.sportModels = sportModels;
    }

    public int getFriendUuid() {
        return friendUuid;
    }

    public void setFriendUuid(int friendUuid) {
        this.friendUuid = friendUuid;
    }

    public int getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(int friendStatus) {
        this.friendStatus = friendStatus;
    }

    public boolean isMyProfile() {
        return myProfile;
    }

    public void setMyProfile(boolean myProfile) {
        this.myProfile = myProfile;
    }
}