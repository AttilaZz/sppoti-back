package com.fr.entities;

import javax.persistence.*;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by: Wail DJENANE on May 22, 2016
 */
@Entity
@Table(name = "MESSAGE")
@JsonInclude(Include.NON_EMPTY)
public class MessageEntity
        extends AbstractCommonEntity {
    @Column(nullable = false)
    private String datetime = new DateTime().toString();
    @Column(nullable = false)
    private String object;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    @JsonIgnore
    private Long receiver_id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    // @JsonBackReference(value = "user_messages")
    @JsonIgnore
    private UserEntity userMessage;

    public MessageEntity() {
        super();
    }

    public MessageEntity(MessageEntity msg) {
        this.content = msg.getContent();
        this.datetime = msg.getDatetime();
        this.receiver_id = msg.getReceiver_id();
        this.object = msg.getObject();
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

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MessageEntity that = (MessageEntity) o;

        if (datetime != null ? !datetime.equals(that.datetime) : that.datetime != null) return false;
        if (object != null ? !object.equals(that.object) : that.object != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        return receiver_id != null ? receiver_id.equals(that.receiver_id) : that.receiver_id == null;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (datetime != null ? datetime.hashCode() : 0);
        result = 31 * result + (object != null ? object.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (receiver_id != null ? receiver_id.hashCode() : 0);
        return result;
    }
}
