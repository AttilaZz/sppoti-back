package com.fr.transformers.impl;

import com.fr.commons.dto.ScoreDTO;
import com.fr.commons.utils.SppotiBeanUtils;
import com.fr.entities.ScoreEntity;
import com.fr.entities.SppotiEntity;
import com.fr.repositories.SppotiRepository;
import com.fr.transformers.ScoreTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * Transformer for the score entity.
 * <p>
 * Created by wdjenane on 04/04/2017.
 */
public class ScoreTransformerImpl extends AbstractTransformerImpl<ScoreDTO, ScoreEntity> implements ScoreTransformer
{
	/** Sppoti repository. */
	private final SppotiRepository sppotiRepository;
	
	/** Init sppoti repository. */
	@Autowired
	public ScoreTransformerImpl(final SppotiRepository sppotiRepository)
	{
		this.sppotiRepository = sppotiRepository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScoreEntity dtoToModel(final ScoreDTO dto)
	{
		final ScoreEntity entity = new ScoreEntity();
		SppotiBeanUtils.copyProperties(entity, dto);
		
		final Optional<SppotiEntity> optional = Optional
				.ofNullable(this.sppotiRepository.findByUuid(dto.getSppotiId()));
		optional.ifPresent(s -> {
			entity.setSppotiEntity(s);
			s.setScoreEntity(entity);
		});
		optional.orElseThrow(() -> new EntityNotFoundException("Sppoti not found !!"));
		
		return entity;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScoreDTO modelToDto(final ScoreEntity model)
	{
		final ScoreDTO scoreDTO = new ScoreDTO();
		SppotiBeanUtils.copyProperties(scoreDTO, model);
		scoreDTO.setStatus(model.getScoreStatus().name());
		scoreDTO.setSppotiId(model.getSppotiEntity().getUuid());
		return scoreDTO;
	}
}
