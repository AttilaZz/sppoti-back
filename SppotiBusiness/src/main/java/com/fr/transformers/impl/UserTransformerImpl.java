package com.fr.transformers.impl;

import com.fr.commons.dto.RoleDTO;
import com.fr.commons.dto.SignUpDTO;
import com.fr.commons.dto.SportDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.GenderEnum;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.utils.SppotiBeanUtils;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.FriendShipEntity;
import com.fr.entities.ResourcesEntity;
import com.fr.entities.UserEntity;
import com.fr.repositories.FriendShipRepository;
import com.fr.repositories.UserRepository;
import com.fr.transformers.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 3/18/17.
 */
@Component
public class UserTransformerImpl extends AbstractTransformerImpl<UserDTO, UserEntity> implements UserTransformer
{
	private final PasswordEncoder passwordEncoder;
	
	private final SportTransformer sportTransformer;
	
	private final UserRepository userRepository;
	
	private final FriendShipRepository friendShipRepository;
	
	@Autowired
	public UserTransformerImpl(final PasswordEncoder passwordEncoder, final SportTransformer sportTransformer,
							   final UserRepository userRepository, final FriendShipRepository friendShipRepository)
	{
		this.passwordEncoder = passwordEncoder;
		this.sportTransformer = sportTransformer;
		this.userRepository = userRepository;
		this.friendShipRepository = friendShipRepository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserEntity dtoToModel(final UserDTO dto)
	{
		return super.dtoToModel(dto);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDTO modelToDto(final UserEntity entity)
	{
		final UserDTO dto = new UserDTO();
		SppotiBeanUtils.copyProperties(dto, entity);
		dto.setId(entity.getUuid());
		dto.setTechId(entity.getId());
		dto.setLanguage(entity.getLanguageEnum().name());
		
		dto.setLastName(entity.getLastName());
		dto.setFirstName(entity.getFirstName());
		dto.setUsername(entity.getUsername());
		dto.setEmail(entity.getEmail());
		dto.setPhone(entity.getTelephone());
		dto.setId(entity.getUuid());
		dto.setLanguage(entity.getLanguageEnum().name());
		dto.setBirthDate(entity.getDateBorn());
		dto.setGender(entity.getGender().name());
		
		dto.getUserRoles().addAll(entity.getRoles().stream().map(r -> {
			RoleDTO roleDTO = new RoleDTO();
			roleDTO.setName(r.getName());
			return roleDTO;
		}).collect(Collectors.toList()));
		
		if (entity.getResources() != null) {
			final UserDTO userResources = getUserCoverAndAvatar(entity);
			dto.setAvatar(userResources.getAvatar());
			dto.setCover(userResources.getCover());
			dto.setCoverType(userResources.getCoverType());
		}
		
		if (entity.getRelatedSports() != null && !entity.getRelatedSports().isEmpty()) {
			final List<SportDTO> sportDTOs = entity.getRelatedSports().stream().map(this.sportTransformer::modelToDto)
					.collect(Collectors.toList());
			dto.setSportDTOs(sportDTOs);
		}
		
		if (entity.getConnectedUserId() != null && entity.getId() != null) {
			dto.setMyProfile(entity.getId().equals(entity.getConnectedUserId()));
			dto.setFriendStatus(this.getFriendShipStatus(entity.getUuid(), entity.getConnectedUserId()));
		}
		
		return dto;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDTO getUserCoverAndAvatar(final UserEntity targetUser)
	{
		
		final UserDTO user = new UserDTO();
		final Set<ResourcesEntity> resources = targetUser.getResources();
		
		final List<ResourcesEntity> resourcesEntityTemp = new ArrayList<>();
		resourcesEntityTemp.addAll(resources);
		
		if (!resourcesEntityTemp.isEmpty()) {
			if (resourcesEntityTemp.size() == 2) {
				//cover and avatar found
				final ResourcesEntity resource1 = resourcesEntityTemp.get(0);
				final ResourcesEntity resource2 = resourcesEntityTemp.get(1);
				
				if (resource1.getType() == 1 && resource2.getType() == 2) {
					user.setAvatar(resource1.getUrl());
					
					user.setCover(resource2.getUrl());
					user.setCoverType(resource2.getTypeExtension());
				} else if (resource1.getType() == 2 && resource2.getType() == 1) {
					user.setAvatar(resource2.getUrl());
					
					user.setCover(resource1.getUrl());
					user.setCoverType(resource1.getTypeExtension());
				}
				
			} else {
				// size is = 1 -> cover or avatar
				final ResourcesEntity resource = resourcesEntityTemp.get(0);
				if (resource.getType() == 1) {
					user.setAvatar(resource.getUrl());
				} else {
					user.setCover(resource.getUrl());
					user.setCoverType(resource.getTypeExtension());
				}
			}
		}
		
		return user;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserEntity signUpDtoToEntity(final SignUpDTO dto)
	{
		final UserEntity entity = new UserEntity();
		SppotiBeanUtils.copyProperties(entity, dto);
		entity.setGender(GenderEnum.valueOf(dto.getGenderType()));
		
		final String confirmationCode = SppotiUtils.generateConfirmationKey();
		entity.setConfirmationCode(confirmationCode);
		
		entity.setPassword(this.passwordEncoder.encode(dto.getPassword()));
		entity.setUsername(dto.getUsername().trim());
		entity.setFirstConnexion(false);
		
		return entity;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserEntity> dtoToModel(final List<UserDTO> dtos) {
		return dtos.stream().map(this::dtoToModel).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserDTO> modelToDto(final List<UserEntity> models) {
		return models.stream().map(u -> {
			UserDTO dto = this.modelToDto(u);
			u.setPassword(null);
			return dto;
		}).collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserDTO> iterableModelsToDtos(final Iterable<UserEntity> models) {
		final List<UserDTO> dtos = super.iterableModelsToDtos(models);
		dtos.forEach(u -> u.setPassword(null));
		return dtos;
	}
	
	/**
	 * {@inheritDoc}
	 */
	//TODO: optimize this code
	private Integer getFriendShipStatus(final String targetUserId, final Long connectedUserId) {
		
		final UserEntity connectedUser = this.userRepository.findOne(connectedUserId);
		
		if (!connectedUser.getUuid().equals(targetUserId)) {
			final FriendShipEntity sent = this.friendShipRepository
					.findLastByFriendUuidAndUserUuidAndStatusNotInOrderByDatetimeCreatedDesc(connectedUser.getUuid(),
							targetUserId, SppotiUtils.statusToFilter());
			
			final FriendShipEntity received = this.friendShipRepository
					.findLastByFriendUuidAndUserUuidAndStatusNotInOrderByDatetimeCreatedDesc(targetUserId,
							connectedUser.getUuid(), SppotiUtils.statusToFilter());
			
			if (sent == null) {
				//When receiving friend request
				
				if (received != null) {
					if (GlobalAppStatusEnum.PENDING.equals(received.getStatus())) {
						return GlobalAppStatusEnum.PENDING.getValue();
					} else if (GlobalAppStatusEnum.CONFIRMED.equals(received.getStatus())) {
						return GlobalAppStatusEnum.CONFIRMED.getValue();
					} else if (GlobalAppStatusEnum.REFUSED.equals(received.getStatus())) {
						return GlobalAppStatusEnum.REFUSED.getValue();
					}
				}
				
			} else {
				//When sending friend request
				if (GlobalAppStatusEnum.CONFIRMED.equals(sent.getStatus())) {
					return GlobalAppStatusEnum.CONFIRMED.getValue();
				} else if (GlobalAppStatusEnum.PENDING.equals(sent.getStatus())) {
					return GlobalAppStatusEnum.PENDING_SENT.getValue();
				} else if (GlobalAppStatusEnum.REFUSED.equals(sent.getStatus())) {
					return GlobalAppStatusEnum.REFUSED.getValue();
				}
			}
		}
		
		return GlobalAppStatusEnum.PUBLIC_RELATION.getValue();
	}
}