package com.fr.impl;

import com.fr.commons.dto.search.GlobalSearchResultDTO;
import com.fr.commons.enumeration.GenderEnum;
import com.fr.commons.enumeration.TeamStatus;
import com.fr.commons.utils.SppotiUtils;
import com.fr.entities.QSppotiEntity;
import com.fr.entities.QTeamEntity;
import com.fr.entities.QUserEntity;
import com.fr.service.GlobalSearchService;
import com.fr.transformers.SppotiTransformer;
import com.fr.transformers.TeamTransformer;
import com.fr.transformers.UserTransformer;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by wdjenane on 22/06/2017.
 */
@Component
public class GlobalSearchServiceImpl extends AbstractControllerServiceImpl implements GlobalSearchService
{
	
	private final UserTransformer userTransformer;
	private final TeamTransformer teamTransformer;
	private final SppotiTransformer sppotiTransformer;
	
	/** Friend list size. */
	@Value("${key.globalSearchPerPage}")
	private Integer searchListSize;
	
	@Autowired
	public GlobalSearchServiceImpl(final UserTransformer userTransformer, final TeamTransformer teamTransformer,
								   final SppotiTransformer sppotiTransformer)
	{
		this.userTransformer = userTransformer;
		this.teamTransformer = teamTransformer;
		this.sppotiTransformer = sppotiTransformer;
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
		
		result.getUsers().addAll(this.userTransformer
				.iterableModelsToDtos(this.userRepository.findAll(predicate, getPage(page)).getContent()));
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GlobalSearchResultDTO findAllTeamsFromCriteria(final String query, final Long sport, final Integer page) {
		
		final GlobalSearchResultDTO result = new GlobalSearchResultDTO();
		
		final Predicate predicate;
		Predicate predicate1 = null;
		
		if (sport != null) {
			predicate1 = QTeamEntity.teamEntity.sport.eq(this.sportRepository.findOne(sport))
					.and(QTeamEntity.teamEntity.type.eq(TeamStatus.PUBLIC));
		}
		
		predicate = QTeamEntity.teamEntity.name.containsIgnoreCase(query).and(predicate1);
		
		result.getTeams().addAll(this.teamTransformer
				.iterableModelsToDtos(this.teamRepository.findAll(predicate, getPage(page)).getContent()));
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GlobalSearchResultDTO findAllSppotisFromCriteria(final String query, final Long sport,
															final String startDate, final Integer page)
	{
		final GlobalSearchResultDTO result = new GlobalSearchResultDTO();
		
		final Predicate predicate;
		Predicate predicate1 = null;
		Predicate predicate2 = null;
		
		if (sport != null) {
			predicate1 = QSppotiEntity.sppotiEntity.sport.eq(this.sportRepository.findOne(sport));
		}
		
		if (StringUtils.hasText(startDate)) {
			final Date date = new Date(Long.parseLong(startDate));
			predicate2 = QSppotiEntity.sppotiEntity.dateTimeStart.eq(date);
		}
		
		predicate = QSppotiEntity.sppotiEntity.name.containsIgnoreCase(query).and(predicate1).and(predicate2);
		
		result.getSppoties().addAll(this.sppotiTransformer
				.iterableModelsToDtos(this.sppotiRepository.findAll(predicate, getPage(page)).getContent()));
		
		return result;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GlobalSearchResultDTO findAllWithoutCriteria(final String query, final Integer page) {
		
		final GlobalSearchResultDTO result = new GlobalSearchResultDTO();
		
		final Predicate predicate1;
		final Predicate predicate2;
		final Predicate predicate3;
		
		predicate1 = QUserEntity.userEntity.username.containsIgnoreCase(query)
				.or(QUserEntity.userEntity.firstName.containsIgnoreCase(query))
				.or(QUserEntity.userEntity.lastName.containsIgnoreCase(query));
		result.getUsers().addAll(this.userTransformer
				.iterableModelsToDtos(this.userRepository.findAll(predicate1, getPage(page)).getContent()));
		
		predicate2 = QTeamEntity.teamEntity.name.containsIgnoreCase(query);
		result.getTeams().addAll(this.teamTransformer
				.iterableModelsToDtos(this.teamRepository.findAll(predicate2, getPage(page)).getContent()));
		
		predicate3 = QSppotiEntity.sppotiEntity.name.containsIgnoreCase(query);
		result.getSppoties().addAll(this.sppotiTransformer
				.iterableModelsToDtos(this.sppotiRepository.findAll(predicate3, getPage(page)).getContent()));
		
		return result;
	}
	
	private Pageable getPage(final Integer page) {
		return new PageRequest(page, this.searchListSize);
	}
}