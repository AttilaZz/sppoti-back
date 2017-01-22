package com.fr.controllers.service;

import com.fr.entities.Sport;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */
public interface SportControllerService extends AbstractControllerService{

    List<Sport> getAllSports();

}