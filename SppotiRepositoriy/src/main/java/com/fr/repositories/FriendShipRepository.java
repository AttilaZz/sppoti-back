package com.fr.repositories;

import com.fr.entities.FriendShip;
import com.fr.entities.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import java.util.List;

/**
 * Created by djenanewail on 12/31/16.
 */
public interface FriendShipRepository extends CrudRepository<FriendShip, Long> {

    FriendShip getByFriendUuidAndUser(int friend, int uuid);

    List<FriendShip> getByUserAndStatus(int uuid, String name, Pageable pageable);

    List<FriendShip> getByFriendUuidAndStatus(int friend, String name, Pageable pageable);

    List<FriendShip>  getByUserAndFriendUuidAndStatus(int connected_user, int uuid, String name);


    //    @PostFilter("!filterObject.isDeleted() AND filterObject.isConfirmed()")
    @Query("SELECT f FROM FriendShip f WHERE (f.friend.firstName LIKE CONCAT('%',:part1,'%') AND f.friend.lastName LIKE CONCAT('%',:part2,'%')) OR (f.friend.firstName LIKE CONCAT('%',:part2,'%') AND f.friend.lastName LIKE CONCAT('%',:part1,'%')) AND f.status = :status")
    List<FriendShip> findFriendsByFirstNameAndLastNameAndStatus(@Param("part1") String part1, @Param("part2") String part2, @Param("status") String status, Pageable pageable);

    @Query("SELECT f FROM FriendShip f WHERE f.friend.username LIKE CONCAT('%',:prefix,'%') AND f.status = :status")
    List<FriendShip> findFriendByUsernameAndStatus(@Param("prefix") String part, @Param("status") String status, Pageable pageable);


}