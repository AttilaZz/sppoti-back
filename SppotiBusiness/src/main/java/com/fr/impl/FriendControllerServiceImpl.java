package com.fr.impl;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.NotificationTypeEnum;
import com.fr.entities.FriendShipEntity;
import com.fr.entities.UserEntity;
import com.fr.service.FriendControllerService;
import com.fr.transformers.UserTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
class FriendControllerServiceImpl extends AbstractControllerServiceImpl implements FriendControllerService
{
	
	/** Class logger. */
	private final Logger LOGGER = Logger.getLogger(FriendControllerServiceImpl.class);
	
	/** {@link UserEntity} transformer. */
	private final UserTransformer userTransformer;
	
	/** Friend list size. */
	@Value("${key.friendShipPerPage}")
	private int friendListSize;
	
	/** Init services. */
	@Autowired
	public FriendControllerServiceImpl(final UserTransformer userTransformer)
	{
		this.userTransformer = userTransformer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FriendShipEntity> getByUserAndStatus(final int uuid, final GlobalAppStatusEnum name,
													 final Pageable pageable)
	{
		return this.friendShipRepository.findByUserUuidAndStatusAndDeletedFalse(uuid, name, pageable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FriendShipEntity> getByFriendUuidAndStatus(final int uuid, final GlobalAppStatusEnum name,
														   final Pageable pageable)
	{
		return this.friendShipRepository.findByFriendUuidAndStatusAndDeletedFalse(uuid, name, pageable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FriendShipEntity getByFriendUuidAndUser(final int friendId, final int userId)
	{
		return this.friendShipRepository.findByFriendUuidAndUserUuidAndDeletedFalse(friendId, userId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void saveFriendShip(final FriendShipEntity friendShip)
	{
		
		if (this.friendShipRepository.save(friendShip) != null) {
			addNotification(NotificationTypeEnum.FRIEND_REQUEST_SENT, friendShip.getUser(), friendShip.getFriend(),
					null, null);
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void updateFriendShip(final Long userId, final int friendUuid, final int friendStatus)
	{

         /*
		Prepare friendShip
         */
		final UserEntity connectedUser = getUserById(userId);

         /*
		Check if i received a friend request from the USER in the request
         */
		final FriendShipEntity tempFriendShip = getByFriendUuidAndUser(connectedUser.getUuid(), friendUuid);
		if (tempFriendShip == null) {
			this.LOGGER.error("UPDATE-FRIEND: FriendShipEntity not found !");
			throw new EntityNotFoundException(
					"FriendShipEntity not found between (" + connectedUser.getUuid() + ") And (" + friendUuid + ")");
		}

        /*
		prepare update
         */
		for (final GlobalAppStatusEnum globalAppStatus : GlobalAppStatusEnum.values()) {
			if (globalAppStatus.getValue() == friendStatus) {
				tempFriendShip.setStatus(globalAppStatus);
			}
		}
		
		//update and add notification
		final FriendShipEntity friendShip = this.friendShipRepository.save(tempFriendShip);
		if (friendShip != null) {
			if (friendShip.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)) {
				addNotification(NotificationTypeEnum.FRIEND_REQUEST_ACCEPTED, friendShip.getFriend(),
						friendShip.getUser(), null, null);
			} else if (friendShip.getStatus().equals(GlobalAppStatusEnum.REFUSED)) {
				addNotification(NotificationTypeEnum.FRIEND_REQUEST_REFUSED, friendShip.getFriend(),
						friendShip.getUser(), null, null);
			}
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void deleteFriendShip(final FriendShipEntity friendShip)
	{
		
		friendShip.setDeleted(true);
		this.friendShipRepository.save(friendShip);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FriendShipEntity findFriendShip(final int user1, final int user2)
	{
		return this.friendShipRepository.findFriendShip(user1, user2);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserDTO> getConfirmedFriendList(final int userId, final int page)
	{
		
		final Pageable pageable = new PageRequest(page, this.friendListSize);
		
		final List<FriendShipEntity> friendShips = this.friendShipRepository
				.findByUserUuidOrFriendUuidAndDeletedFalse(userId, userId, pageable);
		
		return friendShips.stream().filter(f -> f.getStatus().name().equals(GlobalAppStatusEnum.CONFIRMED.name()))
				.map(f -> {
					UserDTO userDTO;
					if (f.getUser().getUuid() == userId) {
						userDTO = this.userTransformer.modelToDto(f.getFriend());
					} else {
						userDTO = this.userTransformer.modelToDto(f.getUser());
					}
					userDTO.setPassword(null);
					return userDTO;
				}).collect(Collectors.toList());
		
	}
}