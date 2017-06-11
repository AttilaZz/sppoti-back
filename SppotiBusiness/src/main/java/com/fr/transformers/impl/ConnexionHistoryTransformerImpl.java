package com.fr.transformers.impl;

import com.fr.commons.dto.ConnexionHistoryDto;
import com.fr.commons.utils.SppotiBeanUtils;
import com.fr.entities.ConnexionHistoryEntity;
import com.fr.transformers.ConnexionHistoryTransformer;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 6/11/17.
 */
@Component
public class ConnexionHistoryTransformerImpl extends
		AbstractTransformerImpl<ConnexionHistoryDto, ConnexionHistoryEntity> implements ConnexionHistoryTransformer
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConnexionHistoryEntity dtoToModel(final ConnexionHistoryDto dto) {
		final ConnexionHistoryEntity model = new ConnexionHistoryEntity();
		SppotiBeanUtils.copyProperties(model, dto);
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
