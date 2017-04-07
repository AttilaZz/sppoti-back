package com.fr.service;

import com.fr.commons.dto.ScoreDTO;
import org.springframework.stereotype.Service;

/**
 * Created by djenanewail on 4/7/17.
 */
@Service
public interface ScoreControllerService extends AbstractControllerService{

    /**
     * @param scoreDTO score to update.
     */
    void updateScore(ScoreDTO scoreDTO);

    /**
     * Add score to a sppoti.
     *
     * @param scoreDTO score to add.
     * @return added score.
     */
    ScoreDTO addSppotiScore(ScoreDTO scoreDTO);
}
