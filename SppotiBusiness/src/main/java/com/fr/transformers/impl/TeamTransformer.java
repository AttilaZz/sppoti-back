package com.fr.transformers.impl;

import com.fr.commons.dto.team.TeamRequestDTO;
import com.fr.commons.dto.team.TeamDTO;
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
     * @param model team entity top transform.
     * @return team entity as team DTO.
     */
    public TeamDTO modelToDto(TeamEntity model) {
        TeamDTO teamDTO = new TeamDTO();

        teamDTO.setId(model.getUuid());
        teamDTO.setVersion(model.getVersion());
        teamDTO.setName(model.getName());
        teamDTO.setCoverPath(model.getCoverPath());
        teamDTO.setLogoPath(model.getLogoPath());
        teamDTO.setSport(SportTransformer.modelToDto(model.getSport()));
        teamDTO.setCreationDate(model.getCreationDate());
        teamDTO.setColor(model.getColor());

        teamDTO.setTeamAdmin(teamMemberTransformer.modelToDto(teamMembersRepository.findByTeamUuidAndAdminTrue(model.getUuid()), null));

        teamDTO.setTeamMembers(
                model.getTeamMembers().stream()
                        .map(m -> teamMemberTransformer.modelToDto(m, null)).collect(Collectors.toList())
        );

        return teamDTO;
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
