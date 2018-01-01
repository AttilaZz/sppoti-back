package com.fr.transformers.impl;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.dto.team.TeamDTO;
import com.fr.commons.utils.SppotiBeanUtils;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.*;
import com.fr.repositories.SportRepository;
import com.fr.repositories.SppoterRepository;
import com.fr.repositories.TeamMembersRepository;
import com.fr.repositories.UserRepository;
import com.fr.transformers.ScoreTransformer;
import com.fr.transformers.SppotiTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.fr.commons.utils.SppotiUtils.statusToFilter;

/**
 * Created by djenanewail on 3/18/17.
 */
@Component
public class SppotiTransformerImpl extends AbstractTransformerImpl<SppotiDTO, SppotiEntity> implements SppotiTransformer
{
	@Autowired
	private ScoreTransformer scoreTransformer;
	@Autowired
	private SportRepository sportRepository;
	@Autowired
	private TeamTransformerImpl teamTransformer;
	@Autowired
	private SportTransformer sportTransformer;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TeamMemberTransformerImpl teamMemberTransformer;
	@Autowired
	private TeamMembersRepository teamMembersRepository;
	@Autowired
	private SppoterRepository sppoterRepository;
	
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
		
		
		final TeamDTO teamHostResponse = fillTeamResponse(model.getTeamHostEntity(), model);
		
		dto.setTeamHost(teamHostResponse);
		dto.setId(model.getUuid());
		dto.setRelatedSport(this.sportTransformer.modelToDto(model.getSport()));
		
		final List<SppoterEntity> sppotiMembers = this.sppoterRepository
				.findByTeamMemberUserUuidAndSppotiSportIdAndStatusNotInAndSppotiDeletedFalse(
						model.getUserSppoti().getUuid(), model.getSport().getId(), statusToFilter());
		
		dto.setSppotiCounter(sppotiMembers.size());
		dto.setMySppoti(Objects.equals(model.getConnectedUserId(), model.getUserSppoti().getId()));
		
		//if user is member of a team, get admin of the tem and other informations.
		final TeamMemberEntity teamAdmin = this.teamMembersRepository
				.findByUserUuidAndTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(model.getUserSppoti().getUuid(),
						model.getTeamHostEntity().getUuid(), statusToFilter());
		
		if (teamAdmin != null) {
			dto.setAdminTeamId(teamAdmin.getUuid());
			dto.setAdminUserId(model.getUserSppoti().getUuid());
		}
		
		return dto;
	}
	
	private TeamDTO fillTeamResponse(final TeamEntity team, final SppotiEntity sppoti)
	{
		
		final List<UserDTO> teamUsers = new ArrayList<>();
		
		for (final TeamMemberEntity memberEntity : team.getTeamMembers()) {
			/**
			 * If team member is not deleted && Sppoter Not deleted too, add sppoter to the list of members.
			 */
			if (memberEntity.getStatus().isNotCancelledAndNotDeletedAndNotRefused() &&
					memberEntity.getSppotiMembers().stream().anyMatch(
							s -> s.getSppoti().getUuid().equals(sppoti.getUuid()) &&
									s.getStatus().isNotCancelledAndNotDeletedAndNotRefused())) {
				
				teamUsers.add(this.teamMemberTransformer.modelToDto(memberEntity, sppoti));
				
			}
		}
		
		final TeamDTO teamDTO = this.teamTransformer.modelToDto(team);
		teamDTO.setMembers(teamUsers);
		
		return teamDTO;
		
	}
	
}