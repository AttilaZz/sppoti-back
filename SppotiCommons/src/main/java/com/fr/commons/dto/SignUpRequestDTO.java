package com.fr.commons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.internal.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 * Created by: Wail DJENANE on Jun 15, 2016
 */
public class SignUpRequestDTO extends AbstractCommonDTO{

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String firstName;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dateBorn;

    @NotEmpty
    //TODO: check genderType type (MALE, FEMALE) and reject others
    @JsonProperty("gender")
    private String genderType;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
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

    public Date getDateBorn() {
        return dateBorn;
    }

    public void setDateBorn(Date dateBorn) {
        this.dateBorn = dateBorn;
    }

    public String getGenderType() {
        return genderType;
    }

    public void setGenderType(String genderType) {
        this.genderType = genderType;
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
