package com.fr.repositories;

import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.entities.SppotiAdverseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by djenanewail on 4/22/17.
 */
public interface SppotiAdverseRepository extends JpaRepository<SppotiAdverseEntity, Long>
{
	
	/**
	 * Find all challenge sent by sppoti admin for a team.
	 *
	 * @param teamId
	 * 		team id.
	 *
	 * @return list of {@link SppotiAdverseEntity}
	 */
	List<SppotiAdverseEntity> findByTeamUuidAndFromSppotiAdminTrue(String teamId);
	
	/**
	 * find all chellenges sent for a team.
	 *
	 * @param uuid
	 * 		team id.
	 *
	 * @return list of {@link SppotiAdverseEntity}
	 */
	List<SppotiAdverseEntity> findByTeamUuid(String uuid);
	
	/**
	 * @param userId
	 * 		user id.
	 * @param status
	 * 		challenge status.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of {@link SppotiAdverseEntity}
	 */
	List<SppotiAdverseEntity> findBySppotiUserSppotiUuidAndStatusAndFromSppotiAdminFalse(String userId,
																						 GlobalAppStatusEnum status,
																						 Pageable pageable);
}
