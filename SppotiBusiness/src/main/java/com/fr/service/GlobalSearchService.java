package com.fr.service;

import com.fr.commons.dto.search.GlobalSearchResultDTO;

import java.util.Date;

/**
 * Global app search:
 * USERS, TEAMS, SPPOTIS
 *
 * Created by wdjenane on 22/06/2017.
 */
public interface GlobalSearchService extends AbstractControllerService{

    GlobalSearchResultDTO findAllUsersFromCriteria(String query, String sexe, Integer ageMax, Integer ageMin,
                                                   Integer page);

    GlobalSearchResultDTO findAllTeamFromCriteria(String query, Integer sport, Integer page);

    GlobalSearchResultDTO findAllSppotisFromCriteria(String query, Integer sport, Date startDate, Integer page);

    GlobalSearchResultDTO findAllWithoutCriteria(String query, Integer page);
}
