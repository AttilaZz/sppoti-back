package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fr.models.FriendStatus;

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

    public Friend(Users user) {
        super(user);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    private String status = FriendStatus.PENDING.name();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "friends")
    @JsonIgnore
    private Set<Users> users;

    public Set<Users> getUsers() {
        return users;
    }

    public void setUsers(Set<Users> users) {
        this.users = users;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
