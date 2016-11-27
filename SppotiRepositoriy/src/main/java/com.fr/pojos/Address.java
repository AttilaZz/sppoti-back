package com.fr.pojos;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Entity
// @JsonIgnoreProperties( { "hibernateLazyInitializer", "handler" } )
@JsonInclude(Include.NON_EMPTY)
public class Address implements Comparable<Address> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = true)
    private Integer number;
    @Column(nullable = true)
    private String type; // rue avenue
    @Column(nullable = true)
    private String streetName;
    @Column(nullable = true)
    private Integer zipCode;
    @Column(nullable = true)
    private String townName;
    @Column(nullable = true)
    private String Country;
    @Column(nullable = true)
    private double latitude;
    @Column(nullable = true)
    private double longitude;
    @Column(nullable = false)
    private String datetimeCreated = new DateTime().toString();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "userAddress", cascade = CascadeType.ALL)
    private Set<Users> users;

    @OneToOne(mappedBy = "gameAddress", cascade = CascadeType.ALL)
    @JsonIgnore
    private Sppoti game;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    public Address() {
    }

    public Address(Integer number, String type, String streetName, Integer zipCode, String townName) {
        super();
        this.number = number;
        this.type = type;
        this.streetName = streetName;
        this.zipCode = zipCode;
        this.townName = townName;
    }

    public Long getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public String getStreetName() {
        return streetName;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public String getTownName() {
        return townName;
    }

    public Sppoti getGame() {
        return game;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public void setGame(Sppoti game) {
        this.game = game;
    }

    public Set<Users> getUser() {
        return users;
    }

    public void setUser(Set<Users> users) {
        this.users = users;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double d) {
        this.latitude = d;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(String datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    @SuppressWarnings("unused")
    @Override
    public int compareTo(Address o) {
        if (this != null) {
            if (o != null) {
                return this.datetimeCreated.compareTo(o.datetimeCreated);
            } else {
                return 1;
            }
        }

        if (o != null)
            return -1;

        return 0;
    }

}
