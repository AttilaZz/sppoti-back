package com.fr.controllers.service;

import com.fr.commons.dto.TeamRequest;
import org.springframework.stereotype.Service;

/**
 * Created by djenanewail on 1/22/17.
 */

@Service
public interface TeamControllerService extends AbstractControllerService{

    void saveTeam(TeamRequest team, Long adminId);
}
