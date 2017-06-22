package com.fr.impl;

import com.fr.commons.dto.search.GlobalSearchDTO;
import com.fr.commons.dto.search.GlobalSearchResultDTO;
import com.fr.service.GlobalSearchService;
import org.springframework.stereotype.Component;

/**
 * Created by wdjenane on 22/06/2017.
 */
@Component
public class GlobalSearchServiceImpl extends AbstractControllerServiceImpl implements GlobalSearchService {

    @Override
    public GlobalSearchResultDTO findUsersAndTeamsAndSppotisByCriteria(GlobalSearchDTO globalSearchDTO) {
        return null;
    }
}
