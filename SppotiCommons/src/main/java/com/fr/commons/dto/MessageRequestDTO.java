package com.fr.commons.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.entities.MessageEntity;
import com.fr.entities.UserEntity;

/**
 * Created by: Wail DJENANE on Jun 25, 2016
 */

@JsonInclude(Include.NON_EMPTY)
public class MessageRequestDTO {

    private Long userId;
    private Long receivedId;

    private MessageEntity msg;

    private UserEntity sender;
    private UserEntity receiver;

    private List<MessageEntity> sentMessages;
    private List<MessageEntity> receivedMessages;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getReceivedId() {
        return receivedId;
    }

    public void setReceivedId(Long receivedId) {
        this.receivedId = receivedId;
    }

    public MessageEntity getMsg() {
        return msg;
    }

    public void setMsg(MessageEntity msg) {
        this.msg = msg;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    public List<MessageEntity> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<MessageEntity> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public List<MessageEntity> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<MessageEntity> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

}
