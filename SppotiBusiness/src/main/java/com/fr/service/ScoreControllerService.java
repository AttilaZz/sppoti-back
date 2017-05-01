package com.fr.service;

import com.fr.commons.dto.ScoreDTO;
import org.springframework.stereotype.Service;

/**
 * Created by djenanewail on 4/7/17.
 */
@Service
public interface ScoreControllerService extends AbstractControllerService
{
	
	/**
	 * @param scoreDTO
	 * 		score to update.
	 * @param id connected user id.
	 */
	void updateScore(ScoreDTO scoreDTO, Long id);
	
	/**
	 * Add score to a sppoti.
	 *
	 * @param scoreDTO
	 * 		score to add.
	 *
	 * @return added score.
	 */
	ScoreDTO addSppotiScore(ScoreDTO scoreDTO);
}
