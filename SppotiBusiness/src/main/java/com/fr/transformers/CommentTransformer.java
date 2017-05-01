package com.fr.transformers;

import com.fr.commons.dto.CommentDTO;
import com.fr.entities.CommentEntity;
import org.springframework.stereotype.Service;

/**
 * Created by djenanewail on 4/8/17.
 */
@Service
public interface CommentTransformer extends CommonTransformer<CommentDTO, CommentEntity>
{
}
