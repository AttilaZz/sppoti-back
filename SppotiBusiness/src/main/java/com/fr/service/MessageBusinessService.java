/**
 *
 */
package com.fr.service;

import com.fr.entities.MessageEntity;

import java.util.List;

/**
 * Created by: Wail DJENANE on Jun 25, 2016
 */

public interface MessageBusinessService extends AbstractBusinessService
{
	
	List<MessageEntity> getSentUserMessages(Long userId, int page);
	
	List<MessageEntity> getReceivedUserMessages(Long userId, int page);
	
	Boolean saveMessage(MessageEntity msg);
	
	Boolean updateMessage(MessageEntity msg);
	
	Boolean deteleMessageById(Long msgId);
	
	MessageEntity findMessageById(Long msgId);
	
}
