package com.fr.impl;

import com.fr.commons.dto.notification.NotificationDTO;
import com.fr.commons.dto.notification.NotificationListDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.exception.NotAdminException;
import com.fr.entities.NotificationEntity;
import com.fr.service.NotificationControllerService;
import com.fr.transformers.impl.NotificationTransformerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 2/11/17.
 */
@Component
class NotificationControllerServiceImpl extends AbstractControllerServiceImpl implements NotificationControllerService
{
	
	/** Notification list size. */
	@Value("${key.notificationsPerPage}")
	private int notificationSize;
	
	/** Notification transformer. */
	@Autowired
	private NotificationTransformerImpl notificationTransformer;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NotificationListDTO getAllReceivedNotifications(final int userId, final int page)
	{
		
		final Pageable pageable = new PageRequest(page, this.notificationSize, Sort.Direction.DESC, "creationDate");
		
		List<NotificationEntity> notifications = this.notificationRepository.findByToUuid(userId, pageable);
		
		notifications = notifications.stream().map(n -> {
			NotificationEntity notif = getNotificationEntity(n.getNotificationType(), n.getFrom(), n.getTo(),
					n.getTeam(), n.getSppoti(), n.getPost(), n.getComment());
			
			notif.setId(n.getId());
			notif.setUuid(n.getUuid());
			notif.setVersion(n.getVersion());
			notif.setNotificationType(n.getNotificationType());
			notif.setStatus(n.getStatus());
			return notif;
		}).collect(Collectors.toList());
		
		final NotificationListDTO notificationListDTO = new NotificationListDTO();
		
		notificationListDTO.setNotifications(
				notifications.stream().map(this.notificationTransformer::modelToDto).collect(Collectors.toList()));
		
		notificationListDTO.setNotifCounter(this.notificationRepository.countByToUuid(userId));
		
		notificationListDTO.setUnreadCounter(
				this.notificationRepository.countByToUuidAndStatus(userId, GlobalAppStatusEnum.UNREAD));
		
		return notificationListDTO;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<NotificationDTO> getAllSentNotifications(final int userId, final int page)
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void switchNotificationStatus(final NotificationDTO notificationDTO)
	{
		final Optional<NotificationEntity> notification = Optional
				.ofNullable(this.notificationRepository.findByUuid(notificationDTO.getId()));
		
		notification.ifPresent(n -> {
			if (!n.getTo().getId().equals(getConnectedUser().getId())) {
				throw new NotAdminException("This is not your notification");
			}
			
			n.setStatus(notificationDTO.getStatus());
			this.notificationRepository.save(n);
		});
		
		notification.orElseThrow(() -> new EntityNotFoundException("Notification not Found"));
		
	}
}
