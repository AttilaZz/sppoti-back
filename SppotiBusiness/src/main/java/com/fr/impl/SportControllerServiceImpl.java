package com.fr.impl;

import com.fr.entities.SportEntity;
import com.fr.service.SportControllerService;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */

public class SportControllerServiceImpl extends AbstractControllerServiceImpl implements SportControllerService {

    @Override
    public List<SportEntity> getAllSports() {
        return sportRepository.findAll();
    }
}
