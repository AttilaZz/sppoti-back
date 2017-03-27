package com.fr.service;

import com.fr.entities.SportEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by djenanewail on 1/22/17.
 */

@Component
public interface SportControllerService extends AbstractControllerService{

    List<SportEntity> getAllSports();

}