package com.fr.core;

import com.fr.entities.NotificationEntity;
import com.fr.models.NotificationType;
import com.fr.rest.service.FriendControllerService;
import com.fr.entities.FriendShip;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
public class FriendControllerServiceImpl extends AbstractControllerServiceImpl implements FriendControllerService {

    @Override
    public List<FriendShip> getByUserAndStatus(int uuid, String name, Pageable pageable) {
        return friendShipRepository.findByUserUuidAndStatusAndDeletedFalse(uuid, name, pageable);
    }

    @Override
    public List<FriendShip> getByFriendUuidAndStatus(int uuid, String name, Pageable pageable) {
        return friendShipRepository.findByFriendUuidAndStatusAndDeletedFalse(uuid, name, pageable);
    }

    @Override
    public FriendShip getByFriendUuidAndUser(int uuid, int uuid1) {
        return friendShipRepository.findByFriendUuidAndUserUuidAndDeletedFalse(uuid, uuid1);
    }

    @Override
    public void saveFriendShip(FriendShip friendShip) {

        if (friendShipRepository.save(friendShip) != null) {
            NotificationEntity notification = new NotificationEntity();
            notification.setNotificationType(NotificationType.FRIEND_REQUEST_SENT);
            notification.setFrom(friendShip.getUser());
            notification.setTo(friendShip.getFriend());
            notificationRepository.save(notification);
        }

    }

    @Override
    public void updateFriendShip(FriendShip friendShip) {

        friendShipRepository.save(friendShip);

    }

    @Override
    public void deleteFriendShip(FriendShip friendShip) {

        friendShip.setDeleted(true);
        friendShipRepository.save(friendShip);

    }
}
