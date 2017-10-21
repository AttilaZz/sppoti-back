package com.fr.impl;

import com.fr.commons.dto.ScoreDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.SppotiStatus;
import com.fr.commons.enumeration.notification.NotificationObjectType;
import com.fr.commons.enumeration.notification.NotificationTypeEnum;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.ScoreEntity;
import com.fr.entities.SppotiAdverseEntity;
import com.fr.entities.TeamEntity;
import com.fr.entities.TeamMemberEntity;
import com.fr.repositories.ScoreRepository;
import com.fr.service.NotificationBusinessService;
import com.fr.service.ScoreBusinessService;
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
public class ScoreBusinessServiceImpl extends AbstractControllerServiceImpl implements ScoreBusinessService
{
	
	private final ScoreTransformer scoreTransformer;
	private final ScoreRepository scoreRepository;
	private final NotificationBusinessService notificationService;
	
	/** Init all dependencies. */
	@Autowired
	public ScoreBusinessServiceImpl(final ScoreTransformer scoreTransformer, final ScoreRepository scoreRepository,
									final NotificationBusinessService notificationService)
	{
		this.scoreTransformer = scoreTransformer;
		this.scoreRepository = scoreRepository;
		this.notificationService = notificationService;
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
		
		scoreEntity.ifPresent(score -> {
			
			NotificationTypeEnum notificationType = NotificationTypeEnum.SCORE_HAS_BEEN_APPROVED;
			if (scoreDTO.getStatus().equals(GlobalAppStatusEnum.REFUSED.name())) {
				notificationType = NotificationTypeEnum.SCORE_HAS_BEEN_REFUSED;
			}
			
			final Optional<TeamEntity> challengedTeam = score.getSppotiEntity().getAdverseTeams().stream()
					.filter(a -> a.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)).map(SppotiAdverseEntity::getTeam)
					.findFirst();
			
			final TeamMemberEntity sppotiTeamHost = this.teamMembersRepository
					.findByTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(challengedTeam.get().getUuid(),
							SppotiUtils.statusToFilter());
			
			this.notificationService
					.saveAndSendNotificationToUsers(sppotiTeamHost.getUser(), score.getSppotiEntity().getUserSppoti(),
							NotificationObjectType.SCORE, notificationType, challengedTeam.get(),
							score.getSppotiEntity(), score);
			
			score.setScoreStatus(GlobalAppStatusEnum.valueOf(scoreDTO.getStatus()));
			this.scoreRepository.save(score);
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
		
		final TeamMemberEntity sppotiTeamHost = this.teamMembersRepository
				.findByTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(challengedTeam.get().getUuid(),
						SppotiUtils.statusToFilter());
		
		this.notificationService
				.saveAndSendNotificationToUsers(scoreEntity.getSppotiEntity().getUserSppoti(), sppotiTeamHost.getUser(),
						NotificationObjectType.SCORE, NotificationTypeEnum.SCORE_SET_AND_WAITING_FOR_APPROVAL,
						scoreEntity.getSppotiEntity().getTeamHostEntity(), scoreEntity.getSppotiEntity(), null, null,
						scoreEntity);
		
		return this.scoreTransformer.modelToDto(this.scoreRepository.save(scoreEntity));
	}
}