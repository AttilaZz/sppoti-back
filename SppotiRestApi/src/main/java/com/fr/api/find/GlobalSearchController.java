package com.fr.api.find;

import com.fr.commons.dto.search.GlobalSearchResultDTO;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.service.GlobalSearchBusinessService;
import com.fr.versionning.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Search controller
 *
 * types:
 * (1) users, (2) teams, (3) sppoti
 *
 * Created by wdjenane on 23/06/2017.
 */
@RestController
@RequestMapping("/find/all")
@ApiVersion("1")
public class GlobalSearchController
{
	private final Logger LOGGER = LoggerFactory.getLogger(GlobalSearchController.class);
	
	private final GlobalSearchBusinessService globalSearchService;
	
	@Autowired
	public GlobalSearchController(final GlobalSearchBusinessService globalSearchService) {
		this.globalSearchService = globalSearchService;
	}
	
	@GetMapping
	public ResponseEntity<GlobalSearchResultDTO> findData(@RequestParam final Long[] types,
														  @RequestParam final String sex,
														  @RequestParam("age_max") final Integer ageMax,
														  @RequestParam("age_min") final Integer ageMin,
														  @RequestParam Long[] sport,
														  @RequestParam("start_date") final String startDate,
														  @RequestParam final String query,
														  @RequestParam final int page)
	{
		this.LOGGER
				.info("Request sent to global search APi, with types {}, query {}, sex {}, ageMax {}, ageMin {}, sport {}, startDate {}, page {}",
						Arrays.toString(types), query, sex, ageMax, ageMin, sport, startDate, page);
		
		if (sport.length == 0) {
			sport = new Long[] {1L, 2L, 3L};
		}
		
		final GlobalSearchResultDTO search = new GlobalSearchResultDTO();
		Long type1;
		final Long type2;
		final Long type3;
		
		if (StringUtils.hasText(query)) {
			
			final List<Long> arrayListTypes = new ArrayList<>();
			arrayListTypes.addAll(Arrays.asList(types));
			
			if (arrayListTypes.isEmpty()) {
				this.globalSearchService.findAllWithoutCriteria(query, page);
			} else {
				
				type1 = arrayListTypes.get(0);
				
				if (arrayListTypes.size() == 1) {
					
					if (type1.intValue() == 1) {
						this.LOGGER.info("Request to Search APi to find users with query {}", query);
						search.getUsers().addAll(this.globalSearchService
								.findAllUsersFromCriteria(query, sex, ageMax, ageMin, page).getUsers());
					}
					
					if (type1.intValue() == 2) {
						this.LOGGER.info("Request to Search APi to find teams with query {}", query);
						search.getTeams().addAll(this.globalSearchService.findAllTeamsFromCriteria(query, sport, page)
								.getTeams());
					}
					
					if (type1.intValue() == 3) {
						this.LOGGER.info("Request to Search APi to find sppotis with query {}", query);
						search.getSppoties().addAll(this.globalSearchService
								.findAllSppotisFromCriteria(query, sport, startDate, page).getSppoties());
					}
					
				} else if (arrayListTypes.size() == 2) {
					
					type1 = arrayListTypes.get(0);
					type2 = arrayListTypes.get(1);
					
					if (type1.intValue() == 1 || type2.intValue() == 1) {
						this.LOGGER.info("Request to Search APi to find users with query {}", query);
						search.getUsers().addAll(this.globalSearchService
								.findAllUsersFromCriteria(query, sex, ageMax, ageMin, page).getUsers());
					}
					
					if (type1.intValue() == 2 || type2.intValue() == 2) {
						this.LOGGER.info("Request to Search APi to find teams with query {}", query);
						search.getTeams().addAll(this.globalSearchService.findAllTeamsFromCriteria(query, sport, page)
								.getTeams());
					}
					
					if (type1.intValue() == 3 || type2.intValue() == 3) {
						this.LOGGER.info("Request to Search APi to find sppotis with query {}", query);
						search.getSppoties().addAll(this.globalSearchService
								.findAllSppotisFromCriteria(query, sport, startDate, page).getSppoties());
					}
					
				} else if (arrayListTypes.size() == 3) {
					
					type1 = arrayListTypes.get(0);
					type2 = arrayListTypes.get(1);
					type3 = arrayListTypes.get(2);
					
					if (type1.intValue() == 1 || type2.intValue() == 1 || type3.intValue() == 1) {
						this.LOGGER.info("Request to Search APi to find users with query {}", query);
						search.getUsers().addAll(this.globalSearchService
								.findAllUsersFromCriteria(query, sex, ageMax, ageMin, page).getUsers());
					}
					
					if (type1.intValue() == 2 || type2.intValue() == 2 || type3.intValue() == 2) {
						this.LOGGER.info("Request to Search APi to find teams with query {}", query);
						search.getTeams().addAll(this.globalSearchService.findAllTeamsFromCriteria(query, sport, page)
								.getTeams());
					}
					
					if (type1.intValue() == 3 || type2.intValue() == 3 || type3.intValue() == 3) {
						this.LOGGER.info("Request to Search APi to find sppotis with query {}", query);
						search.getSppoties().addAll(this.globalSearchService
								.findAllSppotisFromCriteria(query, sport, startDate, page).getSppoties());
					}
					
				}
			}
			return ResponseEntity.ok(search);
			
		}
		
		throw new BusinessGlobalException("Query not found in the request");
		
	}
	
}
