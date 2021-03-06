package com.fr.transformers;

import com.fr.commons.dto.ScoreDTO;
import com.fr.entities.ScoreEntity;
import org.springframework.stereotype.Service;

/**
 * Transformer interface for {@link ScoreEntity}.
 *
 * Created by wdjenane on 04/04/2017.
 */
public interface ScoreTransformer extends CommonTransformer<ScoreDTO, ScoreEntity>
{
}
