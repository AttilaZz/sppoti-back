package com.fr.transformers.impl;

import com.fr.commons.dto.message.MessageDTO;
import com.fr.commons.utils.SppotiBeanUtils;
import com.fr.entities.MessageEntity;
import com.fr.transformers.MessageTranformer;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 5/25/17.
 */
@Component
public class MessageTransformerImpl extends AbstractTransformerImpl<MessageDTO, MessageEntity> implements
		MessageTranformer
{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageEntity dtoToModel(final MessageDTO dto)
	{
		final MessageEntity entity = new MessageEntity();
		SppotiBeanUtils.copyProperties(entity, dto);
		return entity;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MessageDTO modelToDto(final MessageEntity model)
	{
		final MessageDTO dto = new MessageDTO();
		SppotiBeanUtils.copyProperties(dto, model);
		return dto;
	}
}
