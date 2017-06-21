package com.fr.transformers.impl;

import com.fr.commons.dto.UserDTO;
import com.fr.commons.enumeration.GlobalAppStatusEnum;
import com.fr.entities.SppoterEntity;
import com.fr.entities.SppotiEntity;
import com.fr.entities.SppotiRatingEntity;
import com.fr.entities.TeamMemberEntity;
import com.fr.repositories.RatingRepository;
import com.fr.repositories.SppotiMembersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;

/**
 * Created by djenanewail on 2/25/17.
 */
@Transactional(readOnly = true)
@Component
public class TeamMemberTransformer
{
	
	/** Rating Repository. */
	private final RatingRepository ratingRepository;
	
	/** Sppoti member repository. */
	private final SppotiMembersRepository sppotiMembersRepository;
	
	/** User transformer. */
	private final UserTransformerImpl userTransformer;
	
	/** Init dependencies. */
	@Autowired
	public TeamMemberTransformer(final RatingRepository ratingRepository,
								 final SppotiMembersRepository sppotiMembersRepository,
								 final UserTransformerImpl userTransformer)
	{
		this.ratingRepository = ratingRepository;
		this.sppotiMembersRepository = sppotiMembersRepository;
		this.userTransformer = userTransformer;
	}
	
	/**
	 * Transform a team member entity to userDTO.
	 * <p>
	 * Used to create sppoter or only team member.
	 *
	 * @return all team member information inside a DTO.
	 */
	public UserDTO modelToDto(final TeamMemberEntity memberEntity, final SppotiEntity sppoti)
	{
		
		final UserDTO userCoverAndAvatar = this.userTransformer.getUserCoverAndAvatar(memberEntity.getUser());
		
		final String sppotiId = sppoti != null ? sppoti.getUuid() : "";
		
		final UserDTO userDTO = new UserDTO();
		userDTO.setUserId(memberEntity.getUser().getUuid());
		userDTO.setFirstName(memberEntity.getUser().getFirstName());
		userDTO.setLastName(memberEntity.getUser().getLastName());
		userDTO.setUsername(memberEntity.getUser().getUsername());
		userDTO.setAvatar(userCoverAndAvatar.getAvatar());
		userDTO.setCover(userCoverAndAvatar.getCover());
		userDTO.setCoverType(userCoverAndAvatar.getCoverType());
		
		userDTO.setId(memberEntity.getUuid());
		userDTO.setTeamAdmin(memberEntity.getAdmin());
		userDTO.setTeamStatus(memberEntity.getStatus().getValue());
		userDTO.setTeamCaptain(memberEntity.getTeamCaptain());
		userDTO.setxPosition(memberEntity.getxPosition());
		userDTO.setyPosition(memberEntity.getyPosition());
		
		if (sppoti != null) {
			//get status for the selected sppoti
			if (!StringUtils.isEmpty(memberEntity.getSppotiMembers())) {
				for (final SppoterEntity sppoter : memberEntity.getSppotiMembers()) {
					if (sppoter.getTeamMember().getId().equals(memberEntity.getId()) &&
							sppoter.getSppoti().getId().equals(sppoti.getId()) &&
							!sppoter.getStatus().equals(GlobalAppStatusEnum.DELETED)) {
						userDTO.setSppotiStatus(sppoter.getStatus().getValue());
					}
				}
			}
			
			//Is sppoti admin.
			if (memberEntity.getUser().getId().equals(sppoti.getUserSppoti().getId())) {
				userDTO.setSppotiAdmin(Boolean.TRUE);
			} else {
				userDTO.setSppotiAdmin(Boolean.FALSE);
			}
			
			userDTO.setRating(getRatingStars(memberEntity.getUser().getUuid(), sppotiId));
			
			final Optional<SppoterEntity> optional = Optional.ofNullable(this.sppotiMembersRepository
					.findByTeamMemberUserUuidAndSppotiUuidAndStatusNot(memberEntity.getUser().getUuid(), sppotiId,
							GlobalAppStatusEnum.DELETED));
			optional.ifPresent(sm -> userDTO.setHasRateOtherSppoters(sm.getHasRateOtherSppoter()));
			
		}
		
		return userDTO;
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
		
		final Optional<Set<SppotiRatingEntity>> sppotiRatingEntity = this.ratingRepository
				.findByRatedSppoterUuidAndSppotiEntityUuid(userId, sppotiId);
		
		if (sppotiRatingEntity.isPresent()) {
			final Set<SppotiRatingEntity> sppotiRatingEntities = new HashSet<>();
			sppotiRatingEntities.addAll(sppotiRatingEntity.get());
			final OptionalDouble averageRating = sppotiRatingEntities.stream().filter(r -> r.getStarsCount() != 0)
					.mapToDouble(SppotiRatingEntity::getStarsCount).average();
			
			if (averageRating.isPresent()) {
				return averageRating.getAsDouble();
			}
		}
		return 0D;
	}
	
}