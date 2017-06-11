package com.fr.transformers.impl;

import com.fr.commons.dto.ConnexionHistoryDto;
import com.fr.commons.utils.SppotiBeanUtils;
import com.fr.entities.ConnexionHistoryEntity;
import com.fr.repositories.UserRepository;
import com.fr.transformers.ConnexionHistoryTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 6/11/17.
 */
@Component
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
		final ConnexionHistoryEntity model = new ConnexionHistoryEntity();
		SppotiBeanUtils.copyProperties(model, dto);
		
		model.setUser(this.userRepository.findOne(dto.getUserId()));
		return model;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConnexionHistoryDto modelToDto(final ConnexionHistoryEntity model) {
		final ConnexionHistoryDto dto = new ConnexionHistoryDto();
		SppotiBeanUtils.copyProperties(dto, model);
		return dto;
	}
}
