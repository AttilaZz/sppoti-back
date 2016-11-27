/**
 * 
 */
package com.fr.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Created by: Wail DJENANE on Nov 11, 2016
 */

@RestController
@RequestMapping("/notif")
public class NotificationController {

	private static Logger LOGGER = Logger.getLogger(NotificationController.class);

	@RequestMapping(value = "/update/{notifId}", method = RequestMethod.GET)
	public ResponseEntity<Void> getRole(@PathVariable("notifId") Long id, HttpServletRequest request) {

		LOGGER.info("Session ID -> " + RequestContextHolder.currentRequestAttributes().getSessionId());

		return new ResponseEntity<>(HttpStatus.OK);

	}

}
