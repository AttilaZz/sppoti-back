package com.fr.transformers;

import com.fr.commons.dto.post.PostDTO;
import com.fr.entities.PostEntity;
import org.springframework.stereotype.Service;

/**
 * Created by djenanewail on 6/10/17.
 */
@Service
public interface PostTransformer extends CommonTransformer<PostDTO, PostEntity>
{
}
