/**
 * 
 */
package com.fr.models;

/**
 * Created by: Wail DJENANE on Jun 15, 2016
 */
public class SignUpRequest {

	private String lastName;
	private String firstName;
	private String dateBorn;
	private String sexe;
	private String email;
	private String password;
	private String username;
	private Long[] sportId;

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

	public String getDateBorn() {
		return dateBorn;
	}

	public void setDateBorn(String dateBorn) {
		this.dateBorn = dateBorn;
	}

	public String getSexe() {
		return sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long[] getSportId() {
		return sportId;
	}

	public void setSportId(Long[] sportId) {
		this.sportId = sportId;
	}

}
