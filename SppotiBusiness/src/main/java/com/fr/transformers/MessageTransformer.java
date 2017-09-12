package com.fr.transformers;

import com.fr.commons.dto.message.MessageDTO;
import com.fr.entities.MessageEntity;
import org.springframework.stereotype.Service;

/**
 * Created by djenanewail on 5/25/17.
 */
public interface MessageTransformer extends CommonTransformer<MessageDTO, MessageEntity>
{
}
