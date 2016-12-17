package com.fr.models;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by djenanewail on 12/16/16.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User {

    private String firstname;
    private String lastName;
    private String username;
    private String avatar;

    private int id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
