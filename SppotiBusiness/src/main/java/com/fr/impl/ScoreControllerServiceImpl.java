package com.fr.impl;

import com.fr.commons.dto.ScoreDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.entities.ScoreEntity;
import com.fr.repositories.ScoreRepository;
import com.fr.service.ScoreControllerService;
import com.fr.transformers.ScoreTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * Created by djenanewail on 4/7/17.
 */
@Component
public class ScoreControllerServiceImpl extends AbstractControllerServiceImpl implements ScoreControllerService {

    /** Score transformer. */
    private final ScoreTransformer scoreTransformer;

    /** Score Repository. */
    private final ScoreRepository scoreRepository;

    /** Class logger. */
    private static Logger LOGGER = Logger.getLogger(ScoreControllerServiceImpl.class);

    /** Init all dependencies.*/
    @Autowired
    public ScoreControllerServiceImpl(ScoreTransformer scoreTransformer, ScoreRepository scoreRepository) {
        this.scoreTransformer = scoreTransformer;
        this.scoreRepository = scoreRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updateScore(ScoreDTO scoreDTO) {
        Optional<ScoreEntity> scoreEntity = Optional.ofNullable(scoreRepository.findByUuid(scoreDTO.getId()));

        scoreEntity.ifPresent(s -> {
            s.setScoreStatus(GlobalAppStatusEnum.valueOf(scoreDTO.getStatus()));
            scoreRepository.save(s);
            LOGGER.info("Score has been saved: " + scoreDTO);
        });

        scoreEntity.orElseThrow(() -> new EntityNotFoundException("Score not found ! "));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ScoreDTO addSppotiScore(ScoreDTO scoreDTO) {
        ScoreEntity entity = scoreTransformer.dtoToModel(scoreDTO);
        return scoreTransformer.modelToDto(scoreRepository.save(entity));
    }
}