package com.fr.impl;

import com.fr.commons.dto.FriendResponseDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.NotificationTypeEnum;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.entities.FriendShipEntity;
import com.fr.entities.UserEntity;
import com.fr.service.FriendControllerService;
import com.fr.transformers.UserTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
class FriendControllerServiceImpl extends AbstractControllerServiceImpl implements FriendControllerService
{
	
	/** Class logger. */
	private final Logger LOGGER = LoggerFactory.getLogger(FriendControllerServiceImpl.class);
	
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
	@Transactional
	@Override
	public void saveFriendShip(final UserDTO user)
	{
		
        /*
		Prepare friendShip
         */
		final UserEntity connectedUser = this.getConnectedUser();
		final UserEntity friend = this.getUserByUuId(user.getFriendUuid());

        /*
		Check if the friend id refers to an existing user account
         */
		if (friend == null) {
			throw new EntityNotFoundException("Friend id not found");
		}

        /*
		Friend with my self
         */
		if (connectedUser.equals(friend)) {
			throw new BusinessGlobalException("You're already friend with your self body !");
		}

        /*
		Check if friendship exist
         */
		final FriendShipEntity tempFriendShip = this.friendShipRepository
				.findLastByFriendUuidAndUserUuidAndStatusNotOrderByDatetimeCreatedDesc(friend.getUuid(),
						connectedUser.getUuid(), GlobalAppStatusEnum.DELETED);
		if (tempFriendShip != null && !tempFriendShip.getStatus().equals(GlobalAppStatusEnum.PUBLIC_RELATION)) {
			throw new EntityExistsException("FriendShip already exists !");
		}
		
		FriendShipEntity friendShip = new FriendShipEntity();
		
		//friendship already exist in a different status
		if (tempFriendShip != null && tempFriendShip.getStatus().equals(GlobalAppStatusEnum.PUBLIC_RELATION)) {
			friendShip = tempFriendShip;
			friendShip.setStatus(GlobalAppStatusEnum.PENDING);
		} else {
			final UserEntity u = this.getUserByUuId(user.getFriendUuid());
			friendShip.setFriend(u);
			friendShip.setUser(connectedUser);
		}
		
		if (this.friendShipRepository.save(friendShip) != null) {
			addNotification(NotificationTypeEnum.FRIEND_REQUEST_SENT, friendShip.getUser(), friendShip.getFriend(),
					null, null, null, null, null, null);
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void updateFriendShip(final Long userId, final String friendUuid, final int friendStatus)
	{

         /*
		Prepare friendShip
         */
		final UserEntity connectedUser = getUserById(userId);

         /*
		Check if i received a friend request from the USER in the request
         */
		final FriendShipEntity tempFriendShip = this.friendShipRepository
				.findLastByFriendUuidAndUserUuidAndStatusNotOrderByDatetimeCreatedDesc(connectedUser.getUuid(),
						friendUuid, GlobalAppStatusEnum.DELETED);
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
						friendShip.getUser(), null, null, null, null, null, null);
			} else if (friendShip.getStatus().equals(GlobalAppStatusEnum.REFUSED)) {
				addNotification(NotificationTypeEnum.FRIEND_REQUEST_REFUSED, friendShip.getFriend(),
						friendShip.getUser(), null, null, null, null, null, null);
			}
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public void deleteFriendShip(final String friendId)
	{
		final UserEntity connectedUser = getConnectedUser();
		
		//Check if friendship exist
		final List<FriendShipEntity> friendShip = this.friendShipRepository
				.findLastFriendShipOrderByDatetimeCreatedDesc(friendId, connectedUser.getUuid());
		if (friendShip.isEmpty()) {
			throw new EntityNotFoundException("Friendship not found");
		}
		
		friendShip.get(0).setStatus(GlobalAppStatusEnum.DELETED);
		this.friendShipRepository.save(friendShip);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserDTO> getConfirmedFriendList(final String userId, final int page)
	{
		
		final Pageable pageable = new PageRequest(page, this.friendListSize);
		
		final List<FriendShipEntity> friendShips = this.friendShipRepository
				.findByUserUuidOrFriendUuidAndStatus(userId, userId, GlobalAppStatusEnum.CONFIRMED, pageable);
		
		return friendShips.stream().map(f -> {
			UserDTO userDTO;
			if (Objects.equals(f.getUser().getUuid(), userId)) {
				userDTO = this.userTransformer.modelToDto(f.getFriend());
			} else {
				userDTO = this.userTransformer.modelToDto(f.getUser());
			}
			userDTO.setPassword(null);
			return userDTO;
		}).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FriendResponseDTO getAllSentPendingFriendList(final int page)
	{
		final UserEntity connectedUser = this.getConnectedUser();
		
		final Pageable pageable = new PageRequest(page, this.friendListSize, Sort.Direction.DESC, "datetimeCreated");
		
		final List<FriendShipEntity> friendShips = this.friendShipRepository
				.findByUserUuidAndStatus(connectedUser.getUuid(), GlobalAppStatusEnum.PENDING, pageable);
		
		return getFriendResponse(friendShips, connectedUser.getId());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FriendResponseDTO getAllReceivedPendingFriendList(final int page)
	{
		
		final UserEntity connectedUser = getConnectedUser();
		
		final Pageable pageable = new PageRequest(page, this.friendListSize, Sort.Direction.DESC, "datetimeCreated");
		
		final List<FriendShipEntity> friendShips = this.friendShipRepository
				.findByFriendUuidAndStatus(connectedUser.getUuid(), GlobalAppStatusEnum.PENDING, pageable);
		
		return getFriendResponse(friendShips, connectedUser.getId());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public FriendResponseDTO getRefusedFriendList(final int page)
	{
		
		final UserEntity connectedUser = getConnectedUser();
		
		
		final Pageable pageable = new PageRequest(page, this.friendListSize, Sort.Direction.DESC, "datetimeCreated");
		
		final List<FriendShipEntity> friendShips = this.friendShipRepository
				.findByUserUuidAndStatus(connectedUser.getUuid(), GlobalAppStatusEnum.REFUSED, pageable);
		
		return getFriendResponse(friendShips, connectedUser.getId());
	}
	
	/**
	 * @param friendShips
	 * 		list of all friendships.
	 *
	 * @return friend response DTO.
	 */
	private FriendResponseDTO getFriendResponse(final List<FriendShipEntity> friendShips, final Long connectedUser)
	{
		final List<UserDTO> friendList = new ArrayList<>();
		
		for (final FriendShipEntity friendShip : friendShips) {
			final UserEntity user;
			
			if (friendShip.getFriend().getId().equals(connectedUser)) {
				user = friendShip.getUser();
			} else {
				user = friendShip.getFriend();
			}
			
			friendList.add(this.userTransformer.modelToDto(user));
			
		}
		
		final FriendResponseDTO friendResponse = new FriendResponseDTO();
		friendResponse.setPendingList(friendList);
		
		return friendResponse;
	}
}