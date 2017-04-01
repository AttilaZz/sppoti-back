package com.fr.transformers;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.SppotiEntity;
import com.fr.entities.SppotiMemberEntity;
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
public class TeamMemberTransformer {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private SppotiMembersRepository sppotiMembersRepository;

    /**
     * Transform a team member entity to userDTO.
     * <p>
     * Used to create sppoter or only team member.
     *
     * @return all team member information inside a DTO.
     */
    public UserDTO modelToDto(TeamMemberEntity memberEntity, SppotiEntity sppoti) {

        UserDTO userCoverAndAvatar = UserTransformer.getUserCoverAndAvatar(memberEntity.getUsers());

        Integer sppotiId = sppoti != null ? sppoti.getUuid() : 0;

        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(memberEntity.getUsers().getUuid());
        userDTO.setFirstName(memberEntity.getUsers().getFirstName());
        userDTO.setLastName(memberEntity.getUsers().getLastName());
        userDTO.setUsername(memberEntity.getUsers().getUsername());
        userDTO.setAvatar(userCoverAndAvatar.getCover());
        userDTO.setCover(userCoverAndAvatar.getAvatar());
        userDTO.setCoverType(userCoverAndAvatar.getCoverType());

        userDTO.setId(memberEntity.getUuid());//team member id.
        userDTO.setTeamAdmin(memberEntity.getAdmin());
        userDTO.setTeamStatus(memberEntity.getStatus().getValue());
        userDTO.setTeamCaptain(memberEntity.getTeamCaptain());
        userDTO.setxPosition(memberEntity.getxPosition());
        userDTO.setyPosition(memberEntity.getyPosition());

        if (sppoti != null) {
            //get status for the selected sppoti
            if (!StringUtils.isEmpty(memberEntity.getSppotiMembers())) {
                for (SppotiMemberEntity sppoter : memberEntity.getSppotiMembers()) {
                    if (sppoter.getTeamMember().getId().equals(memberEntity.getId()) && sppoter.getSppoti().getId().equals(sppoti.getId())) {
                        userDTO.setSppotiStatus(sppoter.getStatus().getValue());
                    }
                }
            }

            //Is sppoti admin.
            if (memberEntity.getUsers().getId().equals(sppoti.getUserSppoti().getId())) {
                userDTO.setSppotiAdmin(Boolean.TRUE);
            }else{
                userDTO.setSppotiAdmin(Boolean.FALSE);
            }

            userDTO.setRating(getRatingStars(memberEntity.getUsers().getUuid(), sppotiId));
        }

        Optional<SppotiMemberEntity> optional = Optional.ofNullable(sppotiMembersRepository.findByTeamMemberUsersUuidAndSppotiUuid(memberEntity.getUsers().getUuid(), sppotiId));
        optional.ifPresent(sm -> userDTO.setHasRateOtherSppoters(sm.getHasRateOtherSppoter()));

        return userDTO;
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