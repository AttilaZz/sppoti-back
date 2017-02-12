package com.fr.commons.dto;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.entities.Message;
import com.fr.entities.Sport;

/**
 * Created by: Wail DJENANE on Jun 11, 2016
 */
@JsonInclude(Include.NON_ABSENT)
public class ProfilePageDTO {

    private String lastName;
    private String firstName;
    private String birthDate;
    private String sexe;
    private String telephone;
    private String email;
    private String username;

    private boolean isMyProfile;

    private String profileName;

    private List<PostResponseDTO> userPosts;
    private Set<Sport> userSports;
    private List<Message> userMessages;
    private Set<String> userAddresses;

    // when switching to another profile
    private boolean allowPost = true;
    private boolean isMyFriend;

    public boolean isMyProfile() {
        return isMyProfile;
    }

    public void setMyProfile(boolean isMyProfile) {
        this.isMyProfile = isMyProfile;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Set<String> getUserAddresses() {
        return userAddresses;
    }

    public void setUserAddresses(Set<String> userAddresses) {
        this.userAddresses = userAddresses;
    }

    public boolean isAllowPost() {
        return allowPost;
    }

    public boolean isMyFriend() {
        return isMyFriend;
    }

    public void setMyFriend(boolean isMyFriend) {
        this.isMyFriend = isMyFriend;
    }

    public void setAllowPost(boolean allowPost) {
        this.allowPost = allowPost;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public List<PostResponseDTO> getUserPosts() {
        return userPosts;
    }

    public void setUserPosts(List<PostResponseDTO> userPosts) {
        this.userPosts = userPosts;
    }

    public Set<Sport> getUserSports() {
        return userSports;
    }

    public void setUserSports(Set<Sport> sportSet) {
        this.userSports = sportSet;
    }

    public List<Message> getUserMessages() {
        return userMessages;
    }

    public void setUserMessages(List<Message> userMessages) {
        this.userMessages = userMessages;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
