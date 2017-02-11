/**
 *
 */
package com.fr.core;

import java.util.List;

import com.fr.entities.Message;
import org.springframework.stereotype.Component;

import com.fr.rest.service.MessageControllerService;

/**
 * Created by: Wail DJENANE on Jun 25, 2016
 */

@Component
public class MessageControllerServiceImpl extends AbstractControllerServiceImpl implements MessageControllerService {


    @Override
    public List<Message> getSentUserMessages(Long userId, int page) {
        return null;
    }

    @Override
    public List<Message> getReceivedUserMessages(Long userId, int page) {
        return null;
    }

    @Override
    public Boolean saveMessage(Message msg) {
        return null;
    }

    @Override
    public Boolean updateMessage(Message msg) {
        return null;
    }

    @Override
    public Boolean deteleMessageById(Long msgId) {
        return null;
    }

    @Override
    public Message findMessageById(Long msgId) {
        return null;
    }
}
