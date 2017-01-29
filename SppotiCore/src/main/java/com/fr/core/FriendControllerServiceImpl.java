package com.fr.core;

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
        return friendShipRepository.findByUserAndStatusAndDeletedFalse(uuid, name, pageable);
    }

    @Override
    public List<FriendShip> getByFriendUuidAndStatus(int uuid, String name, Pageable pageable) {
        return friendShipRepository.findByFriendUuidAndStatusAndDeletedFalse(uuid, name, pageable);
    }

    @Override
    public FriendShip getByFriendUuidAndUser(int uuid, int uuid1) {
        return friendShipRepository.findByFriendUuidAndUserAndDeletedFalse(uuid, uuid1);
    }

    @Override
    public void saveFriendShip(FriendShip friendShip) {

        friendShipRepository.save(friendShip);

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
