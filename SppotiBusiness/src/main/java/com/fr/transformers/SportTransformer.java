package com.fr.transformers;

import com.fr.commons.SportDTO;
import com.fr.entities.SportEntity;

/**
 * Transformer for the {@link com.fr.entities.SportEntity}.
 * <p>
 * Created by wdjenane on 30/03/2017.
 */
public class SportTransformer {

    /**
     * Transform sport entity to dto.
     *
     * @param sportEntity sport entity to transform.
     * @return SportDTO.
     */
    public static SportDTO modelToDto(SportEntity sportEntity){
        SportDTO sportDTO = new SportDTO();
        sportDTO.setId(sportEntity.getId());
        sportDTO.setName(sportEntity.getName());
        return sportDTO;
    }

}
