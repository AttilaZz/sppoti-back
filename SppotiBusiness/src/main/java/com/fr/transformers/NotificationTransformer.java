package com.fr.transformers;

import com.fr.commons.dto.notification.NotificationDTO;
import com.fr.entities.NotificationEntity;
import org.springframework.stereotype.Service;

/**
 * Notifications transformer.
 *
 * Created by djenanewail on 5/27/17.
 */
public interface NotificationTransformer extends CommonTransformer<NotificationDTO, NotificationEntity>
{
}
