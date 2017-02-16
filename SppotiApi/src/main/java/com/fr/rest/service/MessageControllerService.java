/**
 *
 */
package com.fr.rest.service;

import java.util.List;

import com.fr.entities.MessageEntity;
import org.springframework.stereotype.Service;

/**
 * Created by: Wail DJENANE on Jun 25, 2016
 */

@Service
 public interface MessageControllerService extends AbstractControllerService {

     List<MessageEntity> getSentUserMessages(Long userId, int page);

     List<MessageEntity> getReceivedUserMessages(Long userId, int page);

     Boolean saveMessage(MessageEntity msg);

     Boolean updateMessage(MessageEntity msg);

     Boolean deteleMessageById(Long msgId);

     MessageEntity findMessageById(Long msgId);

}
