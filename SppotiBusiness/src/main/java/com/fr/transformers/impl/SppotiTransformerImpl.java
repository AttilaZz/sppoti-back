package com.fr.transformers.impl;

import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.utils.SppotiBeanUtils;
import com.fr.entities.SportEntity;
import com.fr.entities.SppotiAdverseEntity;
import com.fr.entities.SppotiEntity;
import com.fr.repositories.SportRepository;
import com.fr.transformers.ScoreTransformer;
import com.fr.transformers.SppotiTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 3/18/17.
 */
@Component
public class SppotiTransformerImpl extends AbstractTransformerImpl<SppotiDTO, SppotiEntity>
        implements SppotiTransformer {

    /**
     * Score transformer.
     */
    private final ScoreTransformer scoreTransformer;

    /**
     * Sport repository.
     */
    private final SportRepository sportRepository;

    /**
     * Team repository.
     */
    private final TeamTransformerImpl teamTransformer;

    /**
     * Team members repository.
     */
    private final TeamMemberTransformer teamMemberTransformer;

    /**
     * Init dependencies.
     */
    @Autowired
    public SppotiTransformerImpl(ScoreTransformer scoreTransformer, SportRepository sportRepository, TeamTransformerImpl teamTransformer, TeamMemberTransformer teamMemberTransformer) {
        this.scoreTransformer = scoreTransformer;
        this.sportRepository = sportRepository;
        this.teamTransformer = teamTransformer;
        this.teamMemberTransformer = teamMemberTransformer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SppotiEntity dtoToModel(SppotiDTO dto) {
        SppotiEntity entity = new SppotiEntity();
        SppotiBeanUtils.copyProperties(entity, dto);

        //Sppoti Sport
        SportEntity sportEntity = sportRepository.findOne(dto.getSportId());
        if (sportEntity == null) {
            throw new EntityNotFoundException("SportEntity id is incorrect");
        }
        entity.setSport(sportEntity);

        entity.setDatetimeCreated(new Date());
        if (StringUtils.hasText(dto.getTags())) {
            entity.setTags(dto.getTags());
        }

        if (StringUtils.hasText(dto.getDescription())) {
            entity.setDescription(dto.getDescription());
        }

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SppotiDTO modelToDto(SppotiEntity model) {
        SppotiDTO sppotiDTO = new SppotiDTO();
        SppotiBeanUtils.copyProperties(sppotiDTO, model);

        sppotiDTO.setId(model.getUuid());

        if (model.getScoreEntity() != null) {
            sppotiDTO.setScore(scoreTransformer.modelToDto(model.getScoreEntity()));
        }

        if (model.getTeamHostEntity() != null) {
            sppotiDTO.setTeamHost(teamTransformer.modelToDto(model.getTeamHostEntity()));
        }

        if (model.getAdverseTeams() != null) {
            //Connected user is sppoti admin.
            //connected user is one of the adverse teams.
            //connected user is member of one of the adverse teams.

            Predicate<SppotiAdverseEntity> dtoPredicate =
                    t -> model.getConnectedUserId().equals(model.getUserSppoti().getId()) ||
                            (!model.getConnectedUserId().equals(model.getUserSppoti().getId()) &&
                                    t.getTeam().getTeamMembers().stream().anyMatch(m ->
                                            m.getUsers().getId().equals(model.getConnectedUserId())));

            sppotiDTO.setTeamAdverse(model.getAdverseTeams().stream()
                    .filter(dtoPredicate)
                    .map(t -> {
                        t.getTeam().setRelatedSppotiId(model.getId());
                        TeamDTO dto = teamTransformer.modelToDto(t.getTeam());
                        dto.setTeamAdverseStatus(t.getStatus().name());
                        dto.setSentFromSppotiAdmin(t.getFromSppotiAdmin());
                        return dto;
                    }).collect(Collectors.toList()));
        }

        return sppotiDTO;
    }

}
