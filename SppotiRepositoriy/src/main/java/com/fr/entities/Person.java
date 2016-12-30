package com.fr.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by djenanewail on 12/13/16.
 */

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(nullable = false)
    protected Long id;

    @Column(unique = true)
    protected int uuid = UUID.randomUUID().hashCode();

//    @ElementCollection
//    protected Map<String, String> avatars = new TreeMap<String, String>();

    @Column(nullable = false)
    protected String lastName;

    @Column(nullable = false)
    protected String firstName;

    @Column(nullable = false)
    protected String dateBorn;

    @Column(nullable = false)
    protected String sexe;

    @Column(unique = true)
    protected String telephone;

    @Column(nullable = false, unique = true)
    protected String email;

    @Column(nullable = false, unique = true)
    protected String username;

    protected Person() {
        super();
    }

    protected Person(Users user) {
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
        this.dateBorn = user.getDateBorn();
        this.sexe = user.getSexe();
        this.telephone = user.getTelephone();
        this.email = user.getEmail();
        this.username = user.getUsername();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }
}