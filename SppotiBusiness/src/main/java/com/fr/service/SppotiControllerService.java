package com.fr.service;

import com.fr.commons.dto.SppotiRatingDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.SppotiStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@Service
public interface SppotiControllerService extends AbstractControllerService
{
	
	
	/**
	 * @param newSppoti
	 * 		sppoti to save.
	 *
	 * @return saved sppoti.
	 */
	SppotiDTO saveSppoti(SppotiDTO newSppoti);
	
	/**
	 * @param uuid
	 * 		sppoti unique id.
	 *
	 * @return found sppoti.
	 */
	SppotiDTO getSppotiByUuid(String uuid);
	
	/**
	 * Get all cppoties created by a user, even thoses refused by adverse team.
	 *
	 * @param id
	 * 		user id.
	 *
	 * @return all sppoties created by a user.
	 */
	List<SppotiDTO> getAllUserSppoties(String id, int page);
	
	/**
	 * Logical delete of a sppoti.
	 *
	 * @param id
	 * 		sppoti id.
	 */
	void deleteSppoti(String id);
	
	/**
	 * Update sppoti informations.
	 *
	 * @param sppotiRequest
	 * 		sppoti data to update.
	 * @param id
	 * 		sppoti id.
	 *
	 * @return sppoti DTO with updated data.
	 */
	SppotiDTO updateSppoti(SppotiDTO sppotiRequest, String id);
	
	/**
	 * ACCEPT sppoti and add notification.
	 * <p>
	 * When user accept to join sppoti, he also accept to join his team.
	 *
	 * @param sppotiId
	 * 		sppoti id.
	 * @param userId
	 * 		sppoter id.
	 */
	void acceptSppoti(String sppotiId, String userId);
	
	/**
	 * REFUSE sppoti and add notification.
	 * <p>
	 * When user refuse sppoti, relation with his team remain.
	 *
	 * @param sppotiId
	 * 		sppoti id.
	 * @param userId
	 * 		sppoter id.
	 */
	void refuseSppoti(String sppotiId, String userId);
	
	/**
	 * Get all sppoties that user asked to join, even those he rejects.
	 *
	 * @param id
	 * 		user id.
	 * @param page
	 * 		page  umber.
	 *
	 * @return all sppoties that user has joined.
	 */
	List<SppotiDTO> getAllJoinedSppoties(String id, int page);
	
	/**
	 * Check if user is the sppoti admin.
	 *
	 * @param sppotiId
	 * 		id of the sppoti.
	 */
	boolean isSppotiAdmin(String sppotiId);
	
	/**
	 * This method allow to challenge a team in a sppoti.
	 *
	 * @param sppotiId
	 * 		sppoti id.
	 * @param teamId
	 * 		team id.
	 */
	SppotiDTO sendChallengeToSppotiHostTeam(String sppotiId, String teamId, Long connectedUserId);
	
	/**
	 * rating many sppoters at a time.
	 *
	 * @param sppotiRatingDTO
	 * 		list of sppoters to rate.
	 * @param sppotiId
	 * 		sppoti id.
	 */
	List<UserDTO> rateSppoters(List<SppotiRatingDTO> sppotiRatingDTO, String sppotiId);
	
	/**
	 * Get all confirmed sppoties.
	 *
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return all confirmed sppoties.
	 */
	List<SppotiDTO> getAllConfirmedSppoties(String userId, int page);
	
	/**
	 * Get all refused sppoties.
	 *
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return all refused sppoties.
	 */
	List<SppotiDTO> getAllRefusedSppoties(String userId, int page);
	
	/**
	 * Accept / Refuse a sppoti challenge.
	 *
	 * @param sppotiId
	 * 		sppoti id.
	 * @param teamDTO
	 * 		team DTO containing the id of the accepted team.
	 */
	void chooseOneAdverseTeamFromAllChallengeRequests(String sppotiId, TeamDTO teamDTO);
	
	/**
	 * Get all upcoming sppoties with pagination.
	 *
	 * @param userId
	 * 		connected user id.
	 * @param page
	 * 		page number.
	 *
	 * @return list of upcoming sppoties.
	 */
	List<SppotiDTO> getAllUpcomingSppoties(String userId, int page);
	
	/**
	 * Get all sppoties where i had sent a challenge request and waiting for a request.
	 *
	 * @param userId
	 * 		user id.
	 * @param page
	 * 		page number.
	 *
	 * @return list of sppoties.
	 */
	List<SppotiDTO> getAllPendingChallengeRequestSppoties(String userId, int page);
	
	/**
	 * Find all sppoter who can join sppoti in order to add them to the team host or the adverse.
	 *
	 * @param prefix
	 * 		sppoter prefix name.
	 * @param page
	 * 		page number.
	 *
	 * @return list of {@link UserDTO}
	 */
	List<UserDTO> findAllSppoterAllowedToJoinSppoti(String prefix, String sppotiId, int page);
	
	/**
	 * Add sppoter.
	 *
	 * @param sppotiId
	 * 		sppoti id.
	 * @param userId
	 * 		sppoter id.
	 * @param teamId
	 * 		team id where to add sppoter.
	 *
	 * @return sppoter data.
	 */
	UserDTO addSppoter(String sppotiId, String userId, String teamId);
	
	/**
	 * Delete user from a sppoti.
	 *
	 * @param sppotiId
	 * 		sppoti id.
	 * @param userId
	 * 		user id.
	 */
	void deleteSppoter(String sppotiId, String userId);
	
	/**
	 * Update status to: Public or Private
	 *
	 * @param sppotiId
	 * 		id of the sppoti;
	 * @param type
	 * 		new type.
	 *
	 * @return updated sppoti with the newvalues.
	 */
	SppotiDTO updateSppotiType(String sppotiId, SppotiStatus type);
	
	/**
	 * Request join sppoti.
	 *
	 * @param sppotiId
	 * 		sppoti id to join.
	 *
	 * @return joined sppoti.
	 */
	SppotiDTO requestJoinSppoti(String sppotiId);
	
	/**
	 * Accept user request to join a team.
	 *
	 * @param sppotiId
	 * 		id of the requested sppoti.
	 * @param dto
	 * 		dto containing user id and response status, ACCEPTED.
	 */
	void confirmTeamRequestSentFromUser(String sppotiId, UserDTO dto);
	
	/**
	 * Refuse user request to join a team.
	 *
	 * @param sppotiId
	 * 		id of the requested sppoti.
	 * @param dto
	 * 		dto containing user id and response status, REFUSED.
	 */
	void refuseTeamRequestSentFromUser(String sppotiId, UserDTO dto);
}
