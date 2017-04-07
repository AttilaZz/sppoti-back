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
     * @param model sppoti entity top transform.
     * @return sppoti DTP.
     */
    public static SppotiResponseDTO entityToDto(SppotiEntity model) {
        SppotiResponseDTO sppotiResponseDTO = new SppotiResponseDTO();

        sppotiResponseDTO.setId(model.getUuid());
        sppotiResponseDTO.setVersion(model.getVersion());

        sppotiResponseDTO.setTitre(model.getTitre());
        sppotiResponseDTO.setLocation(model.getLocation());
        sppotiResponseDTO.setDatetimeCreated(model.getDatetimeCreated());
        sppotiResponseDTO.setDateTimeStart(model.getDateTimeStart());
        sppotiResponseDTO.setDescription(model.getDescription());
        sppotiResponseDTO.setMaxMembersCount(model.getMaxMembersCount());
        sppotiResponseDTO.setCover(model.getCover());
        sppotiResponseDTO.setSppotiDuration(model.getSppotiDuration());

        return sppotiResponseDTO;
    }

}
