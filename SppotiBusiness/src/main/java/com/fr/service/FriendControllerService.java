package com.fr.service;

import com.fr.commons.dto.FriendResponseDTO;
import com.fr.commons.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */
@Service
public interface FriendControllerService extends AbstractControllerService
{
	
	/**
	 * Save the friendship and send notification
	 *
	 * @param friendShip
	 * 		friendship.
	 */
	void saveFriendShip(UserDTO friendShip);
	
	/**
	 * Update status (ACCEPT/REFUSE) friend request and send notification
	 *
	 * @param userId
	 * 		user id.
	 * @param friendUuid
	 * 		friend id.
	 * @param friendStatus
	 * 		friend status.
	 */
	void updateFriendShip(Long userId, int friendUuid, int friendStatus);
	
	/**
	 * Delete friendship by the sender
	 *
	 * @param friendId
	 * 		id of user friend.
	 */
	void deleteFriendShip(int friendId);
	
	/**
	 * @param userId
	 * 		user id.
	 *
	 * @return all confirmed friend requests.
	 */
	List<UserDTO> getConfirmedFriendList(int userId, int page);
	
	/**
	 * @param page
	 * 		page number.
	 *
	 * @return list of all pending friend request sent.
	 */
	FriendResponseDTO getAllSentPendingFriendList(int page);
	
	/**
	 * @param page
	 * 		page number.
	 *
	 * @return list of all received friend request sent.
	 */
	FriendResponseDTO getAllReceivedPendingFriendList(int page);
	
	/**
	 * Get all refused friendship request for the connected user.
	 *
	 * @param page
	 * 		page number.
	 *
	 * @return list of refused friend request.
	 */
	FriendResponseDTO getRefusedFriendList(int page);
}
