package com.fr.api;

import com.fr.entities.SportEntity;
import com.fr.service.SportBusinessService;
import com.fr.versionning.ApiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Djenane Wail on 12/10/16.
 */

@RestController
@RequestMapping("/sport")
@ApiVersion("1")
class SportController
{
	private final Logger LOGGER = LoggerFactory.getLogger(SportController.class);
	
	@Autowired
	private SportBusinessService sportService;
	
	@GetMapping(value = "/all")
	ResponseEntity<Object> getAllSports(final HttpServletRequest req)
	{
		this.LOGGER.info("Request sent to retrieve sport list");
		final Device currentDevice = (Device) req.getAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE);
		
		final List<SportEntity> allSports = this.sportService.getAllSports();
		
		if (allSports.isEmpty()) {
			this.LOGGER.info("Sport list is empty in database");
			return new ResponseEntity<>(allSports, HttpStatus.NO_CONTENT);
		}
		
		this.LOGGER.info("Sport list has been returned: {}", allSports);
		return new ResponseEntity<>(allSports, HttpStatus.OK);
		
	}
	
}