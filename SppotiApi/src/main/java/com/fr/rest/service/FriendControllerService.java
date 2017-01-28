package com.fr.rest.service;

import com.fr.entities.FriendShip;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */
@Service
public interface FriendControllerService extends AbstractControllerService{
    List<FriendShip> getByUserAndStatus(int uuid, String name, Pageable pageable);

    List<FriendShip> getByFriendUuidAndStatus(int uuid, String name, Pageable pageable);

    FriendShip getByFriendUuidAndUser(int uuid, int uuid1);

    void saveFriendShip(FriendShip friendShip);

    void updateFriendShip(FriendShip friendShip);

    void deleteFriendShip(FriendShip friendShip);
}
