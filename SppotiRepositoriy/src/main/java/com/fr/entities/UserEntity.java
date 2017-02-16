package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */

@Entity
@Table(name = "USER")
@JsonInclude(Include.NON_EMPTY)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(unique = true)
    private int uuid = UUID.randomUUID().hashCode();

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false, columnDefinition = "DATE")
    private Date dateBorn;

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
    private SortedSet<PostEntity> userPosts = new TreeSet<PostEntity>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userSppoti")
    private Set<SppotiEntity> userSppoties;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userMessage")
    private Set<MessageEntity> userMessages;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<CommentEntity> commentEntities;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<LikeContentEntity> likes;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    @Where(clause = "is_selected='1'")
    private Set<ResourcesEntity> ressources;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", nullable = false)
    @JsonIgnore
    private Set<RoleEntity> roles;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "sport_id", nullable = false)
    @JsonIgnore
    private Set<SportEntity> relatedSports;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "users")
    @OrderBy("dateTime DESC")
    private SortedSet<AddressEntity> addresses;

//    @ManyToMany(cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "team_id")
//    @JsonIgnore
//    private Set<TeamEntity> team;

//    @ElementCollection
//    private Map<TeamEntity, Boolean> teamStatus = new TreeMap<TeamEntity, Boolean>();

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

    public UserEntity() {}

    public Set<PostEntity> getUserPosts() {
        return userPosts;
    }

    public void setUserPosts(SortedSet<PostEntity> userPosts) {
        this.userPosts = userPosts;
    }

    public Set<SppotiEntity> getUserSppoties() {
        return userSppoties;
    }

    public void setUserSppoties(Set<SppotiEntity> userSppoties) {
        this.userSppoties = userSppoties;
    }

    public Set<MessageEntity> getUserMessages() {
        return userMessages;
    }

    public void setUserMessages(Set<MessageEntity> userMessages) {
        this.userMessages = userMessages;
    }

    public Set<CommentEntity> getCommentEntities() {
        return commentEntities;
    }

    public void setCommentEntities(Set<CommentEntity> commentEntities) {
        this.commentEntities = commentEntities;
    }

    public Set<LikeContentEntity> getLikes() {
        return likes;
    }

    public void setLikes(Set<LikeContentEntity> likes) {
        this.likes = likes;
    }

    public Set<ResourcesEntity> getRessources() {
        return ressources;
    }

    public void setRessources(Set<ResourcesEntity> ressources) {
        this.ressources = ressources;
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

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    public Set<SportEntity> getRelatedSports() {
        return relatedSports;
    }

    public void setRelatedSports(Set<SportEntity> relatedSports) {
        this.relatedSports = relatedSports;
    }

    public SortedSet<AddressEntity> getAddresses() {
        return addresses;
    }

    public void setAddresses(SortedSet<AddressEntity> addresses) {
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

    public Date getDateBorn() {
        return dateBorn;
    }

    public void setDateBorn(Date dateBorn) {
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

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
