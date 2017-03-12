package com.fr.transformers;

import com.fr.commons.dto.team.TeamResponseDTO;
import com.fr.entities.TeamEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by djenanewail on 3/5/17.
 */
@Transactional(readOnly = true)
public class TeamTransformer {

    /**
     *Transform team entity to team DTO.
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

}
