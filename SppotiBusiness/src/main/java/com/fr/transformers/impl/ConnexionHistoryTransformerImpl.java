package com.fr.transformers.impl;

import com.fr.commons.dto.ConnexionHistoryDto;
import com.fr.entities.ConnexionHistoryEntity;
import com.fr.entities.UserEntity;
import com.fr.repositories.UserRepository;
import com.fr.transformers.ConnexionHistoryTransformer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by djenanewail on 6/11/17.
 */
public class ConnexionHistoryTransformerImpl extends
		AbstractTransformerImpl<ConnexionHistoryDto, ConnexionHistoryEntity> implements ConnexionHistoryTransformer
{
	
	/** User repository. */
	private final UserRepository userRepository;
	
	/** Init transformer. */
	@Autowired
	public ConnexionHistoryTransformerImpl(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConnexionHistoryEntity dtoToModel(final ConnexionHistoryDto dto) {
		final ConnexionHistoryEntity model = super.dtoToModel(dto);
		
		final UserEntity user = new UserEntity();
		user.setId(dto.getUserId());
		model.setUser(user);
		return model;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConnexionHistoryDto modelToDto(final ConnexionHistoryEntity model) {
		final ConnexionHistoryDto dto = super.modelToDto(model);
		dto.setId(model.getUuid());
		return dto;
	}
}
