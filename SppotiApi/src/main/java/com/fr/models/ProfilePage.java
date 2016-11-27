/**
 * 
 */
package com.fr.models;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.pojos.Address;
import com.fr.pojos.Messages;
import com.fr.pojos.Sport;

/**
 * Created by: Wail DJENANE on Jun 11, 2016
 */
@JsonInclude(Include.NON_ABSENT)
public class ProfilePage {

	private String lastName;
	private String firstName;
	private String birthDate;
	private String sexe;
	private String telephone;
	private String email;
	private String username;

	private boolean isMyProfile;

	private String profileName;

	private List<PostResponse> userPosts;
	private Set<Sport> userSports;
	private List<Messages> userMessages;
	private Set<Address> userAddresses;

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

	public Set<Address> getUserAddresses() {
		return userAddresses;
	}

	public void setUserAddresses(Set<Address> userAddresses) {
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

	public List<PostResponse> getUserPosts() {
		return userPosts;
	}

	public void setUserPosts(List<PostResponse> userPosts) {
		this.userPosts = userPosts;
	}

	public Set<Sport> getUserSports() {
		return userSports;
	}

	public void setUserSports(Set<Sport> sportSet) {
		this.userSports = sportSet;
	}

	public List<Messages> getUserMessages() {
		return userMessages;
	}

	public void setUserMessages(List<Messages> userMessages) {
		this.userMessages = userMessages;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
