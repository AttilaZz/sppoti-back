/**
 *
 */
package com.fr.controllers.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.entities.Messages;

/**
 * Created by: Wail DJENANE on Jun 25, 2016
 */

@Service
public interface MessageControllerService extends AbstractControllerService {

    public List<Messages> getSentUserMessages(Long userId, int page);

    public List<Messages> getReceivedUserMessages(Long userId, int page);

    public Boolean saveMessage(Messages msg);

    public Boolean updateMessage(Messages msg);

    public Boolean deteleMessageById(Long msgId);

    public Messages findMessageById(Long msgId);

}
