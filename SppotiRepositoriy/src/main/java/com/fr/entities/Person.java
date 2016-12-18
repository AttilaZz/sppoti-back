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

    protected int uuid = UUID.randomUUID().hashCode();

    @ElementCollection
    protected Map<String, String> avatars = new TreeMap<String, String>();

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

    @Column(nullable = false)
    protected boolean isConfirmed = false;

    @Column(nullable = false, unique = true)
    protected String username;

    @Column(name = "job")
    protected String job;

    @Column(name = "description")
    protected String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", nullable = false)
    protected Set<Roles> userRoles;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "sport_id", nullable = false)
    protected Set<Sport> relatedSports;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "users")
    @OrderBy("dateTime DESC")
    protected SortedSet<Address> addresses;

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
        this.job = user.getJob();
        this.description = user.getDescription();
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

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Roles> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<Roles> userRoles) {
        this.userRoles = userRoles;
    }

    public Set<Sport> getRelatedSports() {
        return relatedSports;
    }

    public void setRelatedSports(Set<Sport> relatedSports) {
        this.relatedSports = relatedSports;
    }

    public Map<String, String> getAvatars() {
        return avatars;
    }

    public void setAvatars(Map<String, String> avatars) {
        this.avatars = avatars;
    }

    public SortedSet<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(SortedSet<Address> addresses) {
        this.addresses = addresses;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }
}
