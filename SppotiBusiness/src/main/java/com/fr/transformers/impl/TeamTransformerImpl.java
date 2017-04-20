package com.fr.transformers.impl;

import com.fr.commons.dto.team.TeamDTO;
import com.fr.entities.SportEntity;
import com.fr.entities.TeamEntity;
import com.fr.repositories.TeamMembersRepository;
import com.fr.transformers.TeamTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * Created by djenanewail on 3/5/17.
 */
@Component
public class TeamTransformerImpl extends AbstractTransformerImpl<TeamDTO, TeamEntity>
        implements TeamTransformer {

    /**
     * {@link SportEntity} transformer.
     */
    private final SportTransformer sportTransformer;

    /**
     * {@link com.fr.entities.TeamMemberEntity} transformer.
     */
    private final TeamMemberTransformer teamMemberTransformer;

    /**
     * {@link com.fr.entities.TeamMemberEntity} repository.
     */
    private final TeamMembersRepository teamMembersRepository;

    /**
     * Init dependencies.
     */
    @Autowired
    public TeamTransformerImpl(SportTransformer sportTransformer, TeamMemberTransformer teamMemberTransformer, TeamMembersRepository teamMembersRepository) {
        this.sportTransformer = sportTransformer;
        this.teamMemberTransformer = teamMemberTransformer;
        this.teamMembersRepository = teamMembersRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeamDTO modelToDto(TeamEntity model) {
        TeamDTO teamDTO = new TeamDTO();

        teamDTO.setId(model.getUuid());
        teamDTO.setVersion(model.getVersion());
        teamDTO.setName(model.getName());
        teamDTO.setCoverPath(model.getCoverPath());
        teamDTO.setLogoPath(model.getLogoPath());
        teamDTO.setSport(sportTransformer.modelToDto(model.getSport()));
        teamDTO.setCreationDate(model.getCreationDate());
        teamDTO.setColor(model.getColor());

        teamDTO.setTeamAdmin(teamMemberTransformer.modelToDto(teamMembersRepository.findByTeamUuidAndAdminTrue(model.getUuid()), null));

        teamDTO.setMembers(
                model.getTeamMembers().stream()
                        .map(m -> teamMemberTransformer.modelToDto(m, null)).collect(Collectors.toList())
        );

        return teamDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeamEntity dtoToModel(TeamDTO dto) {
        TeamEntity entity = new TeamEntity();

        entity.setUuid(dto.getId());
        entity.setVersion(dto.getVersion());

        entity.setName(dto.getName());
        entity.setCoverPath(dto.getCoverPath());
        entity.setLogoPath(dto.getLogoPath());

        return entity;
    }

}
