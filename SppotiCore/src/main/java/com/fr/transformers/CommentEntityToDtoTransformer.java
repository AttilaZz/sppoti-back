package com.fr.transformers;

import com.fr.commons.dto.CommentDTO;
import com.fr.commons.dto.UserDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.ResourcesEntity;
import com.fr.entities.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by djenanewail on 2/11/17.
 */

@Component
@Transactional(readOnly = true)
public class CommentEntityToDtoTransformer {


    /**
     * @param commentEntity like entity to map.
     * @param userEntity user entity to map.
     * @return like dto.
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

}
