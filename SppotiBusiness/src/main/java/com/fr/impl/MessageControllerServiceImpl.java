/**
 *
 */
package com.fr.impl;

import java.util.List;

import com.fr.entities.MessageEntity;
import org.springframework.stereotype.Component;

import com.fr.service.MessageControllerService;

/**
 * Created by: Wail DJENANE on Jun 25, 2016
 */

@Component
class MessageControllerServiceImpl extends AbstractControllerServiceImpl implements MessageControllerService
{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MessageEntity> getSentUserMessages(Long userId,int page)
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MessageEntity> getReceivedUserMessages(Long userId,int page)
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean saveMessage(MessageEntity msg)
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean updateMessage(MessageEntity msg)
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean deteleMessageById(Long msgId)
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageEntity findMessageById(Long msgId)
	{
		return null;
	}
}
