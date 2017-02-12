package com.fr.rest.service;

import com.fr.entities.FriendShip;
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
     * @return List FriendShip DTO
     */
    List<FriendShip> getByUserAndStatus(int uuid, String name, Pageable pageable);

    /**
     * Fiend a all friends matching the given status
     *
     * @param uuid
     * @param name
     * @param pageable
     * @return List of Friendship DTO
     */
    List<FriendShip> getByFriendUuidAndStatus(int uuid, String name, Pageable pageable);

    /**
     * Fiend friendship between two users
     *
     * @param uuid
     * @param uuid1
     * @return Friendship DTO
     */
    FriendShip getByFriendUuidAndUser(int uuid, int uuid1);

    /**
     * Save the friendship and send notification
     *
     * @param friendShip
     */
    void saveFriendShip(FriendShip friendShip);

    /**
     * Update status (ACCEPT/REFUSE) friend request and send notification
     *
     * @param userId
     * @param friendUuid
     * @param friendStatus
     */
    void updateFriendShip(Long userId, int friendUuid, int friendStatus);

    /**
     * Delete friendship by the sender
     *
     * @param friendShip
     */
    void deleteFriendShip(FriendShip friendShip);
}
