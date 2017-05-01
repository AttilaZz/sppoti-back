package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.commons.dto.post.PostDTO;

import java.util.List;
import java.util.Set;

/**
 * Created by: Wail DJENANE on Jun 11, 2016
 */
@JsonInclude(Include.NON_ABSENT)
public class ProfilePageDTO extends AbstractCommonDTO
{
	
	private String lastName;
	private String firstName;
	private String birthDate;
	private String sexe;
	private String telephone;
	private String email;
	private String username;
	
	private boolean isMyProfile;
	
	private String profileName;
	
	private List<PostDTO> userPosts;
	private Set<SportDTO> userSports;
	//    private List<MessageEntity> userMessages;
	private Set<String> userAddresses;
	
	// when switching to another profile
	private boolean allowPost = true;
	private boolean isMyFriend;
	
	public boolean isMyProfile()
	{
		return this.isMyProfile;
	}
	
	public void setMyProfile(final boolean isMyProfile)
	{
		this.isMyProfile = isMyProfile;
	}
	
	public String getBirthDate()
	{
		return this.birthDate;
	}
	
	public void setBirthDate(final String birthDate)
	{
		this.birthDate = birthDate;
	}
	
	public Set<String> getUserAddresses()
	{
		return this.userAddresses;
	}
	
	public void setUserAddresses(final Set<String> userAddresses)
	{
		this.userAddresses = userAddresses;
	}
	
	public boolean isAllowPost()
	{
		return this.allowPost;
	}
	
	public boolean isMyFriend()
	{
		return this.isMyFriend;
	}
	
	public void setMyFriend(final boolean isMyFriend)
	{
		this.isMyFriend = isMyFriend;
	}
	
	public void setAllowPost(final boolean allowPost)
	{
		this.allowPost = allowPost;
	}
	
	public String getLastName()
	{
		return this.lastName;
	}
	
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}
	
	public String getFirstName()
	{
		return this.firstName;
	}
	
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}
	
	public String getSexe()
	{
		return this.sexe;
	}
	
	public void setSexe(final String sexe)
	{
		this.sexe = sexe;
	}
	
	public String getTelephone()
	{
		return this.telephone;
	}
	
	public void setTelephone(final String telephone)
	{
		this.telephone = telephone;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public void setEmail(final String email)
	{
		this.email = email;
	}
	
	public String getProfileName()
	{
		return this.profileName;
	}
	
	public void setProfileName(final String profileName)
	{
		this.profileName = profileName;
	}
	
	public List<PostDTO> getUserPosts()
	{
		return this.userPosts;
	}
	
	public void setUserPosts(final List<PostDTO> userPosts)
	{
		this.userPosts = userPosts;
	}
	
	public Set<SportDTO> getUserSports()
	{
		return this.userSports;
	}
	
	public void setUserSports(final Set<SportDTO> userSports)
	{
		this.userSports = userSports;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public void setUsername(final String username)
	{
		this.username = username;
	}
	
}
