package com.fr.transformers.impl;

import com.fr.commons.dto.ScoreDTO;
import com.fr.entities.ScoreEntity;
import com.fr.entities.SppotiEntity;
import com.fr.repositories.SppotiRepository;
import com.fr.transformers.ScoreTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * Transformer for the score entity.
 * <p>
 * Created by wdjenane on 04/04/2017.
 */
@Component
public class ScoreTransformerImpl extends AbstractTransformerImpl<ScoreDTO, ScoreEntity>
        implements ScoreTransformer {

    private final SppotiRepository sppotiRepository;

    @Autowired
    public ScoreTransformerImpl(SppotiRepository sppotiRepository) {
        this.sppotiRepository = sppotiRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScoreEntity dtoToModel(ScoreDTO dto) {
        ScoreEntity entity = super.dtoToModel(dto);

        Optional<SppotiEntity> optional = Optional.ofNullable(sppotiRepository.findByUuid(dto.getSppotiId()));
        optional.ifPresent(entity::setSppotiEntity);
        optional.orElseThrow(() -> new EntityNotFoundException("Sppoti not found !!"));

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScoreDTO modelToDto(ScoreEntity model) {
        ScoreDTO scoreDTO = super.modelToDto(model);
        scoreDTO.setSppotiId(model.getSppotiEntity().getUuid());
        return scoreDTO;
    }
}
