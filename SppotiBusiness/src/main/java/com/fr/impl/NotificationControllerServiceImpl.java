package com.fr.impl;

import com.fr.commons.dto.notification.NotificationDTO;
import com.fr.commons.dto.notification.NotificationResponseDTO;
import com.fr.commons.exception.NotAdminException;
import com.fr.entities.NotificationEntity;
import com.fr.service.NotificationControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import com.fr.transformers.NotificationTransformer;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
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
    public NotificationResponseDTO getAllReceivedNotifications(int userId, int page) {

        Pageable pageable = new PageRequest(page, notificationSize);

        List<NotificationEntity> notifications = notificationRepository.findByToUuid(userId, pageable);

        NotificationResponseDTO notificationResponseDTO = new NotificationResponseDTO();
        notificationResponseDTO.setNotifications(notifications.stream()
                .map(NotificationTransformer::notificationEntityToDto)
                .collect(Collectors.toList()));
        notificationResponseDTO.setNotifCounter(notificationRepository.countByToUuid(userId));

        return notificationResponseDTO;
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
    @Transactional
    @Override
    public void openNotification(int notifId, Long connectedUserId) {
        Optional<NotificationEntity> notification = Optional.ofNullable(notificationRepository.findByUuid(notifId));

        notification.ifPresent(
                n -> {
                    if(!n.getTo().getId().equals(connectedUserId)){
                        throw new NotAdminException("ACCESS DENIED TO THIS SERVICE");
                    }

                    n.setOpened(true);
                    notificationRepository.save(n);
                }
        );

        notification.orElseThrow(() -> new EntityNotFoundException("Notification not Found"));

    }
}