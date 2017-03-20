package com.fr.rest.service;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.FriendShipEntity;
import com.fr.models.GlobalAppStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */
@Service
public interface FriendControllerService extends AbstractControllerService {

    /**
     * Fiend a all friends matching the given status
     *
     * @param uuid
     * @param name
     * @param pageable
     * @return List FriendShipEntity DTO
     */
    List<FriendShipEntity> getByUserAndStatus(int uuid, GlobalAppStatus name, Pageable pageable);

    /**
     * Fiend a all friends matching the given status
     *
     * @param uuid
     * @param name
     * @param pageable
     * @return List of Friendship DTO
     */
    List<FriendShipEntity> getByFriendUuidAndStatus(int uuid, GlobalAppStatus name, Pageable pageable);

    /**
     * Fiend friendship between two users
     *
     * @param uuid
     * @param uuid1
     * @return Friendship DTO
     */
    FriendShipEntity getByFriendUuidAndUser(int uuid, int uuid1);

    /**
     * Save the friendship and send notification
     *
     * @param friendShip friendship.
     */
    void saveFriendShip(FriendShipEntity friendShip);

    /**
     * Update status (ACCEPT/REFUSE) friend request and send notification
     *
     * @param userId user id.
     * @param friendUuid friend id.
     * @param friendStatus friend status.
     */
    void updateFriendShip(Long userId, int friendUuid, int friendStatus);

    /**
     * Delete friendship by the sender
     *
     * @param friendShip friendship.
     */
    void deleteFriendShip(FriendShipEntity friendShip);

    /**
     *
     * @return friendship between two users.
     */
    FriendShipEntity findFriendShip(int user1, int user2);

    /**
     *
     * @param userId user id.
     * @return all confirmed friend requests.
     */
    List<UserDTO> getConfirmedFriendList(int userId, int page);
}
