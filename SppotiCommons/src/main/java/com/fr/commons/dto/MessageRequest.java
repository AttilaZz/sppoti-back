package com.fr.commons.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fr.entities.Message;
import com.fr.entities.Users;

/**
 * Created by: Wail DJENANE on Jun 25, 2016
 */

@JsonInclude(Include.NON_EMPTY)
public class MessageRequest {

    private Long userId;
    private Long receivedId;

    private Message msg;

    private Users sender;
    private Users receiver;

    private List<Message> sentMessages;
    private List<Message> receivedMessages;

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

    public Message getMsg() {
        return msg;
    }

    public void setMsg(Message msg) {
        this.msg = msg;
    }

    public Users getSender() {
        return sender;
    }

    public void setSender(Users sender) {
        this.sender = sender;
    }

    public Users getReceiver() {
        return receiver;
    }

    public void setReceiver(Users receiver) {
        this.receiver = receiver;
    }

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

}
