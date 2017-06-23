package com.fr.impl;

import com.fr.commons.dto.search.GlobalSearchResultDTO;
import com.fr.service.GlobalSearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by wdjenane on 22/06/2017.
 */
@Component
public class GlobalSearchServiceImpl extends AbstractControllerServiceImpl implements GlobalSearchService {

    /** Friend list size. */
    @Value("${key.key.globalSearchPerPage}")
    private int searchListSize;

    /**
     * {@inheritDoc}
     */
    @Override
    public GlobalSearchResultDTO findAllUsersFromCriteria(final String query, final String sex, final int ageMax,
                                                          final int ageMin, final int page) {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GlobalSearchResultDTO findAllTeamFromCriteria(final String query, final int sport, final int page) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GlobalSearchResultDTO findAllSppotisFromCriteria(final String query, final int sport, final Date startDate,
                                                            final int page) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GlobalSearchResultDTO findAllWithoutCriteria(final String query, final int page) {
        return null;
    }

    private Pageable getPage(final int page) {
        return new PageRequest(page, this.searchListSize);
    }
}