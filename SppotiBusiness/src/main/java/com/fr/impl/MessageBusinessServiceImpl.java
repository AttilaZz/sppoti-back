/**
 *
 */
package com.fr.impl;

import com.fr.entities.MessageEntity;
import com.fr.service.MessageBusinessService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by: Wail DJENANE on Jun 25, 2016
 */

@Component
class MessageBusinessServiceImpl extends AbstractControllerServiceImpl implements MessageBusinessService
{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MessageEntity> getSentUserMessages(final Long userId, final int page)
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MessageEntity> getReceivedUserMessages(final Long userId, final int page)
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean saveMessage(final MessageEntity msg)
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean updateMessage(final MessageEntity msg)
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean deteleMessageById(final Long msgId)
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageEntity findMessageById(final Long msgId)
	{
		return null;
	}
}
