package com.fr.repositories;

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
	TeamMemberEntity findByUsersUuidAndTeamUuid(int memberId, int teamId);
	
	/**
	 * @param userId
	 * 		user id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return list of found team.
	 */
	List<TeamMemberEntity> findByUsersUuidAndAdminTrue(int userId, Pageable pageable);
	
	/**
	 * @param memberId
	 * 		member id.
	 * @param teamId
	 * 		team id.
	 *
	 * @return found team.
	 */
	TeamMemberEntity findByUsersUuidAndTeamUuidAndAdminTrue(int memberId, int teamId);
	
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
	List<TeamMemberEntity> findByUsersUuidAndTeamNameContaining(int id, String team, Pageable pageable);
	
	/**
	 * @param teamId
	 * 		team id.
	 *
	 * @return found team.
	 */
	TeamMemberEntity findByTeamUuidAndAdminTrue(int teamId);
	
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
	List<TeamMemberEntity> findByTeamSportIdAndUsersUuidAndTeamNameContaining(Long sport, int user, String team,
																			  Pageable pageable);
	
	/**
	 * @param teamId
	 * 		team id;
	 *
	 * @return all team members.
	 */
	List<TeamMemberEntity> findByTeamUuid(int teamId);
	
	/**
	 * @param teamId
	 * 		team id.
	 * @param memberId
	 * 		member id.
	 *
	 * @return team member.
	 */
	TeamMemberEntity findByTeamUuidAndTeamCaptainTrue(int teamId, int memberId);
	
	/**
	 * @param userId
	 * 		user id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return all confirmed teams.
	 */
	List<TeamMemberEntity> findByUsersUuidAndAdminFalse(int userId, Pageable pageable);
	
	/**
	 * @param uuid
	 * 		user unique id.
	 * @param pageable
	 * 		page number.
	 *
	 * @return all deleted teams.
	 */
	List<TeamMemberEntity> findByUsersUuidAndTeamDeletedFalse(int uuid, Pageable pageable);
	
	/**
	 * @param sport
	 * 		sport id.
	 * @param team
	 * 		team name prefix.
	 * @param pageable
	 * 		page id.
	 *
	 * @return list of team members.
	 */
	List<TeamMemberEntity> findByTeamSportIdAndTeamNameContaining(Long sport, String team, Pageable pageable);
}
