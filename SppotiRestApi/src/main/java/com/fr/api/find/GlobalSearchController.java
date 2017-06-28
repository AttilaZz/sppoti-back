package com.fr.api.find;

import com.fr.commons.dto.search.GlobalSearchResultDTO;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.service.GlobalSearchService;
import com.fr.versionning.ApiVersion;
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
 * Add class description.
 *
 * Created by wdjenane on 23/06/2017.
 */
@RestController
@RequestMapping("/find/all")
@ApiVersion("1")
public class GlobalSearchController
{
	
	private final GlobalSearchService globalSearchService;
	
	@Autowired
	public GlobalSearchController(final GlobalSearchService globalSearchService) {
		this.globalSearchService = globalSearchService;
	}
	
	@GetMapping
	public ResponseEntity<GlobalSearchResultDTO> findData(@RequestParam final Long[] types,
														  @RequestParam final String sex,
														  @RequestParam("age_max") final Integer ageMax,
														  @RequestParam("age_min") final Integer ageMin,
														  @RequestParam final Long sport,
														  @RequestParam("start_date") final String startDate,
														  @RequestParam final String query,
														  @RequestParam final int page)
	{
		
		
		GlobalSearchResultDTO search = new GlobalSearchResultDTO();
		if (StringUtils.hasText(query)) {
			
			final List<Long> arrayListTypes = new ArrayList<>();
			arrayListTypes.addAll(Arrays.asList(types));
			
			if (arrayListTypes.isEmpty()) {
				this.globalSearchService.findAllWithoutCriteria(query, page);
			} else {
				
				if (arrayListTypes.size() == 1) {
					switch (arrayListTypes.get(0).intValue()) {
						case 1:
							// search users
							search = this.globalSearchService
									.findAllUsersFromCriteria(query, sex, ageMax, ageMin, page);
							break;
						case 2:
							//search teams
							search = this.globalSearchService.findAllTeamsFromCriteria(query, sport, page);
							break;
						case 3:
							//search sppotis
							search = this.globalSearchService.findAllSppotisFromCriteria(query, sport, startDate, page);
							break;
						default:
							throw new BusinessGlobalException("Search type not correct");
					}
				} else if (arrayListTypes.size() == 2) {
				
				} else if (arrayListTypes.size() == 3) {
				
				}
			}
			
			return ResponseEntity.ok(search);
		}
		
		throw new BusinessGlobalException("Query not found in the request");
	}
}
