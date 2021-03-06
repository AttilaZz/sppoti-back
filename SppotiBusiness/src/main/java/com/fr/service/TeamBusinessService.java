package com.fr.service;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.TeamStatus;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */
public interface TeamBusinessService extends AbstractBusinessService
{
	
	/**
	 * @param team
	 * 		team to save.
	 * @param adminId
	 * 		admin id.
	 *
	 * @return saved team data.
	 */
	TeamDTO saveTeam(TeamDTO team, Long adminId);
	
	/**
	 * @param teamId
	 * 		team id.
	 *
	 * @return team data.
	 */
	TeamDTO getTeamById(String teamId);
	
	/**
	 * Update memeber informations.
	 *
	 * @param request
	 * 		team to update.
	 * @param memberId
	 * 		member team id.
	 * @param teamId
	 * 		team id.
	 */
	void updateTeamMembers(TeamDTO request, String memberId, String teamId);
	
	/**
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return all team that user is admin
	 */
	List<TeamDTO> getAllTeamsByUserId(String userId, int page);
	
	/**
	 * Member can accept team invitation
	 *
	 * @param teamId
	 * 		team id.
	 */
	void acceptTeamRequestSentFromTeamAdmin(String teamId);
	
	/**
	 * Member can refuse team invitation
	 *
	 * @param teamId
	 * 		team id.
	 */
	void refuseTeamRequestSentFromTeamAdmin(String teamId);
	
	/**
	 * Delete a memeber from a team.
	 * This will delete all presence of this member in all teams and sppoties
	 * <p>
	 * UserDTO must be admin of team to delete members.
	 *
	 * @param teamId
	 * 		team id.
	 * @param memberId
	 * 		member team id.
	 */
	void deleteMemberFromTeam(String teamId, String memberId);
	
	/**
	 * Delete a team.
	 * <p>
	 * UserDTO must be admin to delete the team
	 *
	 * @param id
	 * 		team id.
	 */
	void deleteTeam(String id);
	
	/**
	 * @param teamId
	 * 		team id.
	 * @param userParam
	 * 		member to add.
	 *
	 * @return Added memeber.
	 */
	UserDTO addMember(String teamId, UserDTO userParam);
	
	/**
	 * @param team
	 * 		team to find.
	 * @param page
	 * 		page number.
	 *
	 * @return List of all teams that i am in.
	 */
	List<TeamDTO> findAllMyTeams(String team, int page);
	
	/**
	 * @param team
	 * 		team to find.
	 * @param page
	 * 		page number.
	 *
	 * @return List of all teams.
	 */
	List<TeamDTO> findAllTeams(String team, int page);
	
	/**
	 * @param team
	 * 		team to find.
	 * @param sport
	 * 		sport id.
	 * @param page
	 * 		page number.
	 *
	 * @return list of related teams.
	 */
	List<TeamDTO> findAllMyTeamsBySport(String team, Long sport, int page);
	
	/**
	 * @param teamId
	 * 		team id.
	 * @param TeamDTO
	 * 		team data to update.
	 *
	 * @return updated team.
	 */
	TeamDTO updateTeam(String teamId, TeamDTO TeamDTO);
	
	/**
	 * @param teamId
	 * 		team id.
	 * @param memberId
	 * 		member id.
	 */
	void updateTeamCaptain(String teamId, String memberId);
	
	/**
	 * Get all joined teams, except my teams.
	 *
	 * @param userId
	 * 		user iod.
	 * @param page
	 * 		page number.
	 *
	 * @return all teams.
	 */
	List<TeamDTO> getAllJoinedTeamsByUserId(String userId, int page);
	
	/**
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return all deleted teams.
	 */
	List<TeamDTO> getAllDeletedTeamsByUserId(String userId, int page);
	
	
	/**
	 * Answer to sppoti admin challenge.
	 *
	 * @param dto
	 * 		sppoti data.
	 * @param teamId
	 * 		team id.
	 */
	TeamDTO responseToSppotiAdminChallenge(SppotiDTO dto, String teamId);
	
	/**
	 * Get all pending challe,nge sppoti requests, sent from sppoti admin.
	 *
	 * @param teamId
	 * 		challenged team id.
	 * @param page
	 * 		page number.
	 *
	 * @return all pending sppoti challenge requests.
	 */
	List<SppotiDTO> findAllPendingChallenges(String teamId, int page);
	
	/**
	 * @param sppotiId
	 * 		sppoti id.
	 * @param team
	 * 		team name prefix.
	 * @param page
	 * 		page number.
	 *
	 * @return list of teams.
	 */
	List<TeamDTO> findAllAdverseTeamsAllowedToBeChallengedBySppotiAdmin(String sppotiId, String team, int page);
	
	/**
	 * Get all my teams that can send challenge request to sppoti admin.
	 *
	 * @param userId
	 * 		connected user id.
	 * @param sppotiId
	 * 		sppoti id.
	 * @param page
	 * 		page number.
	 *
	 * @return list of all founded teams.
	 */
	List<TeamDTO> findAllAllowedTeamsToChallengeSppoti(Long userId, String sppotiId, int page);
	
	/**
	 * Get all teams by sport id.
	 *
	 * @param sportId
	 * 		sport id.
	 * @param page
	 * 		page number.
	 *
	 * @return list of teams.
	 */
	List<TeamDTO> findAllTeamsBySportType(Long sportId, int page);
	
	/**
	 * Update team type to Public or Private
	 *
	 * @param teamId
	 * 		id of the team to update.
	 * @param type
	 * 		new type to assign.
	 *
	 * @return updated team data.
	 */
	TeamDTO updateTeamType(String teamId, TeamStatus type);
	
	/**
	 * Request team to join it.
	 *
	 * @param teamId
	 * 		team id to join.
	 *
	 * @return Joined team data.
	 */
	TeamDTO sendRequestToJoinTeam(String teamId);
	
	/**
	 * Accept user request to join a team.
	 *
	 * @param teamId
	 * 		id of the requested team.
	 * @param dto
	 * 		dto containing member id and response status, ACCEPTED.
	 */
	void confirmTeamRequestSentFromUser(String teamId, UserDTO dto);
	
	/**
	 * Cancel request sent to join a team.
	 *
	 * @param teamId
	 * 		requested team id.
	 */
	void cancelJoinTeamRequest(String teamId);
	
	/**
	 * Refuse user request to join a team.
	 *
	 * @param teamId
	 * 		id of the requested team.
	 * @param dto
	 * 		dto containing member id and response status, REFUSED.
	 */
	void refuseTeamRequestSentFromUser(String teamId, UserDTO dto);
	
	/**
	 * Leave a team.
	 *
	 * @param teamId
	 * 		team id to leave.
	 */
	void leaveTeam(String teamId);
}
