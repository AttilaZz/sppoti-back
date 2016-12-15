package com.fr.repositoriesImpl;

import org.springframework.stereotype.Component;

import com.fr.RepositoriesService.GameDaoService;
import com.fr.entities.Sppoti;

/**
 * Created by: Wail DJENANE on Jul 3, 2016
 */
@Component
public class GameDaoImpl extends GenericDaoImpl<Sppoti, Integer> implements GameDaoService {

    public GameDaoImpl() {
        this.entityClass = Sppoti.class;
    }
}
