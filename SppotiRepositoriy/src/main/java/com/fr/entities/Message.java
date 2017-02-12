package com.fr.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by: Wail DJENANE on May 22, 2016
 */
@Entity
// @JsonIgnoreProperties( { "hibernateLazyInitializer", "handler" } )
@JsonInclude(Include.NON_EMPTY)
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String datetime = new DateTime().toString();
    @Column(nullable = false)
    private String object;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    @JsonIgnore
    private Long receiver_id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "sender_id")
    // @JsonBackReference(value = "user_messages")
    @JsonIgnore
    private UserEntity userMessage;

    public Message() {
        super();
    }

    public Message(Message msg) {
        this.content = msg.getContent();
        this.datetime = msg.getDatetime();
        this.receiver_id = msg.getReceiver_id();
        this.object = msg.getObject();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(Long receiver_id) {
        this.receiver_id = receiver_id;
    }

    public UserEntity getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(UserEntity userMessage) {
        this.userMessage = userMessage;
    }

}
