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
     * Transform sport dto to entity.
     *
     * @param sportDTO sport DTO to transform.
     * @return SportEntity
     */
    public static SportEntity dtoToModel(SportDTO sportDTO){
        SportEntity sportEntity = new SportEntity();
        sportEntity.setUuid(sportDTO.getId());
        sportEntity.setVersion(sportDTO.getVersion());
        sportEntity.setName(sportDTO.getName());
        return sportEntity;
    }

    /**
     * Transform sport entity to dto.
     *
     * @param sportEntity sport entity to transform.
     * @return SportDTO.
     */
    public static SportDTO modelToDto(SportEntity sportEntity){
        SportDTO sportDTO = new SportDTO();
        sportDTO.setId(sportEntity.getUuid());
        sportDTO.setVersion(sportEntity.getVersion());
        sportDTO.setName(sportEntity.getName());
        return sportDTO;
    }

}
