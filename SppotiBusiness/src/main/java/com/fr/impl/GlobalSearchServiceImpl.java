package com.fr.impl;

import com.fr.commons.dto.search.GlobalSearchResultDTO;
import com.fr.commons.enumeration.GenderEnum;
import com.fr.entities.UserEntity;
import com.fr.service.GlobalSearchService;
import com.fr.transformers.UserTransformer;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserTransformer userTransformer;

    /** Friend list size. */
    @Value("${key.key.globalSearchPerPage}")
    private Integer searchListSize;

    @Autowired
    public GlobalSearchServiceImpl(final UserTransformer userTransformer) {
        this.userTransformer = userTransformer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GlobalSearchResultDTO findAllUsersFromCriteria(final String query, final String sex, final Integer ageMax,
                                                          final Integer ageMin, final Integer page) {
        final Predicate predicate = null;
        final GlobalSearchResultDTO result = new GlobalSearchResultDTO();
        if (sex != null) {
            GenderEnum gender = GenderEnum.MALE;
            if (GenderEnum.FEMALE.name().equals(sex)) {
                gender = GenderEnum.FEMALE;
            }
            final GenderEnum finalGender = gender;
            final UserEntity user = new UserEntity();
//            predicate = (user.getUsername().toLowerCase().contains(query) ||
//                    user.getFirstName().contains(query) || user.getLastName().contains(query)) && finalGender
//                    .name().equals(user.getGender().name());
        }

        if (ageMax != null) {

        }

        if (ageMin != null) {

        }

        if (sex != null && ageMax != null) {

        }

        if (sex != null && ageMin != null) {

        }

        if (ageMin != null && ageMax != null) {

        }

//        this.userTransformer.modelToDto(this.userRepository.findAll(predicate))
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GlobalSearchResultDTO findAllTeamFromCriteria(final String query, final Integer sport, final Integer page) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GlobalSearchResultDTO findAllSppotisFromCriteria(final String query, final Integer sport,
                                                            final Date startDate,
                                                            final Integer page) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GlobalSearchResultDTO findAllWithoutCriteria(final String query, final Integer page) {
        return null;
    }

    private Pageable getPage(final Integer page) {
        return new PageRequest(page, this.searchListSize);
    }
}