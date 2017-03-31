package com.fr.transformers;

import com.fr.commons.dto.team.TeamRequestDTO;
import com.fr.commons.dto.team.TeamResponseDTO;
import com.fr.entities.TeamEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by djenanewail on 3/5/17.
 */
@Transactional(readOnly = true)
public class TeamTransformer {

    /**
     * Transform team entity to team DTO.
     *
     * @param teamEntity team entity top transform.
     * @return team entity as team DTO.
     */
    public static TeamResponseDTO teamEntityToDto(TeamEntity teamEntity) {
        TeamResponseDTO teamResponseDTO = new TeamResponseDTO();

        teamResponseDTO.setId(teamEntity.getUuid());
        teamResponseDTO.setVersion(teamEntity.getVersion());
        teamResponseDTO.setName(teamEntity.getName());
        teamResponseDTO.setCoverPath(teamEntity.getCoverPath());
        teamResponseDTO.setLogoPath(teamEntity.getLogoPath());
        teamResponseDTO.setSport(SportTransformer.modelToDto(teamEntity.getSport()));

        return teamResponseDTO;
    }

    /**
     * Transform team dto to entity.
     *
     * @param teamRequestDTO team dto to transform.
     * @return team entity.
     */
    public static TeamEntity teamDtoToEntity(TeamRequestDTO teamRequestDTO) {
        TeamEntity entity = new TeamEntity();

        entity.setUuid(teamRequestDTO.getId());
        entity.setVersion(teamRequestDTO.getVersion());

        entity.setName(teamRequestDTO.getName());
        entity.setCoverPath(teamRequestDTO.getCoverPath());
        entity.setLogoPath(teamRequestDTO.getLogoPath());

        return entity;
    }

}
