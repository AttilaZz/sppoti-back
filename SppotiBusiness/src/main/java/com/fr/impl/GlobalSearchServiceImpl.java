package com.fr.impl;

import com.fr.commons.dto.search.GlobalSearchResultDTO;
import com.fr.commons.enumeration.GenderEnum;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.QUserEntity;
import com.fr.service.GlobalSearchService;
import com.fr.transformers.UserTransformer;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by wdjenane on 22/06/2017.
 */
@Component
public class GlobalSearchServiceImpl extends AbstractControllerServiceImpl implements GlobalSearchService
{
	
	private final UserTransformer userTransformer;
	
	/** Friend list size. */
	@Value("${key.globalSearchPerPage}")
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
														  final Integer ageMin, final Integer page)
	{
		final GlobalSearchResultDTO result = new GlobalSearchResultDTO();
		
		final Predicate predicate;
		Predicate predicate1 = null;
		Predicate predicate2 = null;
		Predicate predicate3 = null;
		
		if (StringUtils.hasText(sex)) {
			GenderEnum gender = GenderEnum.MALE;
			if ("f".equals(sex)) {
				gender = GenderEnum.FEMALE;
			}
			predicate1 = QUserEntity.userEntity.gender.eq(gender);
		}
		
		if (ageMax != null) {
			//Dates are greater when they are in the past
			predicate2 = QUserEntity.userEntity.dateBorn.gt(SppotiUtils.getDateOfBorn(ageMax));
		}
		
		if (ageMin != null) {
			predicate3 = QUserEntity.userEntity.dateBorn.lt(SppotiUtils.getDateOfBorn(ageMin));
		}
		
		predicate = (QUserEntity.userEntity.username.containsIgnoreCase(query)
				.or(QUserEntity.userEntity.firstName.containsIgnoreCase(query))
				.or(QUserEntity.userEntity.lastName.containsIgnoreCase(query))).and(predicate1).and(predicate2)
				.and(predicate3);
		
		result.getUsers().addAll(this.userTransformer.iterableModelsToDtos(this.userRepository.findAll(predicate)));
		
		return result;
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
															final String startDate, final Integer page)
	{
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