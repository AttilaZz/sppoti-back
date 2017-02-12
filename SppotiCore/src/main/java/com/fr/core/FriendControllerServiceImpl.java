package com.fr.core;

import com.fr.entities.FriendShip;
import com.fr.entities.NotificationEntity;
import com.fr.entities.UserEntity;
import com.fr.models.GlobalAppStatus;
import com.fr.models.NotificationType;
import com.fr.rest.service.FriendControllerService;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
public class FriendControllerServiceImpl extends AbstractControllerServiceImpl implements FriendControllerService {

    private Logger LOGGER = Logger.getLogger(FriendControllerServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FriendShip> getByUserAndStatus(int uuid, String name, Pageable pageable) {
        return friendShipRepository.findByUserUuidAndStatusAndDeletedFalse(uuid, name, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FriendShip> getByFriendUuidAndStatus(int uuid, String name, Pageable pageable) {
        return friendShipRepository.findByFriendUuidAndStatusAndDeletedFalse(uuid, name, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FriendShip getByFriendUuidAndUser(int uuid, int uuid1) {
        return friendShipRepository.findByFriendUuidAndUserUuidAndDeletedFalse(uuid, uuid1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveFriendShip(FriendShip friendShip) {

        if (friendShipRepository.save(friendShip) != null) {
            addFriendNotification(NotificationType.FRIEND_REQUEST_SENT, friendShip.getUser(), friendShip.getFriend());
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateFriendShip(Long userId, int friendUuid, int friendStatus) {

         /*
        Prepare friendShip
         */
        UserEntity connectedUser = getUserById(userId);

         /*
        Check if i received a friend request from the USER in the request
         */
        FriendShip tempFriendShip = getByFriendUuidAndUser(connectedUser.getUuid(), friendUuid);
        if (tempFriendShip == null) {
            LOGGER.error("UPDATE-FRIEND: FriendShip not found !");
            throw new EntityNotFoundException("FriendShip not found between (" + connectedUser.getUuid() + ") And (" + friendUuid + ")");
        }

        /*
        prepare update
         */
        for (GlobalAppStatus globalAppStatus : GlobalAppStatus.values()) {
            if (globalAppStatus.getValue() == friendStatus) {
                tempFriendShip.setStatus(globalAppStatus.name());
            }
        }

        //update and add notification
        FriendShip friendShip = friendShipRepository.save(tempFriendShip);
        if (friendShip != null) {
            if(friendShip.getStatus().equals(GlobalAppStatus.CONFIRMED.name())){
                addFriendNotification(NotificationType.FRIEND_REQUEST_ACCEPTED, friendShip.getUser(), friendShip.getFriend());
            }else if(friendShip.getStatus().equals(GlobalAppStatus.REFUSED.name())){
                addFriendNotification(NotificationType.FRIEND_REQUEST_REFUSED, friendShip.getUser(), friendShip.getFriend());
            }
        }

    }

    /**
     * Add notification
     *
     * @param friendRequestRefused
     * @param user
     * @param friend
     */
    private void addFriendNotification(NotificationType friendRequestRefused, UserEntity user, UserEntity friend) {
        NotificationEntity notification = new NotificationEntity();
        notification.setNotificationType(friendRequestRefused);
        notification.setFrom(user);
        notification.setTo(friend);
        notificationRepository.save(notification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFriendShip(FriendShip friendShip) {

        friendShip.setDeleted(true);
        friendShipRepository.save(friendShip);

    }
}
