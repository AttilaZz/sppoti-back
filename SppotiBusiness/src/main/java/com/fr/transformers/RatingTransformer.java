package com.fr.transformers;

import com.fr.commons.dto.RatingDTO;
import com.fr.entities.RatingEntity;
import org.springframework.stereotype.Service;

/**
 * Created by djenanewail on 9/11/17.
 */
@Service
public interface RatingTransformer extends CommonTransformer<RatingDTO, RatingEntity>
{
}
