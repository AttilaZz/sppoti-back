package com.fr.repositories;

import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.entities.FriendShipEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * Created by djenanewail on 12/31/16.
 */
public interface FriendShipRepository extends JpaRepository<FriendShipEntity, Long>
{
	
	FriendShipEntity findTopByFriendUuidAndUserUuidAndStatusNotInOrderByDatetimeCreatedDesc(String friendUuid,
																							String connectedUser,
																							Collection<GlobalAppStatusEnum> status);
	
	List<FriendShipEntity> findByUserUuidAndStatus(String uuid, GlobalAppStatusEnum name, Pageable pageable);
	
	List<FriendShipEntity> findByFriendUuidAndStatus(String friend, GlobalAppStatusEnum name, Pageable pageable);
	
	@Query(value = "SELECT f FROM FriendShipEntity f WHERE (f.friend.firstName LIKE CONCAT('%',:part1,'%') " +
			"AND f.friend.lastName LIKE CONCAT('%',:part2,'%')) OR (f.friend.firstName LIKE CONCAT('%',:part2,'%') " +
			"AND f.friend.lastName LIKE CONCAT('%',:part1,'%')) AND f.status = :status")
	List<FriendShipEntity> findFriendsByFirstNameAndLastNameAndStatus(@Param("part1") String part1,
																	  @Param("part2") String part2,
																	  @Param("status") String status,
																	  Pageable pageable);
	
	@Query(value = "SELECT f FROM FriendShipEntity f WHERE f.friend.username LIKE CONCAT('%',:prefix,'%') " +
			"AND f.status = :status")
	List<FriendShipEntity> findFriendByUsernameAndStatus(@Param("prefix") String part, @Param("status") String status,
														 Pageable pageable);
	
	@Query(value = "SELECT f FROM FriendShipEntity f WHERE (f.friend.uuid = :user1 AND f.user.uuid =:user2) " +
			"OR (f.friend.uuid = :user2 AND f.user.uuid =:user1) " + "ORDER BY f.datetimeCreated DESC")
	List<FriendShipEntity> findLastFriendShipOrderByDatetimeCreatedDesc(@Param("user1") String user1,
																		@Param("user2") String user2);
	
	@Query("SELECT f FROM FriendShipEntity f WHERE (f.user.uuid = :asUserId OR f.friend.uuid = :asFriendId) " +
			"AND status = :status")
	List<FriendShipEntity> findByUserUuidOrFriendUuidAndStatus(@Param("asUserId") String asUserId,
															   @Param("asFriendId") String asFriendId,
															   @Param("status") GlobalAppStatusEnum status,
															   Pageable pageable);
}