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
	TeamMemberEntity findByUserUuidAndTeamUuidAndStatusNot(int memberId, int teamId, GlobalAppStatusEnum status);
	
	/**
	 * @param userId
	 * 		user id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of found team.
	 */
	List<TeamMemberEntity> findByUserUuidAndStatusNotAndAdminTrue(int userId, GlobalAppStatusEnum status,
																  Pageable pageable);
	
	/**
	 * @param memberId
	 * 		member id.
	 * @param teamId
	 * 		team id.
	 *
	 * @return found team.
	 */
	TeamMemberEntity findByUserUuidAndTeamUuidAndStatusNotAndAdminTrue(int memberId, int teamId,
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
	List<TeamMemberEntity> findByUserUuidAndTeamNameContainingAndStatusNot(int id, String team,
																		   GlobalAppStatusEnum status,
																		   Pageable pageable);
	
	/**
	 * @param teamId
	 * 		team id.
	 *
	 * @return found team.
	 */
	TeamMemberEntity findByTeamUuidAndStatusNotAndAdminTrue(int teamId, GlobalAppStatusEnum status);
	
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
	List<TeamMemberEntity> findByTeamSportIdAndUserUuidAndTeamNameContainingAndStatusNot(Long sport, int user,
																						 String team,
																						 GlobalAppStatusEnum status,
																						 Pageable pageable);
	
	/**
	 * @param teamId
	 * 		team id;
	 *
	 * @return all team members.
	 */
	List<TeamMemberEntity> findByTeamUuidAndStatusNot(int teamId, GlobalAppStatusEnum status);
	
	/**
	 * @param userId
	 * 		user id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return all confirmed teams.
	 */
	List<TeamMemberEntity> findByUserUuidAndStatusNotAndAdminFalse(int userId, GlobalAppStatusEnum status,
																   Pageable pageable);
	
	/**
	 * @param uuid
	 * 		user unique id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return all deleted teams.
	 */
	List<TeamMemberEntity> findByUserUuidAndStatusNotAndTeamDeletedFalse(int uuid, GlobalAppStatusEnum status,
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
