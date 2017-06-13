package com.fr.repositories;

import com.fr.entities.TeamEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */
public interface TeamRepository extends JpaRepository<TeamEntity, Long>
{
	
	/**
	 * Find team by it's unique id, and delete status False.
	 *
	 * @param uuid
	 * 		team id.
	 *
	 * @return List of teams.
	 */
	List<TeamEntity> findByUuidAndDeletedFalse(String uuid);
	
	/**
	 * Find teams by a given prefix and delete status False.
	 *
	 * @param team
	 * 		team prefix.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of teams.
	 */
	List<TeamEntity> findByNameContainingAndDeletedFalse(String team, Pageable pageable);

	/**
	 * Find teams by a specific sport and a prefix and delete status False.
	 * This method filter sppoti admin teams from result.
	 *
	 * @param sport
	 * 		sport id.
	 * @param team
	 * 		team prefix.
	 * @param userId
	 * 		sppoti admin id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of teams.
	 */
	@Query("SELECT t FROM TeamEntity t WHERE (t.name LIKE CONCAT('%',:team,'%') AND t.sport.id = :sport) " +
			"AND t.id NOT IN (SELECT m.team.id FROM TeamMemberEntity m WHERE m.user.id = :user)")
	List<TeamEntity> findAllAllowedTeamsToChallengeAsSppotiAdmin(@Param("sport") Long sport, @Param("team") String team,
																 @Param("user") Long userId, Pageable pageable);
}
