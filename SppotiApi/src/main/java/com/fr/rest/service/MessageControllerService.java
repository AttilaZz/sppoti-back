/**
 *
 */
package com.fr.rest.service;

import java.util.List;

import com.fr.entities.Message;
import org.springframework.stereotype.Service;

/**
 * Created by: Wail DJENANE on Jun 25, 2016
 */

@Service
 public interface MessageControllerService extends AbstractControllerService {

     List<Message> getSentUserMessages(Long userId, int page);

     List<Message> getReceivedUserMessages(Long userId, int page);

     Boolean saveMessage(Message msg);

     Boolean updateMessage(Message msg);

     Boolean deteleMessageById(Long msgId);

     Message findMessageById(Long msgId);

}
