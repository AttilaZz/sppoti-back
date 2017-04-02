package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * Created by djenanewail on 12/16/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends AbstractCommonDTO {

    private String firstName;
    private String lastName;
    private String username;
    private String address;
    private String gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date birthDate;

    private Integer xPosition;
    private Integer yPosition;

    @JsonProperty("sports")
    private List<SportDTO> sportDTOs;

    private String password;
    private String phone;
    private String email;

    private String cover;
    private String avatar;

    private Integer coverType;

    @JsonProperty("friend_id")
    private Integer friendUuid;

    @JsonProperty(value = "friend_status")
    private Integer friendStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private Date datetimeCreated;

    private Boolean myProfile;

    private Boolean sppotiAdmin;
    private Integer sppotiStatus;

    //used in team response.
    private Integer userId;

    private Boolean teamAdmin;
    private Integer teamStatus;
    private Boolean teamCaptain;

    private Double rating;
    private Boolean hasRateOtherSppoters;

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

    public List<SportDTO> getSportDTOs() {
        return sportDTOs;
    }

    public void setSportDTOs(List<SportDTO> sportDTOs) {
        this.sportDTOs = sportDTOs;
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

    public Boolean getTeamCaptain() {
        return teamCaptain;
    }

    public void setTeamCaptain(Boolean teamCaptain) {
        this.teamCaptain = teamCaptain;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Boolean getHasRateOtherSppoters() {
        return hasRateOtherSppoters;
    }

    public void setHasRateOtherSppoters(Boolean hasRateOtherSppoters) {
        this.hasRateOtherSppoters = hasRateOtherSppoters;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}