package utils;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.ResourcesEntity;
import com.fr.entities.UserEntity;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by djenanewail on 2/11/17.
 */

@Component
public class EntitytoDtoTransformer {


    /**
     * @param commentEntity like entity to map.
     * @param userEntity user entity to map.
     * @return like dto.
     */
    @Transactional
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
     * @param targetUser user entity to map.
     * @return userDto with only avatar and cover.
     */
    @Transactional
    public static UserDTO getUserCoverAndAvatar(UserEntity targetUser) {

        UserDTO user = new UserDTO();
        Set<ResourcesEntity> resources = targetUser.getRessources();

        List<ResourcesEntity> resources_Entity_temp = new ArrayList<ResourcesEntity>();
        resources_Entity_temp.addAll(resources);

        if (!resources_Entity_temp.isEmpty()) {
            if (resources_Entity_temp.size() == 2) {
                //cover and avatar found
                ResourcesEntity resource1 = resources_Entity_temp.get(0);
                ResourcesEntity resource2 = resources_Entity_temp.get(1);

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
                ResourcesEntity resource = resources_Entity_temp.get(0);
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
