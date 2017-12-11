package com.fr.transformers.impl;

import com.fr.commons.dto.SportDTO;
import com.fr.entities.SportEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Transformer for the {@link com.fr.entities.SportEntity}.
 * <p>
 * Created by wdjenane on 30/03/2017.
 */
@Component
public class SportTransformer
{
	public SportDTO modelToDto(final SportEntity sportEntity)
	{
		final SportDTO sportDTO = new SportDTO();
		sportDTO.setId(sportEntity.getId());
		sportDTO.setName(sportEntity.getName());
		return sportDTO;
	}
	
	public List<SportDTO> modelToDto(final List<SportEntity> sportEntities) {
		return sportEntities.stream().map(this::modelToDto).collect(Collectors.toList());
	}
}