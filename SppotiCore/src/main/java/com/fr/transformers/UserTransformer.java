package com.fr.transformers;

import com.fr.commons.dto.UserDTO;
import com.fr.entities.ResourcesEntity;
import com.fr.entities.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by djenanewail on 3/18/17.
 */
@Component
@Transactional
public class UserTransformer {

    /**
     *
     * @param entity user entity to transform.
     * @return user DTO.
     */
    public UserDTO entityToDto(UserEntity entity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(entity.getUuid());
        userDTO.setFirstName(entity.getFirstName());
        userDTO.setLastName(entity.getLastName());
        userDTO.setUsername(entity.getUsername());

        UserDTO userRessources = getUserCoverAndAvatar(entity);

        userDTO.setAvatar(userRessources.getAvatar());
        userDTO.setCover(userRessources.getCover());
        userDTO.setCoverType(userRessources.getCoverType());

        return userDTO;
    }

    /**
     * @param targetUser user entity to map.
     * @return userDto with only avatar and cover.
     */
    public static UserDTO getUserCoverAndAvatar(UserEntity targetUser) {

        UserDTO user = new UserDTO();
        Set<ResourcesEntity> resources = targetUser.getResources();

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
