package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fr.commons.enumeration.GenderEnum;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 * Created by: Wail DJENANE on Jun 15, 2016
 */
public class SignUpDTO extends AbstractCommonDTO
{
	
	private String lastName;
	private String firstName;
	private GenderEnum gender;
	private String password;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date dateBorn;
	
	@Email
	@NotEmpty
	private String email;
	
	@NotEmpty
	private String username;
	
	@JsonProperty("new_password")
	private String newPassword;
	
	@JsonProperty("old_password")
	private String oldPassword;
	
	private String timeZone;
	
	private String facebookId;
	private String googleId;
	private String twitterId;
	
	private String cover;
	private Integer coverType;
	private String avatar;
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}
	
	public Date getDateBorn() {
		return this.dateBorn;
	}
	
	public void setDateBorn(final Date dateBorn) {
		this.dateBorn = dateBorn;
	}
	
	public GenderEnum getGender() {
		return this.gender;
	}
	
	public void setGender(final GenderEnum gender) {
		this.gender = gender;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public void setEmail(final String email)
	{
		this.email = email;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public void setPassword(final String password)
	{
		this.password = password;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public void setUsername(final String username)
	{
		this.username = username;
	}
	
	public String getNewPassword()
	{
		return this.newPassword;
	}
	
	public void setNewPassword(final String newPassword)
	{
		this.newPassword = newPassword;
	}
	
	public String getOldPassword()
	{
		return this.oldPassword;
	}
	
	public void setOldPassword(final String oldPassword)
	{
		this.oldPassword = oldPassword;
	}
	
	public String getTimeZone() {
		return this.timeZone;
	}
	
	public void setTimeZone(final String timeZone) {
		this.timeZone = timeZone;
	}
	
	public String getFacebookId() {
		return this.facebookId;
	}
	
	public void setFacebookId(final String facebookId) {
		this.facebookId = facebookId;
	}
	
	public String getGoogleId() {
		return this.googleId;
	}
	
	public void setGoogleId(final String googleId) {
		this.googleId = googleId;
	}
	
	public String getTwitterId() {
		return this.twitterId;
	}
	
	public void setTwitterId(final String twitterId) {
		this.twitterId = twitterId;
	}
	
	public String getCover() {
		return this.cover;
	}
	
	public void setCover(final String cover) {
		this.cover = cover;
	}
	
	public Integer getCoverType() {
		return this.coverType;
	}
	
	public void setCoverType(final Integer coverType) {
		this.coverType = coverType;
	}
	
	public String getAvatar() {
		return this.avatar;
	}
	
	public void setAvatar(final String avatar) {
		this.avatar = avatar;
	}
}
