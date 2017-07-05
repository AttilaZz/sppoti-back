package com.fr.repositories;

import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.entities.SppoterEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * Created by djenanewail on 2/5/17.
 */
public interface SppoterRepository extends JpaRepository<SppoterEntity, Long>
{
	
	
	/**
	 * find sppoter by his global user id and sppoti.
	 * (Expect only one result)
	 *
	 * @param userId
	 * 		user id.
	 * @param sppotiId
	 * 		sppoti id.
	 * @param status
	 * 		status to filter from all rows.
	 *
	 * @return sppoter related to a defined team and a sppoti.
	 */
	SppoterEntity findByTeamMemberUserUuidAndSppotiUuidAndStatusNotInAndSppotiDeletedFalse(String userId,
																						   String sppotiId,
																						   Collection<GlobalAppStatusEnum> status);
	
	/**
	 * Find sppoter by team and sport.
	 * (Expect multiple results)
	 *
	 * @param uuid
	 * 		user id.
	 * @param id
	 * 		sport id.
	 * @param status
	 * 		status to filter from all rows.
	 *
	 * @return all joined sppoties for a particular sport.
	 */
	List<SppoterEntity> findByTeamMemberUserUuidAndSppotiSportIdAndStatusNotInAndSppotiDeletedFalse(String uuid,
																									Long id,
																									Collection<GlobalAppStatusEnum> status);
	
	/**
	 * Find sppoter by his global user id.
	 * (Expect multiple results)
	 *
	 * @param userId
	 * 		user id.
	 * @param status
	 * 		status to filter from all rows.
	 * @param pageable
	 * 		page number.
	 *
	 * @return return all joined sppoties, unless refused ones.
	 */
	List<SppoterEntity> findByTeamMemberUserUuidAndStatusNotInAndSppotiDeletedFalse(String userId,
																					Collection<GlobalAppStatusEnum> status,
																					Pageable pageable);
	
	/**
	 * Find all upcomming sppoties:
	 * - My sppoties.
	 * - Joined sppoties.
	 * - Date in the future.
	 *
	 * @param userId
	 * 		connected user id.
	 * @param status
	 * 		sppoter status.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of {@link SppoterEntity}
	 */
	@Query("SELECT s FROM SppoterEntity s WHERE s.teamMember.user.uuid= :userId " +
			"AND s.sppoti.dateTimeStart > now() " + "AND s.status = :status " +
			"AND s.sppoti.deleted = false ORDER BY s.sppoti.dateTimeStart ASC")
	List<SppoterEntity> findAllUpcomingSppoties(@Param("userId") String userId,
												@Param("status") GlobalAppStatusEnum status, Pageable pageable);
	
	/**
	 * Find all sppoter's entries
	 *
	 * @param userId
	 * 		user id.
	 * @param sppotiId
	 * 		sppoti id.
	 *
	 * @return list of all user entries in sppoter table.
	 */
	List<SppoterEntity> findByTeamMemberUserUuidAndSppotiUuidAndSppotiDeletedFalse(String userId, String sppotiId);
}
