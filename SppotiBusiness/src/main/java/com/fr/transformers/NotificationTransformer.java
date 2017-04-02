package com.fr.transformers;

import com.fr.commons.dto.notification.NotificationDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.NotificationEntity;
import com.fr.entities.SppotiEntity;
import com.fr.entities.TeamEntity;
import com.fr.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by djenanewail on 2/19/17.
 */
@Transactional(readOnly = true)
@Component
public class NotificationTransformer {

    private final TeamTransformer teamTransformer;
    private final UserTransformer userTransformer;

    @Autowired
    public NotificationTransformer(TeamTransformer teamTransformer, UserTransformer userTransformer) {
        this.teamTransformer = teamTransformer;
        this.userTransformer = userTransformer;
    }

    /**
     * @param notification notification entity to map.
     * @return NotificationEntity DTO.
     */
    public NotificationDTO notificationEntityToDto(NotificationEntity notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notification.getUuid());
        notificationDTO.setDatetime(notification.getCreationDate());
        notificationDTO.setFrom(notificationUserEntityToDto(notification.getFrom()));
        notificationDTO.setTo(notificationUserEntityToDto(notification.getTo()));
        notificationDTO.setNotificationType(notification.getNotificationType().getNotifType());
        notificationDTO.setOpened(notification.isOpened());

        Optional<TeamEntity> optionalTeam = Optional.ofNullable(notification.getTeam());
        optionalTeam.ifPresent(t -> notificationDTO.setTeamResponseDTO(teamTransformer.modelToDto(notification.getTeam())));

        Optional<SppotiEntity> optionalSppoti = Optional.ofNullable(notification.getSppoti());
        optionalSppoti.ifPresent(t -> notificationDTO.setSppotiResponseDTO(SppotiTransformer.entityToDto(notification.getSppoti())));

        return notificationDTO;
    }

    /**
     * @param userEntity user entity to map.
     * @return user DTO used in notifications.
     */
    public UserDTO notificationUserEntityToDto(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO(), resourceUserDto = userTransformer.getUserCoverAndAvatar(userEntity);
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        userDTO.setEmail(userDTO.getEmail());
        userDTO.setUsername(userEntity.getUsername());

        userDTO.setAvatar(resourceUserDto.getAvatar());
        userDTO.setCover(resourceUserDto.getCover());
        userDTO.setCoverType(resourceUserDto.getCover() != null ? resourceUserDto.getCoverType() : null);

//        userDTO.setBirthDate(userEntity.getDateBorn());

        return userDTO;
    }

}
