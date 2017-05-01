package com.fr.transformers;

import com.fr.commons.dto.team.TeamDTO;
import com.fr.entities.TeamEntity;
import org.springframework.stereotype.Service;

/**
 * Created by djenanewail on 4/9/17.
 */
@Service
public interface TeamTransformer extends CommonTransformer<TeamDTO, TeamEntity>
{
}
