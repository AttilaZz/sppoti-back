package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.commons.enumeration.GenderEnum;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */

@Entity
@Table(name = "USER")
@JsonInclude(Include.NON_EMPTY)
public class UserEntity
        extends AbstractCommonEntity {

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Temporal(TemporalType.DATE)
    @Past
    @Column(nullable = false)
    private Date dateBorn;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(unique = true)
    private String telephone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String confirmationCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date accountCreationDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date accountMaxActivationDate;

    @Column(nullable = false)
    private String password;

    private boolean deleted = false;
    private boolean confirmed = false;

    private String job;
    private String description;

    private String recoverCode;

    @Temporal(TemporalType.TIMESTAMP)
    private Date recoverCodeCreationDate;

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
    private Set<ResourcesEntity> resources;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", nullable = false)
    @JsonIgnore
    private Set<RoleEntity> roles;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "sport_id")
    @JsonIgnore
    private Set<SportEntity> relatedSports;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "users")
    @OrderBy("dateTime DESC")
    private SortedSet<AddressEntity> addresses;

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

    public UserEntity() {
    }

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

    public Set<ResourcesEntity> getResources() {
        return resources;
    }

    public void setResources(Set<ResourcesEntity> resources) {
        this.resources = resources;
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

    public String getRecoverCode() {
        return recoverCode;
    }

    public void setRecoverCode(String recoverCode) {
        this.recoverCode = recoverCode;
    }

    public Date getRecoverCodeCreationDate() {
        return recoverCodeCreationDate;
    }

    public void setRecoverCodeCreationDate(Date recoverCodeCreationDate) {
        this.recoverCodeCreationDate = recoverCodeCreationDate;
    }

    public Date getAccountCreationDate() {
        return accountCreationDate;
    }

    public void setAccountCreationDate(Date accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public Date getAccountMaxActivationDate() {
        return accountMaxActivationDate;
    }

    public void setAccountMaxActivationDate(Date accountMaxActivationDate) {
        this.accountMaxActivationDate = accountMaxActivationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        if (!super.equals(o)) return false;

        UserEntity that = (UserEntity) o;

        if (deleted != that.deleted) return false;
        if (confirmed != that.confirmed) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (dateBorn != null ? !dateBorn.equals(that.dateBorn) : that.dateBorn != null) return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (telephone != null ? !telephone.equals(that.telephone) : that.telephone != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (confirmationCode != null ? !confirmationCode.equals(that.confirmationCode) : that.confirmationCode != null)
            return false;
        if (accountCreationDate != null ? !accountCreationDate.equals(that.accountCreationDate) : that.accountCreationDate != null) return false;
        if (accountMaxActivationDate != null ? !accountMaxActivationDate.equals(that.accountMaxActivationDate) : that.accountMaxActivationDate != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (job != null ? !job.equals(that.job) : that.job != null) return false;
        if (recoverCode != null ? !recoverCode.equals(that.recoverCode) : that.recoverCode != null) return false;
        if (recoverCodeCreationDate != null ? !recoverCodeCreationDate.equals(that.recoverCodeCreationDate) : that.recoverCodeCreationDate != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (dateBorn != null ? dateBorn.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (confirmationCode != null ? confirmationCode.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + (confirmed ? 1 : 0);
        result = 31 * result + (job != null ? job.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (recoverCode != null ? recoverCode.hashCode() : 0);
        result = 31 * result + (recoverCodeCreationDate != null ? recoverCodeCreationDate.hashCode() : 0);
        result = 31 * result + (accountCreationDate != null ? accountCreationDate.hashCode() : 0);
        result = 31 * result + (accountMaxActivationDate != null ? accountMaxActivationDate.hashCode() : 0);
        return result;
    }
}
