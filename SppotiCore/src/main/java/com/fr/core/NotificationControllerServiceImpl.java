package com.fr.core;

import com.fr.commons.dto.NotificationDTO;
import com.fr.entities.NotificationEntity;
import com.fr.rest.service.NotificationControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import utils.EntitytoDtoTransformer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 2/11/17.
 */
@Component
public class NotificationControllerServiceImpl extends AbstractControllerServiceImpl implements NotificationControllerService {

    private Logger LOGGER = Logger.getLogger(NotificationControllerServiceImpl.class);

    @Value("${key.notificationsPerPage}")
    private int notificationSize;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotificationDTO> getAllReceivedNotifications(int userId, int page) {

        Pageable pageable = new PageRequest(page, notificationSize);

        List<NotificationEntity> notifications = notificationRepository.findByToUuidAndOpenedFalse(userId, pageable);

        return notifications.stream()
                .map(EntitytoDtoTransformer::notificationEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotificationDTO> getAllSentNotifications(int userId, int page) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openNotification(int notifId) {
        notificationRepository.findByUuid(notifId);
    }
}
