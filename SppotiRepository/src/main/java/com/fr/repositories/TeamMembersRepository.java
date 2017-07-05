package com.fr.repositories;

import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.entities.TeamMemberEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by djenanewail on 2/4/17.
 */
public interface TeamMembersRepository extends JpaRepository<TeamMemberEntity, Long>
{
	
	/**
	 * @param memberId
	 * 		member team id.
	 * @param teamId
	 * 		team id.
	 * @param status
	 * 		status that should be filtered from response
	 *
	 * @return found team.
	 */
	TeamMemberEntity findByUserUuidAndTeamUuidAndStatusNotInAndTeamDeletedFalse(String memberId, String teamId,
																				Collection<GlobalAppStatusEnum> status);
	
	/**
	 * @param userId
	 * 		user id.
	 * @param status
	 * 		status that should be filtered from response.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of found team.
	 */
	List<TeamMemberEntity> findByUserUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(String userId,
																					   Collection<GlobalAppStatusEnum> status,
																					   Pageable pageable);
	
	/**
	 * @param memberId
	 * 		member id.
	 * @param teamId
	 * 		team id.
	 * @param status
	 * 		status that should be filtered from response.
	 *
	 * @return found team.
	 */
	TeamMemberEntity findByUserUuidAndTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(String memberId,
																							String teamId,
																							Collection<GlobalAppStatusEnum> status);
	
	/**
	 * @param id
	 * 		user id.
	 * @param team
	 * 		team id.
	 * @param status
	 * 		status that should be filtered from response.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of teams.
	 */
	List<TeamMemberEntity> findByUserUuidAndTeamNameContainingAndStatusNotInAndTeamDeletedFalse(String id, String team,
																								Collection<GlobalAppStatusEnum> status,
																								Pageable pageable);
	
	/**
	 * @param teamId
	 * 		team id.
	 * @param status
	 * 		status that should be filtered from response.
	 *
	 * @return found team.
	 */
	TeamMemberEntity findByTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(String teamId,
																				 Collection<GlobalAppStatusEnum> status);
	
	/**
	 * @param sport
	 * 		sport id.
	 * @param user
	 * 		user id.
	 * @param team
	 * 		team id.
	 * @param status
	 * 		status that should be filtered from response.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of teams.
	 */
	List<TeamMemberEntity> findByTeamSportIdAndUserUuidAndTeamNameContainingAndStatusNotInAndTeamDeletedFalse(
			Long sport, String user, String team, Collection<GlobalAppStatusEnum> status, Pageable pageable);
	
	/**
	 * @param teamId
	 * 		team id.
	 * @param status
	 * 		status that should be filtered from response.
	 *
	 * @return all team members.
	 */
	List<TeamMemberEntity> findByTeamUuidAndStatusNotInAndTeamDeletedFalse(String teamId,
																		   Collection<GlobalAppStatusEnum> status);
	
	/**
	 * @param userId
	 * 		user id.
	 * @param status
	 * 		status that should be filtered from response.
	 * @param pageable
	 * 		page number.
	 *
	 * @return all confirmed teams.
	 */
	List<TeamMemberEntity> findByUserUuidAndStatusNotInAndAdminFalseAndTeamDeletedFalse(String userId,
																						Collection<GlobalAppStatusEnum> status,
																						Pageable pageable);
	
	/**
	 * @param uuid
	 * 		user unique id.
	 * @param status
	 * 		status that should be filtered from response.
	 * @param pageable
	 * 		page number.
	 *
	 * @return all deleted teams.
	 */
	List<TeamMemberEntity> findByUserUuidAndStatusNotInAndTeamDeletedFalseAndTeamDeletedFalse(String uuid,
																							  Collection<GlobalAppStatusEnum> status,
																							  Pageable pageable);
	
	/**
	 * Find all user's teams by sport and admin true.
	 *
	 * @param sportId
	 * 		sppoti sport id.
	 * @param userId
	 * 		connected user id.
	 * @param status
	 * 		status that should be filtered from response.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of {@link TeamMemberEntity}
	 */
	List<TeamMemberEntity> findByTeamSportIdAndUserIdAndStatusNotInAndAdminTrueAndTeamDeletedFalse(Long sportId,
																								   Long userId,
																								   Collection<GlobalAppStatusEnum> status,
																								   Pageable pageable);
	
	/**
	 * Get the request that user sent to join a team.
	 *
	 * @param teamId
	 * 		team id which user asked to join.
	 * @param userId
	 * 		id of the user who asked to join.
	 * @param pending
	 * 		status of the request
	 *
	 * @return The team member entity of the request.
	 */
	Optional<TeamMemberEntity> findByTeamUuidAndUserUuidAndStatusAndRequestSentFromUserTrueAndTeamDeletedFalse(
			String teamId, String userId, GlobalAppStatusEnum pending);
}
