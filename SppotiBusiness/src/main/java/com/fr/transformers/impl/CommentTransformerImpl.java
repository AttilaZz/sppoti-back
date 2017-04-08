package com.fr.transformers.impl;

import com.fr.commons.dto.CommentDTO;
import com.fr.entities.CommentEntity;
import com.fr.entities.UserEntity;
import com.fr.transformers.CommentTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by djenanewail on 2/11/17.
 */

@Component
@Transactional(readOnly = true)
public class CommentTransformerImpl extends AbstractTransformerImpl<CommentDTO, CommentEntity>
implements CommentTransformer {

    /** User transformer. */
    private final UserTransformer userTransformer;

    @Autowired
    public CommentTransformerImpl(UserTransformer userTransformer) {
        this.userTransformer = userTransformer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommentEntity dtoToModel(CommentDTO dto) {
        return super.dtoToModel(dto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommentDTO modelToDto(CommentEntity model) {
        CommentDTO commentDTO = new CommentDTO();
        UserEntity userEntity = model.getUser();
        commentDTO.setAuthorFirstName(userEntity.getFirstName());
        commentDTO.setAuthorLastName(userEntity.getLastName());
        commentDTO.setAuthorUsername(userEntity.getUsername());
        commentDTO.setId(model.getUuid());
        commentDTO.setAuthorUsername(userEntity.getUsername());
        commentDTO.setText(model.getContent());

        commentDTO.setImageLink(model.getImageLink());
        commentDTO.setVideoLink(model.getVideoLink());
        commentDTO.setCreationDate(model.getDatetimeCreated());

        commentDTO.setAuthorAvatar(userTransformer.getUserCoverAndAvatar(userEntity).getAvatar());

        return commentDTO;
    }

}
