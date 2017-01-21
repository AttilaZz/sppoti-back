package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */

@Entity
@JsonInclude(Include.NON_EMPTY)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;


    private int uuid = UUID.randomUUID().hashCode();

//    @ElementCollection
//    private Map<String, String> avatars = new TreeMap<String, String>();

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String dateBorn;

    @Column(nullable = false)
    private String sexe;

    @Column(unique = true)
    private String telephone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false, unique = true)
    private String confirmationCode;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    private boolean deleted = false;

    @JsonIgnore
    private boolean confirmed = false;

    @Column
    private String job;

    @Column
    private String description;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @OrderBy("datetimeCreated DESC")
    private SortedSet<Post> userPosts = new TreeSet<Post>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userSppoti")
    private Set<Sppoti> userSppoties;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userMessage")
    private Set<Messages> userMessages;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<LikeContent> likes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "notifSender")
    private Set<Notifications> notifications;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    @Where(clause = "is_selected='1'")
    private Set<Resources> ressources;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", nullable = false)
    @JsonIgnore
    private Set<Roles> roles;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "sport_id", nullable = false)
    @JsonIgnore
    private Set<Sport> relatedSports;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "users")
    @OrderBy("dateTime DESC")
    private SortedSet<Address> addresses;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id", nullable = true)
    @JsonIgnore
    private Team team;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Users() {
    }

    public Set<Post> getUserPosts() {
        return userPosts;
    }

    public void setUserPosts(SortedSet<Post> userPosts) {
        this.userPosts = userPosts;
    }

    public Set<Sppoti> getUserSppoties() {
        return userSppoties;
    }

    public void setUserSppoties(Set<Sppoti> userSppoties) {
        this.userSppoties = userSppoties;
    }

    public Set<Messages> getUserMessages() {
        return userMessages;
    }

    public void setUserMessages(Set<Messages> userMessages) {
        this.userMessages = userMessages;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<LikeContent> getLikes() {
        return likes;
    }

    public void setLikes(Set<LikeContent> likes) {
        this.likes = likes;
    }

    public Set<Resources> getRessources() {
        return ressources;
    }

    public void setRessources(Set<Resources> ressources) {
        this.ressources = ressources;
    }

    public Set<Notifications> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notifications> notifications) {
        this.notifications = notifications;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }

    public Set<Sport> getRelatedSports() {
        return relatedSports;
    }

    public void setRelatedSports(Set<Sport> relatedSports) {
        this.relatedSports = relatedSports;
    }

    public SortedSet<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(SortedSet<Address> addresses) {
        this.addresses = addresses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
