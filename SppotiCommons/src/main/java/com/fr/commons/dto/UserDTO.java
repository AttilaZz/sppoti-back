package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by djenanewail on 12/16/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends AbstractCommonDTO{

    private String firstName;
    private String lastName;
    private String username;
    private String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date birthDate;

    private Integer xPosition;
    private Integer yPosition;

    @SerializedName("sports")
    @JsonProperty("sports")
    private List<SportModelDTO> sportModelDTOs;

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
    private Integer friendStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private Date datetimeCreated;

    private Boolean myProfile;

    private Boolean teamAdmin;
    private Boolean sppotiAdmin;

    private Integer teamStatus;
    private Integer sppotiStatus;
    private Integer userId;

    public UserDTO() {
    }

    public UserDTO(int id, String firstName, String lastName, String username, String cover, String avatar, Integer coverType, Boolean isTeamAdmin, Boolean isSppotiAdmin, Integer teamStatus, Integer sppotiStatus, Integer userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.cover = cover;
        this.avatar = avatar;
        this.coverType = coverType;
        this.teamAdmin = isTeamAdmin;
        this.sppotiAdmin = isSppotiAdmin;
        this.id = id;
        this.teamStatus = teamStatus;
        this.sppotiStatus = sppotiStatus;
        this.userId = userId;
    }

    public UserDTO(int uuid, String firstName, String lastName, String username, String cover, String avatar, Integer coverType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.cover = cover;
        this.avatar = avatar;
        this.coverType = coverType;
        this.id = uuid;
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

    public List<SportModelDTO> getSportModelDTOs() {
        return sportModelDTOs;
    }

    public void setSportModelDTOs(List<SportModelDTO> sportModelDTOs) {
        this.sportModelDTOs = sportModelDTOs;
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

    public Boolean getMyProfile() {
        return myProfile;
    }

    public void setMyProfile(Boolean myProfile) {
        this.myProfile = myProfile;
    }

    public Boolean getTeamAdmin() {
        return teamAdmin;
    }

    public void setTeamAdmin(Boolean teamAdmin) {
        this.teamAdmin = teamAdmin;
    }

    public Boolean getSppotiAdmin() {
        return sppotiAdmin;
    }

    public void setSppotiAdmin(Boolean sppotiAdmin) {
        this.sppotiAdmin = sppotiAdmin;
    }

    public Integer getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(Integer teamStatus) {
        this.teamStatus = teamStatus;
    }

    public Integer getSppotiStatus() {
        return sppotiStatus;
    }

    public void setSppotiStatus(Integer sppotiStatus) {
        this.sppotiStatus = sppotiStatus;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}