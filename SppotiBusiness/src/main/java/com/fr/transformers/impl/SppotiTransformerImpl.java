package com.fr.transformers.impl;

import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.utils.SppotiBeanUtils;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.SportEntity;
import com.fr.entities.SppotiEntity;
import com.fr.repositories.SportRepository;
import com.fr.repositories.UserRepository;
import com.fr.transformers.ScoreTransformer;
import com.fr.transformers.SppotiTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 3/18/17.
 */
@Component
public class SppotiTransformerImpl extends AbstractTransformerImpl<SppotiDTO, SppotiEntity> implements SppotiTransformer
{
	
	/** Score transformer. */
	private final ScoreTransformer scoreTransformer;
	
	/** Sport repository. */
	private final SportRepository sportRepository;
	
	/** Team repository. */
	private final TeamTransformerImpl teamTransformer;
	
	/** Sport transformer. */
	private final SportTransformer sportTransformer;
	
	/** Team members repository. */
	private final UserRepository userRepository;
	
	/** Init dependencies. */
	@Autowired
	public SppotiTransformerImpl(final ScoreTransformer scoreTransformer, final SportRepository sportRepository,
								 final TeamTransformerImpl teamTransformer, final SportTransformer sportTransformer,
								 final UserRepository userRepository)
	{
		this.scoreTransformer = scoreTransformer;
		this.sportRepository = sportRepository;
		this.teamTransformer = teamTransformer;
		this.sportTransformer = sportTransformer;
		this.userRepository = userRepository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SppotiEntity dtoToModel(final SppotiDTO dto)
	{
		final SppotiEntity entity = new SppotiEntity();
		SppotiBeanUtils.copyProperties(entity, dto);
		
		//Sppoti Sport
		final SportEntity sportEntity = this.sportRepository.findOne(dto.getSportId());
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
	public SppotiDTO modelToDto(final SppotiEntity model)
	{
		final SppotiDTO dto = new SppotiDTO();
		SppotiBeanUtils.copyProperties(dto, model);
		
		dto.setId(model.getUuid());
		dto.setDatetimeCreated(SppotiUtils.dateWithTimeZone(model.getDatetimeCreated(), model.getTimeZone()));
		
		if (model.getConnectedUserId() != null)
			dto.setConnectedUserId(this.userRepository.findOne(model.getConnectedUserId()).getUuid());
		
		if (model.getSport() != null) {
			dto.setRelatedSport(this.sportTransformer.modelToDto(model.getSport()));
		}
		
		if (model.getScoreEntity() != null) {
			dto.setScore(this.scoreTransformer.modelToDto(model.getScoreEntity()));
		}
		
		if (model.getTeamHostEntity() != null) {
			dto.setTeamHost(this.teamTransformer.modelToDto(model.getTeamHostEntity()));
		}
		
		if (model.getAdverseTeams() != null && model.getConnectedUserId() != null) {
			dto.setTeamAdverse(model.getAdverseTeams().stream().map(t -> {
				t.getTeam().setRelatedSppotiId(model.getId());
				TeamDTO teamDTO = this.teamTransformer.modelToDto(t.getTeam());
				teamDTO.setTeamAdverseStatus(t.getStatus().name());
				teamDTO.setSentFromSppotiAdmin(t.getFromSppotiAdmin());
				return teamDTO;
			}).collect(Collectors.toList()));
		}
		
		dto.setMySppoti(model.getUserSppoti().getId().equals(model.getConnectedUserId()));
		
		return dto;
	}
	
}