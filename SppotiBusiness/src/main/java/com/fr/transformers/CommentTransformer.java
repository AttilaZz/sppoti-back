package com.fr.transformers;

import com.fr.commons.dto.CommentDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by djenanewail on 2/11/17.
 */

@Component
@Transactional(readOnly = true)
public class CommentTransformer {


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

        commentDTO.setImageLink(commentEntity.getImageLink());
        commentDTO.setVideoLink(commentEntity.getVideoLink());
        commentDTO.setCreationDate(commentEntity.getDatetimeCreated());

        return commentDTO;
    }

}
