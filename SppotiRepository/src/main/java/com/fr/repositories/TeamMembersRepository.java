package com.fr.repositories;

import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.entities.TeamMemberEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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
	 *
	 * @return found team.
	 */
	TeamMemberEntity findByUserUuidAndTeamUuidAndStatusNotAndTeamDeletedFalse(String memberId, String teamId,
																			  GlobalAppStatusEnum status);
	
	/**
	 * @param userId
	 * 		user id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of found team.
	 */
	List<TeamMemberEntity> findByUserUuidAndStatusNotAndAdminTrueAndTeamDeletedFalse(String userId,
																					 GlobalAppStatusEnum status,
																					 Pageable pageable);
	
	/**
	 * @param memberId
	 * 		member id.
	 * @param teamId
	 * 		team id.
	 *
	 * @return found team.
	 */
	TeamMemberEntity findByUserUuidAndTeamUuidAndStatusNotAndAdminTrueAndTeamDeletedFalse(String memberId,
																						  String teamId,
																						  GlobalAppStatusEnum status);
	
	/**
	 * @param id
	 * 		user id.
	 * @param team
	 * 		team id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of teams.
	 */
	List<TeamMemberEntity> findByUserUuidAndTeamNameContainingAndStatusNotAndTeamDeletedFalse(String id, String team,
																							  GlobalAppStatusEnum status,
																							  Pageable pageable);
	
	/**
	 * @param teamId
	 * 		team id.
	 *
	 * @return found team.
	 */
	TeamMemberEntity findByTeamUuidAndStatusNotAndAdminTrueAndTeamDeletedFalse(String teamId,
																			   GlobalAppStatusEnum status);
	
	/**
	 * @param sport
	 * 		sport id.
	 * @param user
	 * 		user id.
	 * @param team
	 * 		team id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of teams.
	 */
	List<TeamMemberEntity> findByTeamSportIdAndUserUuidAndTeamNameContainingAndStatusNotAndTeamDeletedFalse(Long sport,
																											String user,
																											String team,
																											GlobalAppStatusEnum status,
																											Pageable pageable);
	
	/**
	 * @param teamId
	 * 		team id;
	 *
	 * @return all team members.
	 */
	List<TeamMemberEntity> findByTeamUuidAndStatusNotAndTeamDeletedFalse(String teamId, GlobalAppStatusEnum status);
	
	/**
	 * @param userId
	 * 		user id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return all confirmed teams.
	 */
	List<TeamMemberEntity> findByUserUuidAndStatusNotAndAdminFalseAndTeamDeletedFalse(String userId,
																					  GlobalAppStatusEnum status,
																					  Pageable pageable);
	
	/**
	 * @param uuid
	 * 		user unique id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return all deleted teams.
	 */
	List<TeamMemberEntity> findByUserUuidAndStatusNotAndTeamDeletedFalseAndTeamDeletedFalse(String uuid,
																							GlobalAppStatusEnum status,
																							Pageable pageable);
	
	/**
	 * Find all user's teams by sport and admin true.
	 *
	 * @param sportId
	 * 		sppoti sport id.
	 * @param userId
	 * 		connected user id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of {@link TeamMemberEntity}
	 */
	List<TeamMemberEntity> findByTeamSportIdAndUserIdAndStatusNotAndAdminTrueAndTeamDeletedFalse(Long sportId,
																								 Long userId,
																								 GlobalAppStatusEnum status,
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
