package com.fr.commons.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by djenanewail on 4/8/17.
 */
public class ContactDTO extends AbstractCommonDTO{

    @NotEmpty
    private String message;
    @NotEmpty
    private String name;
    @NotEmpty
    private String phone;
    @NotEmpty
    private String email;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
