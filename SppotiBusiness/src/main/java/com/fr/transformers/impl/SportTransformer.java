package com.fr.transformers.impl;

import com.fr.commons.dto.SportDTO;
import com.fr.entities.SportEntity;
import org.springframework.stereotype.Component;

/**
 * Transformer for the {@link com.fr.entities.SportEntity}.
 * <p>
 * Created by wdjenane on 30/03/2017.
 */
@Component
public class SportTransformer
{
	
	/**
	 * Transform sport entity to dto.
	 *
	 * @param sportEntity
	 * 		sport entity to transform.
	 *
	 * @return SportDTO.
	 */
	public SportDTO modelToDto(SportEntity sportEntity)
	{
		SportDTO sportDTO = new SportDTO();
		sportDTO.setId(sportEntity.getId());
		sportDTO.setName(sportEntity.getName());
		return sportDTO;
	}
	
}
