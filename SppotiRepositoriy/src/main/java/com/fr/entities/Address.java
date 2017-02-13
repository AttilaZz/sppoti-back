package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by djenanewail on 12/18/16.
 */

@Entity
public class Address implements Comparable<Address> {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private int uuid = UUID.randomUUID().hashCode();

    private String address;
    private String dateTime = new DateTime().toString();

    @ManyToOne
    @JoinColumn(columnDefinition = "post_id")
    @JsonIgnore
    private PostEntity post;

    @ManyToOne
    @JoinColumn(columnDefinition = "userid")
    @JsonIgnore
    private UserEntity users;

    public Address() {
    }

    public Address(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
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

    public UserEntity getUsers() {
        return users;
    }

    public void setUsers(UserEntity users) {
        this.users = users;
    }

    @Override
    public int compareTo(Address o) {
        if (this != null) {
            if (o != null) {
                return this.dateTime.compareTo(o.dateTime);
            } else {
                return 1;
            }
        }

        if (o != null)
            return -1;

        return 0;
    }
}
