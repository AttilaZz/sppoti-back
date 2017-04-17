package com.fr.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by djenanewail on 12/18/16.
 */

@Entity
@Table(name = "ADDRESS")
public class AddressEntity
        extends AbstractCommonEntity
        implements Comparable<AddressEntity> {

    private String address;
    private String dateTime = new DateTime().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition = "post_id")
    @JsonIgnore
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition = "userid")
    @JsonIgnore
    private UserEntity users;

    public AddressEntity() {
    }

    public AddressEntity(String address) {
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

    public UserEntity getUsers() {
        return users;
    }

    public void setUsers(UserEntity users) {
        this.users = users;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public int compareTo(AddressEntity o) {
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

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressEntity)) return false;
        if (!super.equals(o)) return false;

        AddressEntity that = (AddressEntity) o;

        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        return dateTime != null ? dateTime.equals(that.dateTime) : that.dateTime == null;

    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        return result;
    }
}
