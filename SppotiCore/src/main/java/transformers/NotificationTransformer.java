package transformers;

import com.fr.commons.dto.notification.NotificationDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.NotificationEntity;
import com.fr.entities.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import static transformers.EntityToDtoTransformer.getUserCoverAndAvatar;

/**
 * Created by djenanewail on 2/19/17.
 */
@Transactional(readOnly = true)
public class NotificationTransformer {

    /**
     * @param notification notification entity to map.
     * @return NotificationEntity DTO.
     */
    public static NotificationDTO notificationEntityToDto(NotificationEntity notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notification.getUuid());
        notificationDTO.setDatetime(notification.getCreationDate());
        notificationDTO.setFrom(notificationUserEntityToDto(notification.getFrom()));
        notificationDTO.setTo(notificationUserEntityToDto(notification.getTo()));
        notificationDTO.setNotificationType(notification.getNotificationType().getNotifType());
        notificationDTO.setOpened(notification.isOpened());

        if (notification.getTeam() != null) {
            TeamTransformer.teamEntityToDto(notification.getTeam());
        }

        return notificationDTO;
    }

    /**
     * @param userEntity user entity to map.
     * @return user DTO used in notifications.
     */
    public static UserDTO notificationUserEntityToDto(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO(), resourceUserDto = getUserCoverAndAvatar(userEntity);
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        userDTO.setEmail(userDTO.getEmail());
        userDTO.setUsername(userEntity.getUsername());

        userDTO.setAvatar(resourceUserDto.getAvatar() != null ? resourceUserDto.getAvatar() : null);
        userDTO.setCover(resourceUserDto.getCover() != null ? resourceUserDto.getCover() : null);
        userDTO.setCoverType(resourceUserDto.getCover() != null ? resourceUserDto.getCoverType() : null);

//        userDTO.setBirthDate(userEntity.getDateBorn());

        return userDTO;
    }

}
