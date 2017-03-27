package com.fr.service;

import com.fr.entities.SportEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */

@Service
public interface SportControllerService extends AbstractControllerService{

    List<SportEntity> getAllSports();

}