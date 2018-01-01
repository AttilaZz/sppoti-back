package com.fr.transformers.impl;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.RatingEntity;
import com.fr.entities.SppoterEntity;
import com.fr.entities.SppotiEntity;
import com.fr.entities.TeamMemberEntity;
import com.fr.repositories.RatingRepository;
import com.fr.repositories.SppoterRepository;
import com.fr.transformers.TeamMemberTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by djenanewail on 2/25/17.
 */
@Transactional(readOnly = true)
@Component
public class TeamMemberTransformerImpl implements TeamMemberTransformer
{
	private final Logger LOGGER = LoggerFactory.getLogger(TeamMemberTransformerImpl.class);
	
	@Autowired
	private RatingRepository ratingRepository;
	@Autowired
	private SppoterRepository sppoterRepository;
	@Autowired
	private UserTransformerImpl userTransformer;
	
	@Override
	public List<UserDTO> modelToDto(final Set<TeamMemberEntity> memberEntity, final SppotiEntity sppoti)
	{
		return memberEntity.stream().map(m -> this.modelToDto(m, sppoti)).collect(Collectors.toList());
	}
	
	@Override
	public UserDTO modelToDto(final TeamMemberEntity memberEntity, final SppotiEntity sppoti)
	{
		if (Objects.isNull(memberEntity.getUser())) {
			this.LOGGER.info("Missing link to user table from team member {}", memberEntity.getId());
			return null;
		}
		
		final UserDTO userCoverAndAvatar = this.userTransformer.getUserCoverAndAvatar(memberEntity.getUser());
		
		final String sppotiId = sppoti != null ? sppoti.getUuid() : "";
		
		final UserDTO dto = new UserDTO();
		dto.setUserId(memberEntity.getUser().getUuid());
		dto.setFirstName(memberEntity.getUser().getFirstName());
		dto.setLastName(memberEntity.getUser().getLastName());
		dto.setUsername(memberEntity.getUser().getUsername());
		dto.setEmail(memberEntity.getUser().getEmail());
		dto.setAvatar(userCoverAndAvatar.getAvatar());
		dto.setCover(userCoverAndAvatar.getCover());
		dto.setCoverType(userCoverAndAvatar.getCoverType());
		dto.setWaitConfirmation(memberEntity.getRequestSentFromUser());
		
		dto.setId(memberEntity.getUuid());
		dto.setTeamAdmin(memberEntity.getAdmin());
		dto.setTeamStatus(memberEntity.getStatus());
		dto.setTeamCaptain(memberEntity.getTeamCaptain());
		dto.setxPosition(memberEntity.getxPosition());
		dto.setyPosition(memberEntity.getyPosition());
		dto.setLanguage(memberEntity.getUser().getLanguageEnum().name());
		
		dto.setSportDTOs(null);
		
		if (Objects.nonNull(memberEntity.getUser().getParamEntity())) {
			dto.setCanReceiveEmails(memberEntity.getUser().getParamEntity().isCanReceiveEmail());
			dto.setCanReceiveNotifications(memberEntity.getUser().getParamEntity().isCanReceiveNotification());
		}
		
		if (sppoti != null) {
			//get status for the selected sppoti
			if (!StringUtils.isEmpty(memberEntity.getSppotiMembers())) {
				for (final SppoterEntity sppoter : memberEntity.getSppotiMembers()) {
					if (sppoter.getTeamMember().getId().equals(memberEntity.getId()) &&
							sppoter.getSppoti().getId().equals(sppoti.getId()) &&
							!sppoter.getStatus().equals(GlobalAppStatusEnum.DELETED)) {
						dto.setSppotiStatus(sppoter.getStatus());
					}
				}
			}
			
			//Is sppoti admin.
			if (memberEntity.getUser().getId().equals(sppoti.getUserSppoti().getId())) {
				dto.setSppotiAdmin(Boolean.TRUE);
			} else {
				dto.setSppotiAdmin(Boolean.FALSE);
			}
			
			dto.setRating(getRatingStars(memberEntity.getUser().getUuid(), sppotiId));
			
			final Optional<SppoterEntity> optional = Optional.ofNullable(this.sppoterRepository
					.findByTeamMemberUserUuidAndSppotiUuidAndStatusNotInAndSppotiDeletedFalse(
							memberEntity.getUser().getUuid(), sppotiId, SppotiUtils.statusToFilter()));
			optional.ifPresent(sm -> dto.setHasRateOtherSppoters(sm.getHasRateOtherSppoter()));
			
		}
		
		return dto;
	}
	
	/**
	 * Get sppoter rating from it's user id.
	 *
	 * @param userId
	 * 		sppoter as user id.
	 *
	 * @return rating stars.
	 */
	@Transactional
	private Double getRatingStars(final String userId, final String sppotiId)
	{
		
		final Optional<Set<RatingEntity>> sppotiRatingEntity = this.ratingRepository
				.findByRatedSppoterUuidAndSppotiEntityUuid(userId, sppotiId);
		
		if (sppotiRatingEntity.isPresent()) {
			final Set<RatingEntity> sppotiRatingEntities = new HashSet<>();
			sppotiRatingEntities.addAll(sppotiRatingEntity.get());
			final OptionalDouble averageRating = sppotiRatingEntities.stream().filter(r -> r.getStars() != 0)
					.mapToDouble(RatingEntity::getStars).average();
			
			if (averageRating.isPresent()) {
				return averageRating.getAsDouble();
			}
		}
		return 0D;
	}
	
}