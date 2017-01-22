package com.fr.core;

import com.fr.controllers.service.SportControllerService;
import com.fr.entities.Sport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */
@Component
public class SportControllerServiceImpl extends AbstractControllerServiceImpl implements SportControllerService {
    @Override
    public List<Sport> getAllSports() {
        return sportRepository.findAll();
    }
}
