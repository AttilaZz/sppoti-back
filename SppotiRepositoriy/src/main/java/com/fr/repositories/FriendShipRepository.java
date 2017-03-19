package com.fr.repositories;

import com.fr.entities.FriendShipEntity;
import com.fr.models.GlobalAppStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by djenanewail on 12/31/16.
 */
public interface FriendShipRepository extends CrudRepository<FriendShipEntity, Long> {

    FriendShipEntity findByFriendUuidAndUserUuidAndDeletedFalse(int friend_uuid, int connected_user);

    List<FriendShipEntity> findByUserUuidAndStatusAndDeletedFalse(int uuid, GlobalAppStatus name, Pageable pageable);

    List<FriendShipEntity> findByFriendUuidAndStatusAndDeletedFalse(int friend, GlobalAppStatus name, Pageable pageable);

    List<FriendShipEntity> findByUserUuidAndFriendUuidAndStatusAndDeletedFalse(int connectedUser, int uuid, String name);

    //    @PostFilter("!filterObject.isDeleted() AND filterObject.isConfirmed()")
    @Query("SELECT f FROM FriendShipEntity f WHERE (f.friend.firstName LIKE CONCAT('%',:part1,'%') AND f.friend.lastName LIKE CONCAT('%',:part2,'%')) OR (f.friend.firstName LIKE CONCAT('%',:part2,'%') AND f.friend.lastName LIKE CONCAT('%',:part1,'%')) AND f.status = :status AND f.deleted = false")
    List<FriendShipEntity> findFriendsByFirstNameAndLastNameAndStatus(@Param("part1") String part1, @Param("part2") String part2, @Param("status") String status, Pageable pageable);

    @Query("SELECT f FROM FriendShipEntity f WHERE f.friend.username LIKE CONCAT('%',:prefix,'%') AND f.status = :status AND f.deleted = false")
    List<FriendShipEntity> findFriendByUsernameAndStatus(@Param("prefix") String part, @Param("status") String status, Pageable pageable);

    @Query("SELECT f FROM FriendShipEntity f WHERE (f.friend.uuid = :user1 AND f.user.uuid =:user2) OR (f.friend.uuid = :user2 AND f.user.uuid =:user1) AND f.deleted = false")
    FriendShipEntity findFriendShip(@Param("user1") int user1, @Param("user2") int user2);
}
