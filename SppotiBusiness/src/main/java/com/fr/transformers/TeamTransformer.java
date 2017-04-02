package com.fr.transformers;

import com.fr.commons.dto.team.TeamRequestDTO;
import com.fr.commons.dto.team.TeamResponseDTO;
import com.fr.entities.TeamEntity;
import com.fr.repositories.TeamMembersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * Created by djenanewail on 3/5/17.
 */
@Transactional(readOnly = true)
@Component
public class TeamTransformer {

    @Autowired
    private TeamMemberTransformer teamMemberTransformer;

    @Autowired
    private TeamMembersRepository teamMembersRepository;

    /**
     * Transform team entity to team DTO.
     *
     * @param teamEntity team entity top transform.
     * @return team entity as team DTO.
     */
    public TeamResponseDTO modelToDto(TeamEntity teamEntity) {
        TeamResponseDTO teamResponseDTO = new TeamResponseDTO();

        teamResponseDTO.setId(teamEntity.getUuid());
        teamResponseDTO.setVersion(teamEntity.getVersion());
        teamResponseDTO.setName(teamEntity.getName());
        teamResponseDTO.setCoverPath(teamEntity.getCoverPath());
        teamResponseDTO.setLogoPath(teamEntity.getLogoPath());
        teamResponseDTO.setSport(SportTransformer.modelToDto(teamEntity.getSport()));

        teamResponseDTO.setTeamAdmin(teamMemberTransformer.modelToDto(teamMembersRepository.findByTeamUuidAndAdminTrue(teamEntity.getUuid()), null));

        teamResponseDTO.setTeamMembers(
                teamEntity.getTeamMembers().stream()
                        .map(m -> teamMemberTransformer.modelToDto(m, null)).collect(Collectors.toList())
        );

        return teamResponseDTO;
    }

    /**
     * Transform team dto to entity.
     *
     * @param teamRequestDTO team dto to transform.
     * @return team entity.
     */
    public TeamEntity dtoToModel(TeamRequestDTO teamRequestDTO) {
        TeamEntity entity = new TeamEntity();

        entity.setUuid(teamRequestDTO.getId());
        entity.setVersion(teamRequestDTO.getVersion());

        entity.setName(teamRequestDTO.getName());
        entity.setCoverPath(teamRequestDTO.getCoverPath());
        entity.setLogoPath(teamRequestDTO.getLogoPath());

        return entity;
    }

}
