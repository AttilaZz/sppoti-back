package com.fr.transformers.impl;

import com.fr.commons.dto.RatingDTO;
import com.fr.entities.RatingEntity;
import com.fr.transformers.RatingTransformer;
import org.springframework.stereotype.Component;

/**
 * Created by djenanewail on 9/12/17.
 */
@Component
public class RatingTransformerImpl extends AbstractTransformerImpl<RatingDTO, RatingEntity> implements RatingTransformer
{
}
