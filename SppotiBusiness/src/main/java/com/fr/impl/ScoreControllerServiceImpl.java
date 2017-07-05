package com.fr.impl;

import com.fr.commons.dto.ScoreDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.NotificationTypeEnum;
import com.fr.commons.enumeration.SppotiStatus;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.ScoreEntity;
import com.fr.entities.SppotiAdverseEntity;
import com.fr.entities.TeamEntity;
import com.fr.repositories.ScoreRepository;
import com.fr.service.ScoreControllerService;
import com.fr.transformers.ScoreTransformer;
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
	
	/** Init all dependencies. */
	@Autowired
	public ScoreControllerServiceImpl(final ScoreTransformer scoreTransformer, final ScoreRepository scoreRepository)
	{
		this.scoreTransformer = scoreTransformer;
		this.scoreRepository = scoreRepository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("ConstantConditions")
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
			
			NotificationTypeEnum typeEnum = NotificationTypeEnum.SCORE_HAS_BEEN_APPROVED;
			if (scoreDTO.getStatus().equals(GlobalAppStatusEnum.REFUSED.name())) {
				typeEnum = NotificationTypeEnum.SCORE_HAS_BEEN_REFUSED;
			}
			
			final Optional<TeamEntity> challengedTeam = s.getSppotiEntity().getAdverseTeams().stream()
					.filter(a -> a.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)).map(SppotiAdverseEntity::getTeam)
					.findFirst();
			
			addNotification(typeEnum, this.teamMembersRepository
							.findByTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(challengedTeam.get().getUuid(),
									SppotiUtils.statusToFilter()).getUser(), s.getSppotiEntity().getUserSppoti(),
					challengedTeam.get(), s.getSppotiEntity(), null, null, s, null);
			
			s.setScoreStatus(GlobalAppStatusEnum.valueOf(scoreDTO.getStatus()));
			this.scoreRepository.save(s);
		});
		
		scoreEntity.orElseThrow(() -> new EntityNotFoundException("Sppoti has no score yet! "));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("ConstantConditions")
	@Override
	@Transactional
	public ScoreDTO addSppotiScore(final ScoreDTO scoreDTO)
	{
		final ScoreEntity scoreEntity = this.scoreTransformer.dtoToModel(scoreDTO);
		scoreEntity.getSppotiEntity().setStatus(SppotiStatus.DONE);
		
		final Optional<TeamEntity> challengedTeam = scoreEntity.getSppotiEntity().getAdverseTeams().stream()
				.filter(a -> a.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)).map(SppotiAdverseEntity::getTeam)
				.findFirst();
		
		addNotification(NotificationTypeEnum.SCORE_SET_AND_WAITING_FOR_APPROVAL,
				scoreEntity.getSppotiEntity().getUserSppoti(), this.teamMembersRepository
						.findByTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(challengedTeam.get().getUuid(),
								SppotiUtils.statusToFilter()).getUser(),
				scoreEntity.getSppotiEntity().getTeamHostEntity(), scoreEntity.getSppotiEntity(), null, null,
				scoreEntity, null);
		
		return this.scoreTransformer.modelToDto(this.scoreRepository.save(scoreEntity));
	}
}