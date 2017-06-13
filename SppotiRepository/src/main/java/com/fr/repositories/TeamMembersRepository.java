package com.fr.repositories;

import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.entities.TeamMemberEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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
	TeamMemberEntity findByUserUuidAndTeamUuidAndStatusNot(String memberId, String teamId, GlobalAppStatusEnum status);
	
	/**
	 * @param userId
	 * 		user id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of found team.
	 */
	List<TeamMemberEntity> findByUserUuidAndStatusNotAndAdminTrue(String userId, GlobalAppStatusEnum status,
																  Pageable pageable);
	
	/**
	 * @param memberId
	 * 		member id.
	 * @param teamId
	 * 		team id.
	 *
	 * @return found team.
	 */
	TeamMemberEntity findByUserUuidAndTeamUuidAndStatusNotAndAdminTrue(String memberId, String teamId,
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
	List<TeamMemberEntity> findByUserUuidAndTeamNameContainingAndStatusNot(String id, String team,
																		   GlobalAppStatusEnum status,
																		   Pageable pageable);
	
	/**
	 * @param teamId
	 * 		team id.
	 *
	 * @return found team.
	 */
	TeamMemberEntity findByTeamUuidAndStatusNotAndAdminTrue(String teamId, GlobalAppStatusEnum status);
	
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
	List<TeamMemberEntity> findByTeamSportIdAndUserUuidAndTeamNameContainingAndStatusNot(Long sport, String user,
																						 String team,
																						 GlobalAppStatusEnum status,
																						 Pageable pageable);
	
	/**
	 * @param teamId
	 * 		team id;
	 *
	 * @return all team members.
	 */
	List<TeamMemberEntity> findByTeamUuidAndStatusNot(String teamId, GlobalAppStatusEnum status);
	
	/**
	 * @param userId
	 * 		user id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return all confirmed teams.
	 */
	List<TeamMemberEntity> findByUserUuidAndStatusNotAndAdminFalse(String userId, GlobalAppStatusEnum status,
																   Pageable pageable);
	
	/**
	 * @param uuid
	 * 		user unique id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return all deleted teams.
	 */
	List<TeamMemberEntity> findByUserUuidAndStatusNotAndTeamDeletedFalse(String uuid, GlobalAppStatusEnum status,
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
	List<TeamMemberEntity> findByTeamSportIdAndUserIdAndStatusNotAndAdminTrue(Long sportId, Long userId,
																			  GlobalAppStatusEnum status,
																			  Pageable pageable);
}
