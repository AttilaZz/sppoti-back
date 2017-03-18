package com.fr.transformers;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.SppotiEntity;
import com.fr.entities.SppotiRatingEntity;
import com.fr.entities.TeamMemberEntity;
import com.fr.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;

/**
 * Created by djenanewail on 2/25/17.
 */
@Transactional(readOnly = true)
@Component
public class TeamMemberTransformer {

    @Autowired
    private RatingRepository ratingRepository;

    /**
     * Transform a team member entity to userDTO.
     * <p>
     * Used to create sppoter or only team member.
     *
     * @return all team member information inside a DTO.
     */
    public UserDTO teamMemberEntityToDto(TeamMemberEntity memberEntity, SppotiEntity sppoti, Integer sppoterStatus) {

        UserDTO userCoverAndAvatar = UserTransformer.getUserCoverAndAvatar(memberEntity.getUsers());

        Integer sppotiId = sppoti != null ? sppoti.getUuid() : 0;

        return new UserDTO(memberEntity.getUuid(), memberEntity.getUsers().getFirstName(), memberEntity.getUsers().getLastName(), memberEntity.getUsers().getUsername(),
                userCoverAndAvatar.getCover() != null ? userCoverAndAvatar.getCover() : null,
                userCoverAndAvatar.getAvatar() != null ? userCoverAndAvatar.getAvatar() : null,
                userCoverAndAvatar.getCoverType() != null ? userCoverAndAvatar.getCoverType() : null,
                memberEntity.getAdmin(),
                sppoti != null && sppoti.getUserSppoti().getId() != null && memberEntity.getUsers().getId().equals(sppoti.getUserSppoti().getId()) ? true : null,
                memberEntity.getStatus().getValue(),
                sppoti != null && sppoti.getUserSppoti().getId() != null ? sppoterStatus : null, memberEntity.getUsers().getUuid(),
                memberEntity.getxPosition() != null ? memberEntity.getxPosition() : null,
                memberEntity.getyPosition() != null ? memberEntity.getyPosition() : null,
                memberEntity.getTeamCaptain() != null ? memberEntity.getTeamCaptain() : null,
                getRatingStars(memberEntity.getUsers().getUuid(), sppotiId));
    }

    /**
     * Get sppoter rating from it's user id.
     *
     * @param userId sppoter as user id.
     * @return rating stars.
     */
    @Transactional
    private Double getRatingStars(int userId, int sppotiId) {

        Optional<Set<SppotiRatingEntity>> sppotiRatingEntity = ratingRepository.findByRatedSppoterUuidAndSppotiEntityUuid(userId, sppotiId);

        if (sppotiRatingEntity.isPresent()) {
            Set<SppotiRatingEntity> sppotiRatingEntities = new HashSet<>();
            sppotiRatingEntities.addAll(sppotiRatingEntity.get());
            OptionalDouble averageRating = sppotiRatingEntities.stream()
                    .mapToDouble(
                            SppotiRatingEntity::getStarsCount
                    ).average();

            if (averageRating.isPresent()) {
                return averageRating.getAsDouble();
            }
        }
        return 0D;
    }

}