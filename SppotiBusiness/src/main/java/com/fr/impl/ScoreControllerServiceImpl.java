package com.fr.impl;

import com.fr.commons.dto.ScoreDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.entities.ScoreEntity;
import com.fr.repositories.ScoreRepository;
import com.fr.service.ScoreControllerService;
import com.fr.transformers.ScoreTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * Created by djenanewail on 4/7/17.
 */
@Component
public class ScoreControllerServiceImpl extends AbstractControllerServiceImpl implements ScoreControllerService
{
	
	/** Score transformer. */
	private final ScoreTransformer scoreTransformer;
	
	/** Score Repository. */
	private final ScoreRepository scoreRepository;
	
	/** Class logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ScoreControllerServiceImpl.class);
	
	/** Init all dependencies. */
	@Autowired
	public ScoreControllerServiceImpl(final ScoreTransformer scoreTransformer, final ScoreRepository scoreRepository)
	{
		super(this.messagingTemplate);
		this.scoreTransformer = scoreTransformer;
		this.scoreRepository = scoreRepository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void updateScore(final ScoreDTO scoreDTO, final Long connectedUserId)
	{
		final Optional<ScoreEntity> scoreEntity = Optional
				.ofNullable(this.scoreRepository.findBySppotiEntityUuid(scoreDTO.getSppotiId()));
		
		scoreEntity.ifPresent(s -> {
			//            if(!s.getSppotiEntity().getUserSppoti().getId().equals(connectedUserId)){
			//                throw new NotAdminException("You're not the sppoti admin");
			//            }
			
			s.setScoreStatus(GlobalAppStatusEnum.valueOf(scoreDTO.getStatus()));
			this.scoreRepository.save(s);
			LOGGER.info("Score has been updated: " + scoreDTO);
		});
		
		scoreEntity.orElseThrow(() -> new EntityNotFoundException("Sppoti has no score yet! "));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ScoreDTO addSppotiScore(final ScoreDTO scoreDTO)
	{
		final ScoreEntity entity = this.scoreTransformer.dtoToModel(scoreDTO);
		return this.scoreTransformer.modelToDto(this.scoreRepository.save(entity));
	}
}