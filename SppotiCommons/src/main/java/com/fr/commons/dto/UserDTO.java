package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fr.commons.enumeration.GenderEnum;
import com.fr.commons.enumeration.GlobalAppStatusEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by djenanewail on 12/16/16.
 */
public class UserDTO extends AbstractCommonDTO
{
	
	private String firstName;
	private String lastName;
	private String username;
	private String address;
	private GenderEnum gender;
	
	//team admin decision about user request to join team
	private Boolean waitConfirmation;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date birthDate;
	
	private Integer xPosition;
	private Integer yPosition;
	
	@JsonProperty("sports")
	private List<SportDTO> sportDTOs = new ArrayList<>();
	
	private String password;
	private String oldPassword;
	private String phone;
	private String email;
	
	private Long techId;
	private boolean confirmed;
	private List<RoleDTO> userRoles = new ArrayList<>();
	//	private Date deactivationDate;
	
	private String cover;
	private String avatar;
	
	private Integer coverType;
	
	@JsonProperty("friend_id")
	private String friendUuid;
	
	@JsonProperty(value = "friend_status")
	private Integer friendStatus;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
	private Date datetimeCreated;
	
	private Boolean myProfile;
	
	private Boolean sppotiAdmin;
	private GlobalAppStatusEnum sppotiStatus;
	
	//used in team response.
	private String userId;
	
	private Boolean teamAdmin;
	private GlobalAppStatusEnum teamStatus;
	private Boolean teamCaptain;
	
	private Double rating;
	private Boolean hasRateOtherSppoters;
	
	private String language;
	private String timeZone;
	
	private boolean firstConnexion;
	private boolean isProfileComplete;
	private boolean readPrivacy;
	
	public boolean isFirstConnexion() {
		return this.firstConnexion;
	}
	
	public void setFirstConnexion(final boolean firstConnexion) {
		this.firstConnexion = firstConnexion;
	}
	
	public boolean isReadPrivacy() {
		return this.readPrivacy;
	}
	
	public void setReadPrivacy(final boolean readPrivacy) {
		this.readPrivacy = readPrivacy;
	}
	
