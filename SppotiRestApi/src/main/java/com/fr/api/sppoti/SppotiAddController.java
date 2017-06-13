package com.fr.api.sppoti;

import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.service.SppotiControllerService;
import com.fr.versionning.ApiVersion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by: Wail DJENANE on Jul 11, 2016
 */

@RestController
@RequestMapping("/sppoti")
@ApiVersion("1")
class SppotiAddController
{
	
	/** Sppoti service. */
	private SppotiControllerService sppotiControllerService;
	
	/** Init service. */
	@Autowired
	void setSppotiControllerService(final SppotiControllerService sppotiControllerService)
	{
		this.sppotiControllerService = sppotiControllerService;
	}
	
	/**
	 * @param newSppoti
	 * 		sppoti to save.
	 *
	 * @return 201 status && SppotiEntity object with the inserted data, 400 status otherwise.
	 */
	@PostMapping
	ResponseEntity<SppotiDTO> addSppoti(@RequestBody @Valid final SppotiDTO newSppoti)
	{

		if (newSppoti.getMaxTeamCount() == 0) {
			throw new BusinessGlobalException("Max team count must be greater than 0");
		}
		
		if (StringUtils.isBlank(newSppoti.getMyTeamId()) && newSppoti.getTeamHost() == null) {
			throw new BusinessGlobalException("A team host is required to add sppoti");
		}
		
		return new ResponseEntity<>(this.sppotiControllerService.saveSppoti(newSppoti), HttpStatus.CREATED);
		
	}
	
}