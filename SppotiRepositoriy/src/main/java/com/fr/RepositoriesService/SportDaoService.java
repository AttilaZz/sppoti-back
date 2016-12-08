package com.fr.RepositoriesService;

import org.springframework.stereotype.Service;

import com.fr.pojos.Sport;

/**
 * Created by: Wail DJENANE on May 29, 2016
 */
@Service("sportService")
public interface SportDaoService extends GenericDaoService<Sport, Integer> {

    Sport getSportById(Long id);

}
