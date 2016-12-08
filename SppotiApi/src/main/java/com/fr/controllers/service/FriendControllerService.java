/**
 * 
 */
package com.fr.controllers.service;

import org.springframework.stereotype.Service;

import com.fr.entities.FriendShip;
import com.fr.entities.Users;

/**
 * Created by: Wail DJENANE on Jul 3, 2016
 */
@Service
public interface FriendControllerService extends AbstractControllerService {

	public Users getUserById(Long userId);

	public boolean saveFriend(FriendShip f);

	public boolean updateFriend(FriendShip f);

	public FriendShip getConfirmedFriendShip(Long userId, Long friendId);

	public FriendShip getPendingFriendShip(Long userId, Long friendId);

	public boolean deleteFriendShip(FriendShip fr);

	public FriendShip getfriendShipById(Long friendShipId);

	public Users getFrindfromUsername(String frindUsername);

	public FriendShip getFriendShip(Long userId, Long friendId);
}
