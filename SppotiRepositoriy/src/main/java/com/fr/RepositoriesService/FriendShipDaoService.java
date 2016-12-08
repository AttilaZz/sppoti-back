package com.fr.RepositoriesService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fr.entities.FriendShip;

/**
 * Created by: Wail DJENANE on Jul 3, 2016
 */
@Service
public interface FriendShipDaoService extends GenericDaoService<FriendShip, Integer> {

    int getPendingFriendRequestCount(Long userId);

    int getConfirmedFriendRequestCount(Long userId);

    List<FriendShip> getPendingFriendList(Long userId, int bottomId);

    List<FriendShip> getConfirmedFriendList(Long userId, int bottomId);

    /**
     * @param userId
     * @param friendId
     * @return
     */
    FriendShip getConfirmedFriendShip(Long userId, Long friendId);

    /**
     * @param userId
     * @param friendId
     * @return
     */
    FriendShip getPendingFriendShip(Long userId, Long friendId);

    /**
     * @param userId
     * @param friendId
     * @return
     */
    FriendShip getFriendShip(Long userId, Long friendId);

    /**
     * @param friendPrefix
     * @param page
     * @return
     */
    List<FriendShip> getFriendsFromPrefix(Long userId, String friendPrefix, int page);

}
