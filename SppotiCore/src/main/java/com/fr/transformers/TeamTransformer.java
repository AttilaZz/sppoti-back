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
        teamResponseDTO.setName(teamEntity.getName());
        if (teamEntity.getCoverPath() != null) {
            teamResponseDTO.setCoverPath(teamEntity.getCoverPath());
        }
        if (teamEntity.getLogoPath() != null) {
            teamResponseDTO.setLogoPath(teamEntity.getLogoPath());
        }

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

        if (teamRequestDTO.getName() != null) {
            entity.setName(teamRequestDTO.getName());
        }

        if (teamRequestDTO.getCoverPath() != null) {
            entity.setCoverPath(teamRequestDTO.getCoverPath());
        }
        if (teamRequestDTO.getLogoPath() != null) {
            entity.setLogoPath(teamRequestDTO.getLogoPath());
        }

        return entity;
    }

}
