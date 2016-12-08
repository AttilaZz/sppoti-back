/**
 * 
 */
package com.fr.controllers.serviceImpl;

import org.springframework.stereotype.Component;

import com.fr.controllers.service.FriendControllerService;
import com.fr.entities.FriendShip;
import com.fr.entities.Users;

/**
 * Created by: Wail DJENANE on Jul 3, 2016
 */
@Component
public class FriendControllerServiceImpl extends AbstractControllerServiceImpl implements FriendControllerService {

	@Override
	public Users getUserById(Long userId) {
		return userDaoService.getEntityByID(userId);
	}

	@Override
	public boolean saveFriend(FriendShip f) {
		return friendDaoService.saveOrUpdate(f);
	}

	@Override
	public boolean updateFriend(FriendShip f) {
		return friendDaoService.update(f);
	}

	@Override
	public boolean deleteFriendShip(FriendShip fr) {
		return friendDaoService.delete(fr);
	}

	@Override
	public FriendShip getfriendShipById(Long friendShipId) {
		return friendDaoService.getEntityByID(friendShipId);
	}

	@Override
	public Users getFrindfromUsername(String frindUsername) {

		return userDaoService.getUserFromloginUsername(frindUsername, 1);
	}

	@Override
	public FriendShip getConfirmedFriendShip(Long userId, Long friendId) {
		return friendDaoService.getConfirmedFriendShip(userId, friendId);

	}

	@Override
	public FriendShip getPendingFriendShip(Long userId, Long friendId) {
		return friendDaoService.getPendingFriendShip(userId, friendId);

	}

	@Override
	public FriendShip getFriendShip(Long userId, Long friendId) {
		return friendDaoService.getFriendShip(userId, friendId);

	}

}
