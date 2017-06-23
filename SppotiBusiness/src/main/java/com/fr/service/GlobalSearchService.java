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

    GlobalSearchResultDTO findAllUsersFromCriteria(String query, String sexe, int ageMax, int ageMin, int page);

    GlobalSearchResultDTO findAllTeamFromCriteria(String query, int sport, int page);

    GlobalSearchResultDTO findAllSppotisFromCriteria(String query, int sport, Date startDate, int page);

    GlobalSearchResultDTO findAllWithoutCriteria(String query, int page);
}
