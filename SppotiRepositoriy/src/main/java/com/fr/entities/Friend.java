package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by djenanewail on 12/13/16.
 */

@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Friend extends Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;

    private Long matchingUserId;

    public Friend(Users user) {
        super(user);
        this.matchingUserId = user.getId();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "friends")
    @JsonIgnore
    private Set<Users> users;

    public Set<Users> getUsers() {
        return users;
    }

    public void setUsers(Set<Users> users) {
        this.users = users;
    }

    public Long getMatchingUserId() {
        return matchingUserId;
    }

    public void setMatchingUserId(Long matchingUserId) {
        this.matchingUserId = matchingUserId;
    }
}
