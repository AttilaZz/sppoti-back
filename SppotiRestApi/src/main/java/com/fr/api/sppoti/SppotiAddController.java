package com.fr.api.sppoti;

import com.fr.commons.dto.sppoti.SppotiDTO;
import com.fr.commons.exception.BusinessGlobalException;
import com.fr.service.SppotiBusinessService;
import com.fr.versionning.ApiVersion;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	private final Logger LOGGER = LoggerFactory.getLogger(SppotiAddController.class);
	
	@Autowired
	private SppotiBusinessService sppotiControllerService;
	
	@PostMapping
	ResponseEntity<SppotiDTO> addSppoti(@RequestBody @Valid final SppotiDTO newSppoti)
	{
		this.LOGGER.info("Request received to save sppoti: {}", newSppoti);
		
		if (newSppoti.getMaxTeamCount() == 0) {
			throw new BusinessGlobalException("Max team count must be greater than 0");
		}
		
		if (StringUtils.isBlank(newSppoti.getMyTeamId()) && newSppoti.getTeamHost() == null) {
			throw new BusinessGlobalException("A team host is required to add sppoti");
		}
		
		return new ResponseEntity<>(this.sppotiControllerService.saveSppoti(newSppoti), HttpStatus.CREATED);
		
	}
	
}