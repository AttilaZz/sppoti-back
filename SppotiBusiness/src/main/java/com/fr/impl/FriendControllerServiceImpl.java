
package com.fr.impl;

import com.fr.commons.dto.SportDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.FriendShipEntity;
import com.fr.entities.UserEntity;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.NotificationTypeEnum;
import com.fr.service.FriendControllerService;
import com.fr.transformers.TeamTransformer;
import com.fr.transformers.UserTransformer;
import com.fr.transformers.impl.SportTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
class FriendControllerServiceImpl extends AbstractControllerServiceImpl implements FriendControllerService {

    /**
     * Class logger.
     */
    private Logger LOGGER = Logger.getLogger(FriendControllerServiceImpl.class);

    /**
     * {@link UserEntity} transformer.
     */
    private final UserTransformer userTransformer;

    /**
     * Friend list size.
     */
    @Value("${key.friendShipPerPage}")
    private int friendListSize;

    /**
     * Init services;
     */
    @Autowired
    public FriendControllerServiceImpl(UserTransformer userTransformer) {
        this.userTransformer = userTransformer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FriendShipEntity> getByUserAndStatus(int uuid, GlobalAppStatusEnum name, Pageable pageable) {
        return friendShipRepository.findByUserUuidAndStatusAndDeletedFalse(uuid, name, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FriendShipEntity> getByFriendUuidAndStatus(int uuid, GlobalAppStatusEnum name, Pageable pageable) {
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
            addNotification(NotificationTypeEnum.FRIEND_REQUEST_SENT, friendShip.getUser(), friendShip.getFriend(), null, null);
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
        for (GlobalAppStatusEnum globalAppStatus : GlobalAppStatusEnum.values()) {
            if (globalAppStatus.getValue() == friendStatus) {
                tempFriendShip.setStatus(globalAppStatus);
            }
        }

        //update and add notification
        FriendShipEntity friendShip = friendShipRepository.save(tempFriendShip);
        if (friendShip != null) {
            if (friendShip.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)) {
                addNotification(NotificationTypeEnum.FRIEND_REQUEST_ACCEPTED, friendShip.getFriend(), friendShip.getUser(), null, null);
            } else if (friendShip.getStatus().equals(GlobalAppStatusEnum.REFUSED)) {
                addNotification(NotificationTypeEnum.FRIEND_REQUEST_REFUSED, friendShip.getFriend(), friendShip.getUser(), null, null);
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

        Pageable pageable = new PageRequest(page, friendListSize);

        List<FriendShipEntity> friendShips = this.getByUserAndStatus(userId, GlobalAppStatusEnum.CONFIRMED, pageable);

        return friendShips.stream()
                .map(f -> this.userTransformer.modelToDto(f.getFriend()))
                .collect(Collectors.toList());

    }
}