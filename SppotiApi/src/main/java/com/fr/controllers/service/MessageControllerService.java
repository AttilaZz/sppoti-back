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

     List<Messages> getSentUserMessages(Long userId, int page);

     List<Messages> getReceivedUserMessages(Long userId, int page);

     Boolean saveMessage(Messages msg);

     Boolean updateMessage(Messages msg);

     Boolean deteleMessageById(Long msgId);

     Messages findMessageById(Long msgId);

}
