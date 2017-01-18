/**
 *
 */
package com.fr.controllers.service.implem;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fr.controllers.service.MessageControllerService;
import com.fr.entities.Messages;

/**
 * Created by: Wail DJENANE on Jun 25, 2016
 */

@Component
public class MessageControllerServiceImpl extends AbstractControllerServiceImpl implements MessageControllerService {


    @Override
    public List<Messages> getSentUserMessages(Long userId, int page) {
        return null;
    }

    @Override
    public List<Messages> getReceivedUserMessages(Long userId, int page) {
        return null;
    }

    @Override
    public Boolean saveMessage(Messages msg) {
        return null;
    }

    @Override
    public Boolean updateMessage(Messages msg) {
        return null;
    }

    @Override
    public Boolean deteleMessageById(Long msgId) {
        return null;
    }

    @Override
    public Messages findMessageById(Long msgId) {
        return null;
    }
}
