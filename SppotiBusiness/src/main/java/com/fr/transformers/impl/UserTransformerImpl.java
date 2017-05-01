package com.fr.transformers.impl;

import com.fr.commons.dto.SignUpDTO;
import com.fr.commons.dto.SportDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.GenderEnum;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.LanguageEnum;
import com.fr.commons.utils.SppotiBeanUtils;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.ResourcesEntity;
import com.fr.entities.SportEntity;
import com.fr.entities.UserEntity;
import com.fr.transformers.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
	
	/**
	 * Sprig security crypt password.
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * Sport transformer.
	 */
	@Autowired
	private SportTransformer sportTransformer;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserEntity dtoToModel(UserDTO dto)
	{
		return super.dtoToModel(dto);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDTO modelToDto(UserEntity entity)
	{
		UserDTO dto = new UserDTO();
		SppotiBeanUtils.copyProperties(dto,entity);
		dto.setId(entity.getUuid());
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
		
		if (entity.getResources() != null) {
			UserDTO userResources = getUserCoverAndAvatar(entity);
			dto.setAvatar(userResources.getAvatar());
			dto.setCover(userResources.getCover());
			dto.setCoverType(userResources.getCoverType());
		}
		
		if (entity.getRelatedSports() != null && !entity.getRelatedSports().isEmpty()) {
			List<SportDTO> sportDTOs = entity.getRelatedSports().stream().map(sportTransformer::modelToDto)
					.collect(Collectors.toList());
			dto.setSportDTOs(sportDTOs);
		}
		
		return dto;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDTO getUserCoverAndAvatar(UserEntity targetUser)
	{
		
		UserDTO user = new UserDTO();
		Set<ResourcesEntity> resources = targetUser.getResources();
		
		List<ResourcesEntity> resourcesEntityTemp = new ArrayList<>();
		resourcesEntityTemp.addAll(resources);
		
		if (!resourcesEntityTemp.isEmpty()) {
			if (resourcesEntityTemp.size() == 2) {
				//cover and avatar found
				ResourcesEntity resource1 = resourcesEntityTemp.get(0);
				ResourcesEntity resource2 = resourcesEntityTemp.get(1);
				
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
				ResourcesEntity resource = resourcesEntityTemp.get(0);
				if (resource.getType() == 1) {//acatar
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
	public UserEntity signUpDtoToEntity(SignUpDTO dto)
	{
		UserEntity entity = new UserEntity();
		SppotiBeanUtils.copyProperties(entity,dto);
		entity.setGender(GenderEnum.valueOf(dto.getGenderType()));
		
		String confirmationCode = SppotiUtils.generateConfirmationKey();
		entity.setConfirmationCode(confirmationCode);
		
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		entity.setUsername(dto.getUsername().trim());
		return entity;
	}
}
