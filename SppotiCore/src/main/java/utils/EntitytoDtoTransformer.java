package utils;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.NotificationDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.Notification;
import com.fr.entities.Resources;
import com.fr.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by djenanewail on 2/11/17.
 */

@Component
public class EntitytoDtoTransformer {

    /**
     * @param notification
     * @return Notification DTO
     */
    public static NotificationDTO notificationEntityToDto(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setDatetime(notification.getCreationDate());
        notificationDTO.setFrom(notificationUserEntityToDto(notification.getFrom()));
        notificationDTO.setTo(notificationUserEntityToDto(notification.getTo()));
        notificationDTO.setNotificationType(notification.getNotificationType());

        return notificationDTO;
    }


    /**
     * @param userEntity
     * @return user DTO used in notifications
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

    /**
     *
     * @param commentEntity
     * @param userEntity
     * @return comment dto
     */
    public static CommentDTO commentEntityToDto(CommentEntity commentEntity, UserEntity userEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setAuthorFirstName(userEntity != null ? userEntity.getFirstName() : null);
        commentDTO.setAuthorLastName(userEntity != null ? userEntity.getLastName() : null);
        commentDTO.setAuthorUsername(userEntity != null ? userEntity.getUsername() : null);
        commentDTO.setId(commentEntity.getUuid());
        commentDTO.setAuthorUsername(userEntity != null ? userEntity.getUsername() : null);
        commentDTO.setText(commentEntity.getContent());
        commentDTO.setImageLink(commentEntity.getImageLink() != null ? commentEntity.getImageLink() : null);
        commentDTO.setVideoLink(commentEntity.getVideoLink() != null ? commentEntity.getVideoLink() : null);
        commentDTO.setCreationDate(commentEntity.getDatetimeCreated());

        return commentDTO;
    }

    /**
     * @param targetUser
     * @return userDto with only avatar and cover
     */
    public static UserDTO getUserCoverAndAvatar(UserEntity targetUser) {

        UserDTO user = new UserDTO();
        Set<Resources> resources = targetUser.getRessources();

        List<Resources> resources_temp = new ArrayList<Resources>();
        resources_temp.addAll(resources);

        if (!resources_temp.isEmpty()) {
            if (resources_temp.size() == 2) {
                //cover and avatar found
                Resources resource1 = resources_temp.get(0);
                Resources resource2 = resources_temp.get(1);

                if (resource1.getType() == 1 && resource2.getType() == 2) {
                    user.setAvatar(resource1.getUrl());

                    user.setCover(resource2.getUrl());
                    user.setCoverType(resource2.getTypeExtension());
                } else if (resource1.getType() == 2 && resource2.getType() == 1) {
                    user.setAvatar(resource2.getUrl());

                    user.setCover(resource1.getUrl());
                    user.setCoverType(resource1.getTypeExtension());
                }

            } else {
                // size is = 1 -> cover or avatar
                Resources resource = resources_temp.get(0);
                if (resource.getType() == 1) {//acatar
                    user.setAvatar(resource.getUrl());
                } else {
                    user.setCover(resource.getUrl());
                    user.setCoverType(resource.getTypeExtension());
                }
            }
        }

        return user;
    }

}
