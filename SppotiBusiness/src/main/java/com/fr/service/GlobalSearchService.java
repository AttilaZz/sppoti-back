package com.fr.service;

import com.fr.commons.dto.search.GlobalSearchDTO;
import com.fr.commons.dto.search.GlobalSearchResultDTO;

/**
 * Global app search:
 * USERS, TEAMS, SPPOTIS
 *
 * Created by wdjenane on 22/06/2017.
 */
public interface GlobalSearchService extends AbstractControllerService{

    GlobalSearchResultDTO findUsersAndTeamsAndSppotisByCriteria(GlobalSearchDTO globalSearchDTO);
}
