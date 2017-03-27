
package com.fr.impl;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.FriendShipEntity;
import com.fr.entities.UserEntity;
import com.fr.models.GlobalAppStatus;
import com.fr.models.NotificationType;
import com.fr.service.FriendControllerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
public class FriendControllerServiceImpl extends AbstractControllerServiceImpl implements FriendControllerService {

    private Logger LOGGER = Logger.getLogger(FriendControllerServiceImpl.class);

    @Value("${key.friendShipPerPage}")
    private int friend_list_size;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FriendShipEntity> getByUserAndStatus(int uuid, GlobalAppStatus name, Pageable pageable) {
        return friendShipRepository.findByUserUuidAndStatusAndDeletedFalse(uuid, name, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FriendShipEntity> getByFriendUuidAndStatus(int uuid, GlobalAppStatus name, Pageable pageable) {
        return friendShipRepository.findByFriendUuidAndStatusAndDeletedFalse(uuid, name, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FriendShipEntity getByFriendUuidAndUser(int friendId, int userId) {
        return friendShipRepository.findByFriendUuidAndUserUuidAndDeletedFalse(friendId, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void saveFriendShip(FriendShipEntity friendShip) {

        if (friendShipRepository.save(friendShip) != null) {
            addNotification(NotificationType.FRIEND_REQUEST_SENT, friendShip.getUser(), friendShip.getFriend(), null, null);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void updateFriendShip(Long userId, int friendUuid, int friendStatus) {

         /*
        Prepare friendShip
         */
        UserEntity connectedUser = getUserById(userId);

         /*
        Check if i received a friend request from the USER in the request
         */
        FriendShipEntity tempFriendShip = getByFriendUuidAndUser(connectedUser.getUuid(), friendUuid);
        if (tempFriendShip == null) {
            LOGGER.error("UPDATE-FRIEND: FriendShipEntity not found !");
            throw new EntityNotFoundException("FriendShipEntity not found between (" + connectedUser.getUuid() + ") And (" + friendUuid + ")");
        }

        /*
        prepare update
         */
        for (GlobalAppStatus globalAppStatus : GlobalAppStatus.values()) {
            if (globalAppStatus.getValue() == friendStatus) {
                tempFriendShip.setStatus(globalAppStatus);
            }
        }

        //update and add notification
        FriendShipEntity friendShip = friendShipRepository.save(tempFriendShip);
        if (friendShip != null) {
            if (friendShip.getStatus().equals(GlobalAppStatus.CONFIRMED)) {
                addNotification(NotificationType.FRIEND_REQUEST_ACCEPTED, friendShip.getFriend(), friendShip.getUser(), null, null);
            } else if (friendShip.getStatus().equals(GlobalAppStatus.REFUSED)) {
                addNotification(NotificationType.FRIEND_REQUEST_REFUSED, friendShip.getFriend(), friendShip.getUser(), null, null);
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deleteFriendShip(FriendShipEntity friendShip) {

        friendShip.setDeleted(true);
        friendShipRepository.save(friendShip);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FriendShipEntity findFriendShip(int user1, int user2) {
        return friendShipRepository.findFriendShip(user1, user2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserDTO> getConfirmedFriendList(int userId, int page) {

        Pageable pageable = new PageRequest(page, friend_list_size);

        List<FriendShipEntity> friendShips = this.getByUserAndStatus(userId, GlobalAppStatus.CONFIRMED, pageable);

        return  friendShips.stream()
                .map( f -> this.fillUserResponse(f.getFriend(), null))
                .collect(Collectors.toList());

    }
}