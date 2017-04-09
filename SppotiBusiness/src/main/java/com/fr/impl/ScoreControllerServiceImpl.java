package com.fr.impl;

import com.fr.commons.dto.ScoreDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.exception.NotAdminException;
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
    public void updateScore(ScoreDTO scoreDTO, Long connectedUserId) {
        Optional<ScoreEntity> scoreEntity = Optional.ofNullable(scoreRepository.findBySppotiEntityUuid(scoreDTO.getSppotiId()));

        scoreEntity.ifPresent(s -> {
            if(!s.getSppotiEntity().getUserSppoti().getId().equals(connectedUserId)){
                throw new NotAdminException("You're not the sppoti admin");
            }

            s.setScoreStatus(GlobalAppStatusEnum.valueOf(scoreDTO.getStatus()));
            scoreRepository.save(s);
            LOGGER.info("Score has been updated: " + scoreDTO);
        });

        scoreEntity.orElseThrow(() -> new EntityNotFoundException("Sppoti has no score yet! "));
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