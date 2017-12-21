package com.fr.impl;

import com.fr.commons.dto.ScoreDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.enumeration.SppotiStatus;
import com.fr.commons.enumeration.notification.NotificationObjectType;
import com.fr.commons.enumeration.notification.NotificationTypeEnum;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.*;
import com.fr.repositories.ScoreRepository;
import com.fr.service.NotificationBusinessService;
import com.fr.service.ScoreBusinessService;
import com.fr.service.email.ScoreMailerService;
import com.fr.transformers.ScoreTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 4/7/17.
 */
@Component
public class ScoreBusinessServiceImpl extends CommonControllerServiceImpl implements ScoreBusinessService
{
	
	@Autowired
	private ScoreTransformer scoreTransformer;
	@Autowired
	private ScoreRepository scoreRepository;
	@Autowired
	private NotificationBusinessService notificationService;
	@Autowired
	private ScoreMailerService scoreMailerService;
	
	/**
	 * {@inheritDoc}
	 */
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
			
			final TeamMemberEntity teamAdverseAdmin = this.teamMembersRepository
					.findByTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(challengedTeam.get().getUuid(),
							SppotiUtils.statusToFilter());
			
			score.setScoreStatus(GlobalAppStatusEnum.valueOf(scoreDTO.getStatus()));
			this.scoreRepository.save(score);
			
			this.notificationService
					.saveAndSendNotificationToUsers(teamAdverseAdmin.getUser(), score.getSppotiEntity().getUserSppoti(),
							NotificationObjectType.SCORE, notificationType, challengedTeam.get(),
							score.getSppotiEntity(), score);
			
			if (notificationType.isScoreHasBeenApproved()) {
				this.scoreMailerService.sendAcceptScoreEmail(scoreDTO, teamAdverseAdmin.getUser(),
						buildSppotiMemberMailingList(score.getSppotiEntity()));
			} else if (notificationType.isScoreHasBeenRefused()) {
				this.scoreMailerService.sendRefuseScoreEmail(scoreDTO, teamAdverseAdmin.getUser(),
						buildSppotiMemberMailingList(score.getSppotiEntity()));
			}
			
		});
		
		scoreEntity.orElseThrow(() -> new EntityNotFoundException("Sppoti has no score yet! "));
	}
	
	private List<UserEntity> buildSppotiMemberMailingList(final SppotiEntity sppotiEntity) {
		return sppotiEntity.getSppotiMembers().stream().map(s -> s.getTeamMember().getUser())
				.collect(Collectors.toList());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("ConstantConditions")
	@Override
	@Transactional
	public ScoreDTO addScoreToSppoti(final ScoreDTO scoreDTO)
	{
		final ScoreEntity scoreEntity = this.scoreTransformer.dtoToModel(scoreDTO);
		scoreEntity.getSppotiEntity().setStatus(SppotiStatus.DONE);
		
		final Optional<TeamEntity> challengedTeam = scoreEntity.getSppotiEntity().getAdverseTeams().stream()
				.filter(a -> a.getStatus().equals(GlobalAppStatusEnum.CONFIRMED)).map(SppotiAdverseEntity::getTeam)
				.findFirst();
		
		final TeamMemberEntity teamAdverseAdmin = this.teamMembersRepository
				.findByTeamUuidAndStatusNotInAndAdminTrueAndTeamDeletedFalse(challengedTeam.get().getUuid(),
						SppotiUtils.statusToFilter());
		
		this.notificationService.saveAndSendNotificationToUsers(scoreEntity.getSppotiEntity().getUserSppoti(),
				teamAdverseAdmin.getUser(), NotificationObjectType.SCORE,
				NotificationTypeEnum.SCORE_SET_AND_WAITING_FOR_APPROVAL,
				scoreEntity.getSppotiEntity().getTeamHostEntity(), scoreEntity.getSppotiEntity(), scoreEntity);
		
		this.scoreMailerService
				.sendAddScoreEmail(scoreDTO, teamAdverseAdmin.getUser(), scoreEntity.getSppotiEntity().getUserSppoti());
		
		return this.scoreTransformer.modelToDto(this.scoreRepository.save(scoreEntity));
	}
}