	public String getFirstName()
	{
		return this.firstName;
	}
	
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}
	
	public String getLastName()
	{
		return this.lastName;
	}
	
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public void setUsername(final String username)
	{
		this.username = username;
	}
	
	public String getAddress()
	{
		return this.address;
	}
	
	public void setAddress(final String address)
	{
		this.address = address;
	}
	
	public Integer getxPosition()
	{
		return this.xPosition;
	}
	
	public void setxPosition(final Integer xPosition)
	{
		this.xPosition = xPosition;
	}
	
	public Integer getyPosition()
	{
		return this.yPosition;
	}
	
	public void setyPosition(final Integer yPosition)
	{
		this.yPosition = yPosition;
	}
	
	public List<SportDTO> getSportDTOs()
	{
		return this.sportDTOs;
	}
	
	public void setSportDTOs(final List<SportDTO> sportDTOs)
	{
		this.sportDTOs = sportDTOs;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public void setPassword(final String password)
	{
		this.password = password;
	}
	
	public String getPhone()
	{
		return this.phone;
	}
	
	public void setPhone(final String phone)
	{
		this.phone = phone;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public void setEmail(final String email)
	{
		this.email = email;
	}
	
	public String getCover()
	{
		return this.cover;
	}
	
	public void setCover(final String cover)
	{
		this.cover = cover;
	}
	
	public String getAvatar()
	{
		return this.avatar;
	}
	
	public void setAvatar(final String avatar)
	{
		this.avatar = avatar;
	}
	
	public Integer getCoverType()
	{
		return this.coverType;
	}
	
	public void setCoverType(final Integer coverType)
	{
		this.coverType = coverType;
	}
	
	public String getFriendUuid()
	{
		return this.friendUuid;
	}
	
	public void setFriendUuid(final String friendUuid)
	{
		this.friendUuid = friendUuid;
	}
	
	public Integer getFriendStatus()
	{
		return this.friendStatus;
	}
	
	public void setFriendStatus(final Integer friendStatus)
	{
		this.friendStatus = friendStatus;
	}
	
	public Date getDatetimeCreated()
	{
		return this.datetimeCreated;
	}
	
	public void setDatetimeCreated(final Date datetimeCreated)
	{
		this.datetimeCreated = datetimeCreated;
	}
	
	public Boolean getMyProfile()
	{
		return this.myProfile;
	}
	
	public void setMyProfile(final Boolean myProfile)
	{
		this.myProfile = myProfile;
	}
	
	public Boolean getTeamAdmin()
	{
		return this.teamAdmin;
	}
	
	public void setTeamAdmin(final Boolean teamAdmin)
	{
		this.teamAdmin = teamAdmin;
	}
	
	public Boolean getSppotiAdmin()
	{
		return this.sppotiAdmin;
	}
	
	public void setSppotiAdmin(final Boolean sppotiAdmin)
	{
		this.sppotiAdmin = sppotiAdmin;
	}
	
	public GlobalAppStatusEnum getTeamStatus()
	{
		return this.teamStatus;
	}
	
	public void setTeamStatus(final GlobalAppStatusEnum teamStatus)
	{
		this.teamStatus = teamStatus;
	}
	
	public GlobalAppStatusEnum getSppotiStatus()
	{
		return this.sppotiStatus;
	}
	
	public void setSppotiStatus(final GlobalAppStatusEnum sppotiStatus)
	{
		this.sppotiStatus = sppotiStatus;
	}
	
	public Date getBirthDate()
	{
		return this.birthDate;
	}
	
	public void setBirthDate(final Date birthDate)
	{
		this.birthDate = birthDate;
	}
	
	public String getUserId()
	{
		return this.userId;
	}
	
	public void setUserId(final String userId)
	{
		this.userId = userId;
	}
	
	public Boolean getTeamCaptain()
	{
		return this.teamCaptain;
	}
	
	public void setTeamCaptain(final Boolean teamCaptain)
	{
		this.teamCaptain = teamCaptain;
	}
	
	public Double getRating()
	{
		return this.rating;
	}
	
	public void setRating(final Double rating)
	{
		this.rating = rating;
	}
	
	public Boolean getHasRateOtherSppoters()
	{
		return this.hasRateOtherSppoters;
	}
	
	public void setHasRateOtherSppoters(final Boolean hasRateOtherSppoters)
	{
		this.hasRateOtherSppoters = hasRateOtherSppoters;
	}
	
	public GenderEnum getGender() {
		return this.gender;
	}
	
	public void setGender(final GenderEnum gender) {
		this.gender = gender;
	}
	
	public String getOldPassword()
	{
		return this.oldPassword;
	}
	
	public void setOldPassword(final String oldPassword)
	{
		this.oldPassword = oldPassword;
	}
	
	public String getLanguage()
	{
		return this.language;
	}
	
	public void setLanguage(final String language)
	{
		this.language = language;
	}
	
	public String getTimeZone()
	{
		return this.timeZone;
	}
	
	public void setTimeZone(final String timeZone)
	{
		this.timeZone = timeZone;
	}
	
	public boolean isConfirmed() {
		return this.confirmed;
	}
	
	public void setConfirmed(final boolean confirmed) {
		this.confirmed = confirmed;
	}
	
	public List<RoleDTO> getUserRoles() {
		return this.userRoles;
	}
	
	public void setUserRoles(final List<RoleDTO> userRoles) {
		this.userRoles = userRoles;
	}
	
	public Long getTechId() {
		return this.techId;
	}
	
	public void setTechId(final Long techId) {
		this.techId = techId;
	}
	
	public Boolean getWaitConfirmation() {
		return this.waitConfirmation;
	}
	
	public void setWaitConfirmation(final Boolean waitConfirmation) {
		this.waitConfirmation = waitConfirmation;
	}
	
	public boolean isProfileComplete() {
		return this.isProfileComplete;
	}
	
	public void setProfileComplete(final boolean profileComplete) {
		this.isProfileComplete = profileComplete;
	}
}