package com.fr.repositories;

import com.fr.entities.FriendShip;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


import java.util.List;

/**
 * Created by djenanewail on 12/31/16.
 */
public interface FriendShipRepository extends CrudRepository<FriendShip, Long> {

    FriendShip getByFriendAndUser(int friendUuid, int uuid);

    List<FriendShip> getByUserAndStatus(int uuid, String name, Pageable pageable);

    List<FriendShip> getByFriendAndStatus(int uuid, String name, Pageable pageable);
}
