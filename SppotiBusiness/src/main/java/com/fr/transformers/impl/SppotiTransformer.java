package com.fr.transformers.impl;

import com.fr.commons.dto.sppoti.SppotiResponseDTO;
import com.fr.entities.SppotiEntity;

/**
 * Created by djenanewail on 3/18/17.
 */
public class SppotiTransformer {

    /**
     * Transform sppoti entity to DTO.
     *
     * @param sppotiEntity sppoti entity top transform.
     * @return sppoti DTP.
     */
    public static SppotiResponseDTO entityToDto(SppotiEntity sppotiEntity) {
        SppotiResponseDTO sppotiResponseDTO = new SppotiResponseDTO();

        sppotiResponseDTO.setId(sppotiEntity.getUuid());
        sppotiResponseDTO.setVersion(sppotiEntity.getVersion());

        sppotiResponseDTO.setTitre(sppotiEntity.getTitre());
        sppotiResponseDTO.setLocation(sppotiEntity.getLocation());
        sppotiResponseDTO.setDatetimeCreated(sppotiEntity.getDatetimeCreated());
        sppotiResponseDTO.setDateTimeStart(sppotiEntity.getDateTimeStart());
        sppotiResponseDTO.setDescription(sppotiEntity.getDescription());
        sppotiResponseDTO.setMaxMembersCount(sppotiEntity.getMaxMembersCount());
        sppotiResponseDTO.setCover(sppotiEntity.getCover());

        return sppotiResponseDTO;
    }

}
