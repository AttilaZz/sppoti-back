package com.fr.transformers.impl;

import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.SportEntity;
import com.fr.entities.SppotiEntity;
import com.fr.entities.TeamEntity;
import com.fr.repositories.SportRepository;
import com.fr.repositories.SppotiRepository;
import com.fr.repositories.TeamMembersRepository;
import com.fr.transformers.TeamTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Created by djenanewail on 3/5/17.
 */
@Component
public class TeamTransformerImpl extends AbstractTransformerImpl<TeamDTO, TeamEntity> implements TeamTransformer
{
	
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
	 * {@link com.fr.entities.SppotiEntity} repository.
	 */
	private final SppotiRepository sppotiRepository;
	
	/**
	 * {@link SportEntity} repository.
	 */
	private final SportRepository sportRepository;
	
	/**
	 * Init dependencies.
	 */
	@Autowired
	public TeamTransformerImpl(final SportTransformer sportTransformer,
							   final TeamMemberTransformer teamMemberTransformer,
							   final TeamMembersRepository teamMembersRepository,
							   final SppotiRepository sppotiRepository, final SportRepository sportRepository)
	{
		this.sportTransformer = sportTransformer;
		this.teamMemberTransformer = teamMemberTransformer;
		this.teamMembersRepository = teamMembersRepository;
		this.sppotiRepository = sppotiRepository;
		this.sportRepository = sportRepository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TeamDTO modelToDto(final TeamEntity model)
	{
		final TeamDTO teamDTO = new TeamDTO();
		
		teamDTO.setId(model.getUuid());
		teamDTO.setVersion(model.getVersion());
		teamDTO.setName(model.getName());
		teamDTO.setCoverPath(model.getCoverPath());
		teamDTO.setLogoPath(model.getLogoPath());
		teamDTO.setSport(this.sportTransformer.modelToDto(model.getSport()));
		teamDTO.setCreationDate(SppotiUtils.dateWithTimeZone(model.getCreationDate(), model.getTimeZone()));
		teamDTO.setColor(model.getColor());
		teamDTO.setType(model.getType());
		
		SppotiEntity sppotiEntity = null;
		if (model.getRelatedSppotiId() != null) {
			sppotiEntity = this.sppotiRepository.findOne(model.getRelatedSppotiId());
		}
		
		teamDTO.setTeamAdmin(this.teamMemberTransformer.modelToDto(this.teamMembersRepository
				.findByTeamUuidAndStatusNotAndAdminTrueAndTeamDeletedFalse(model.getUuid(),
						GlobalAppStatusEnum.DELETED), sppotiEntity));
		
		final SppotiEntity finalSppotiEntity = sppotiEntity;
		teamDTO.setMembers(
				model.getTeamMembers().stream().map(m -> this.teamMemberTransformer.modelToDto(m, finalSppotiEntity))
						.collect(Collectors.toList()));
		
		return teamDTO;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TeamEntity dtoToModel(final TeamDTO dto)
	{
		final TeamEntity entity = new TeamEntity();
		
		if (dto.getId() != null) {
			entity.setUuid(dto.getId());
		}
		if (dto.getVersion() != null) {
			entity.setVersion(dto.getVersion());
		}
		
		entity.setName(dto.getName());
		entity.setCoverPath(dto.getCoverPath());
		entity.setLogoPath(dto.getLogoPath());
		entity.setTimeZone(dto.getTimeZone());
		entity.setSport(this.sportRepository.getById(dto.getSportId()));
		
		return entity;
	}
	
}
