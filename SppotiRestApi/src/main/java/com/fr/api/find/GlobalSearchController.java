package com.fr.api.find;

import com.fr.commons.dto.search.GlobalSearchResultDTO;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.service.GlobalSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Add class description.
 *
 * Created by wdjenane on 23/06/2017.
 */
@RestController
@RequestMapping("/find/all")
public class GlobalSearchController
{
	
	private final GlobalSearchService globalSearchService;
	
	@Autowired
	public GlobalSearchController(final GlobalSearchService globalSearchService) {
		this.globalSearchService = globalSearchService;
	}
	
	@GetMapping
	public ResponseEntity<GlobalSearchResultDTO> findData(@RequestParam final int type, @RequestParam final String sex,
														  @RequestParam("age_max") final Integer ageMax,
														  @RequestParam("age_min") final Integer ageMin,
														  @RequestParam final Long sport,
														  @RequestParam("start_date") final String startDate,
														  @RequestParam final String query,
														  @RequestParam final int page)
	{
		
		
		GlobalSearchResultDTO search = new GlobalSearchResultDTO();
		if (StringUtils.hasText(query)) {
			switch (type) {
				case 1:
					// search users
					search = this.globalSearchService.findAllUsersFromCriteria(query, sex, ageMax, ageMin, page);
					break;
				case 2:
					//search teams
					search = this.globalSearchService.findAllTeamFromCriteria(query, sport, page);
					break;
				case 3:
					//search sppotis
					search = this.globalSearchService.findAllSppotisFromCriteria(query, sport, startDate, page);
					break;
				case 4:
					//all - without criteria
					search = this.globalSearchService.findAllWithoutCriteria(query, page);
					break;
				default:
					throw new BusinessGlobalException("Search type not correct");
			}
		}
		
		return ResponseEntity.ok(search);
	}
}
