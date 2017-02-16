package com.fr.rest.service;

import com.fr.entities.SportEntity;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */
public interface SportControllerService extends AbstractControllerService{

    List<SportEntity> getAllSports();

}