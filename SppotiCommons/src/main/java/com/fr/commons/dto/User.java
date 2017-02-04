package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fr.models.GlobalAppStatus;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by djenanewail on 12/16/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String address;

    private Integer xPosition;
    private Integer yPosition;

    @SerializedName("sports")
    @JsonProperty("sports")
    private List<SportModel> sportModels;

    private String password;
    private String phone;
    private String email;

    private String cover;
    private String avatar;

    private Integer coverType;

    @SerializedName("friend_id")
    @JsonProperty("friend_id")
    private Integer friendUuid;

    @SerializedName("friend_status")
    @JsonProperty(value = "friend_status")
    private Integer friendStatus = GlobalAppStatus.PUBLIC_RELATION.getValue();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private Date datetimeCreated;

    private boolean myProfile;

    public User() {
    }

    public User(int id, String firstName, String lastName, String username, String cover, String avatar, int coverType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.cover = cover;
        this.avatar = avatar;
        this.coverType = coverType;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getxPosition() {
        return xPosition;
    }

    public void setxPosition(Integer xPosition) {
        this.xPosition = xPosition;
    }

    public Integer getyPosition() {
        return yPosition;
    }

    public void setyPosition(Integer yPosition) {
        this.yPosition = yPosition;
    }

    public List<SportModel> getSportModels() {
        return sportModels;
    }

    public void setSportModels(List<SportModel> sportModels) {
        this.sportModels = sportModels;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getCoverType() {
        return coverType;
    }

    public void setCoverType(Integer coverType) {
        this.coverType = coverType;
    }

    public Integer getFriendUuid() {
        return friendUuid;
    }

    public void setFriendUuid(Integer friendUuid) {
        this.friendUuid = friendUuid;
    }

    public Integer getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(Integer friendStatus) {
        this.friendStatus = friendStatus;
    }

    public Date getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(Date datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public boolean isMyProfile() {
        return myProfile;
    }

    public void setMyProfile(boolean myProfile) {
        this.myProfile = myProfile;
    }
}