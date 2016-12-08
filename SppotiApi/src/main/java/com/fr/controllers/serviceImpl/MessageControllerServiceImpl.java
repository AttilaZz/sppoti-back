/**
 * 
 */
package com.fr.controllers.serviceImpl;

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
		return messageDaoService.getAllSentMessagesByUserId(userId, page);
	}

	@Override
	public List<Messages> getReceivedUserMessages(Long userId, int page) {
		return messageDaoService.getAllReceivedtMessagesByUserId(userId, page);
	}

	@Override
	public Boolean saveMessage(Messages msg) {
		return messageDaoService.saveOrUpdate(msg);
	}

	@Override
	public Boolean updateMessage(Messages msg) {
		return messageDaoService.update(msg);
	}

	@Override
	public Boolean deteleMessageById(Long msgId) {
		Messages m = messageDaoService.getMessageById(msgId);
		if (m != null) {
			return messageDaoService.delete(m);
		}
		return false;
	}

	@Override
	public Messages findMessageById(Long msgId) {
		return messageDaoService.getMessageById(msgId);
	}

}
