package com.fr.impl;

import com.fr.commons.dto.notification.NotificationDTO;
import com.fr.commons.dto.notification.NotificationListDTO;
import com.fr.commons.exception.NotAdminException;
import com.fr.entities.NotificationEntity;
import com.fr.service.NotificationControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import com.fr.transformers.impl.NotificationTransformer;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 2/11/17.
 */
@Component
class NotificationControllerServiceImpl extends AbstractControllerServiceImpl implements NotificationControllerService {

    @Value("${key.notificationsPerPage}")
    private int notificationSize;

    @Autowired
    private NotificationTransformer notificationTransformer;

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationListDTO getAllReceivedNotifications(int userId, int page) {

        Pageable pageable = new PageRequest(page, notificationSize);

        List<NotificationEntity> notifications = notificationRepository.findByToUuid(userId, pageable);

        NotificationListDTO notificationListDTO = new NotificationListDTO();
        notificationListDTO.setNotifications(notifications.stream()
                .map(notificationTransformer::notificationEntityToDto)
                .collect(Collectors.toList()));
        notificationListDTO.setNotifCounter(notificationRepository.countByToUuid(userId));

        return notificationListDTO;
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
                    if (!n.getTo().getId().equals(connectedUserId)) {
                        throw new NotAdminException("ACCESS DENIED TO THIS SERVICE");
                    }

                    n.setOpened(true);
                    notificationRepository.save(n);
                }
        );

        notification.orElseThrow(() -> new EntityNotFoundException("Notification not Found"));

    }
}
